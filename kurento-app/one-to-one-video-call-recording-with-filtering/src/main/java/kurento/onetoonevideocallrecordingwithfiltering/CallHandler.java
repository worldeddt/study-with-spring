package kurento.onetoonevideocallrecordingwithfiltering;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.*;
import org.kurento.jsonrpc.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class CallHandler extends TextWebSocketHandler {

    private final ConcurrentHashMap<String, MediaPipeline> pipelines
            = new ConcurrentHashMap<>();

    @Autowired
    private KurentoClient kurentoClient;

    @Autowired
    private UserRegistry userRegistry;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Gson gson = new Gson();
        JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
        UserSession user = userRegistry.getBySession(session);

        if (user != null) {
            log.debug("Incoming message from user '{}': {}", user.getName(), jsonMessage);
        } else {
            log.debug("Incoming message from new user: {}", jsonMessage);
        }

        log.info("message id : {}", jsonMessage.get("id").getAsString());

        switch (jsonMessage.get("id").getAsString()) {
            case "register":
                register(session, jsonMessage);
                break;
            case "call":
                call(user, jsonMessage);
                break;
            case "incomingCallResponse":
                incomingCallResponse(user, jsonMessage);
                break;
            case "play":
                log.info("jsonMessage from play = {}", jsonMessage);
                play(user, jsonMessage);
                break;
            case "onIceCandidate": {
                JsonObject candidate = jsonMessage.get("candidate").getAsJsonObject();

                log.info("candidate = {}", candidate);

                if (user != null) {
                    IceCandidate cand =
                            new IceCandidate(candidate.get("candidate").getAsString(), candidate.get("sdpMid")
                                    .getAsString(), candidate.get("sdpMLineIndex").getAsInt());
                    user.addCandidate(cand);
                }
                break;
            }
            case "stop":
                stop(session);
                releasePipeline(user);
                break;
            case "stopPlay":
                releasePipeline(user);
                break;
            default:
                break;
        }

        super.handleTextMessage(session, message);
    }

    private void register(WebSocketSession session, JsonObject jsonMessage) throws IOException {
        String name = jsonMessage.getAsJsonPrimitive("name").getAsString();

        UserSession caller = new UserSession(session, name);
        String responseMessage = "accepted";

        if (name.isEmpty()) {
            responseMessage = "rejected: empty user name";
        } else if (userRegistry.exists(name)) {
            responseMessage = "rejected: user '"+name+"' already registered";
        } else {
            userRegistry.register(caller);
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", "registerResponse");
        jsonObject.addProperty("response", responseMessage);
        caller.sendMessage(jsonObject);
    }

    private void call(UserSession caller, JsonObject jsonMessage) throws IOException {
        String toName = jsonMessage.get("to").getAsString();
        String fromName = jsonMessage.get("from").getAsString();
        JsonObject response = new JsonObject();
        log.info("to Name when calling = {}", toName);
        log.info("userRegistry.exists(toName) = {}", userRegistry.exists(toName));

        if (userRegistry.exists(toName)) {
            caller.setSdpOffer(jsonMessage.getAsJsonPrimitive("sdpOffer").getAsString());
            caller.setCallingTo(toName);

            response.addProperty("id", "incomingCall");
            response.addProperty("from", fromName);

            // 새로 만들기 : 받는 이
            UserSession callee = userRegistry.getByName(toName);
            callee.sendMessage(response);
            callee.setCallingFrom(fromName);
        } else {
            response.addProperty("id", "callResponse");
            response.addProperty("response", "rejected");
            response.addProperty("message", "user '"+toName +"' is not registered");

            caller.sendMessage(response);
        }
    }

    private void incomingCallResponse(final UserSession callee, JsonObject jsonMessage) throws
            IOException {
        String callResponse = jsonMessage.get("callResponse").getAsString();
        String fromName = jsonMessage.get("from").getAsString();

        final UserSession calleer = userRegistry.getByName(fromName);
        //콜러에서 이미 등록된 받는이를 꺼낸다.
        String toName = calleer.getCallingTo();

        log.info("call Response = {}", callResponse);
        if ("accept".equals(callResponse)) {
            log.info("Accepted call from '{}' to '{}'", fromName, toName);

            //파이프 생성 및 유저간 파이프 연결 설정
            CallMediaPipeline callMediaPipeline
                    = new CallMediaPipeline(kurentoClient, fromName, toName);
            pipelines.put(calleer.getSessionId(), callMediaPipeline.getPipeline());
            pipelines.put(callee.getSessionId(), callMediaPipeline.getPipeline());

            // 미디어 파이프 객체에서 RTC 앤드 포인트를 가져와서 세팅한다.
            callee.setWebRtcEndpoint(callMediaPipeline.getCalleeWebRtcEp());
            // 해당 앤드포인트에 이벤트를 설정해 준다.
            callMediaPipeline.getCalleeWebRtcEp().addIceCandidateFoundListener(
                new EventListener<IceCandidateFoundEvent>() {
                    @Override
                    public void onEvent(IceCandidateFoundEvent iceCandidateFoundEvent) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("id","iceCandidate");
                        jsonObject.add("candidate",
                                JsonUtils.toJsonObject(iceCandidateFoundEvent.getCandidate()));

                        //candidate 정보를 메세지로 전송한다.
                        try {
                            synchronized (callee.getSession()) {
                                callee.getSession().sendMessage(
                                        new TextMessage(
                                                jsonObject.toString()
                                        )
                                );
                            }
                        } catch (IOException e) {
                            log.error("error message = {}", e.getMessage());
                        }
                    }
                }
            );

            String calleeSdpOffer = jsonMessage.get("sdpOffer").getAsString();
            String calleeSdpAnswer = callMediaPipeline.generateSdpAnswerForCallee(calleeSdpOffer);

            JsonObject startCommunication = new JsonObject();
            startCommunication.addProperty("id", "startCommunication");
            startCommunication.addProperty("adpAnswer", calleeSdpAnswer);

            synchronized (callee) {
                // call 을 받은 쪽에 커뮤니케이션이 시작되었다고 전송.
                callee.sendMessage(startCommunication);
            }

            callMediaPipeline.getCalleeWebRtcEp().gatherCandidates();
            String callerSdpOffer = userRegistry.getByName(fromName).getSdpOffer();

            calleer.setWebRtcEndpoint(callMediaPipeline.getCallerWebRtcEp());
            callMediaPipeline.getCalleeWebRtcEp().addIceCandidateFoundListener(
                new EventListener<IceCandidateFoundEvent>() {
                    @Override
                    public void onEvent(IceCandidateFoundEvent iceCandidateFoundEvent) {
                        JsonObject response = new JsonObject();
                        response.addProperty("id", "iceCandidate");
                        response.add("candidate", JsonUtils
                                .toJsonObject(iceCandidateFoundEvent.getCandidate()));

                        try {
                            synchronized (calleer.getSession()) {
                                calleer.getSession().sendMessage(new
                                        TextMessage(response.toString()));
                            }
                        } catch (IOException e) {
                            log.info("exception = {}", e.getMessage());
                        }
                    }
                }
            );

            String callerSdpAnswer = callMediaPipeline.generateSdpAnswerForCaller(callerSdpOffer);

            JsonObject response = new JsonObject();
            response.addProperty("id", "callResponse");
            response.addProperty("response", "accepted");
            response.addProperty("sdpAnswer", callerSdpAnswer);

            synchronized (calleer) {
                //전화를 건 사람에게 callResponse 반환.
                calleer.sendMessage(response);
            }

            callMediaPipeline.getCallerWebRtcEp().gatherCandidates();

            callMediaPipeline.record();
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", "callResponse");
            jsonObject.addProperty("response", "rejected");
            calleer.sendMessage(jsonObject);
        }

    }

    public void stop(WebSocketSession session) throws IOException {
        // Both users can stop the communication. A 'stopCommunication'
        // message will be sent to the other peer.
        UserSession stopperUser = userRegistry.getBySession(session);
        if (stopperUser != null) {
            UserSession stoppedUser =
                    (stopperUser.getCallingFrom() != null) ? userRegistry.getByName(stopperUser.getCallingFrom())
                            : stopperUser.getCallingTo() != null ? userRegistry.getByName(stopperUser.getCallingTo())
                            : null;

            if (stoppedUser != null) {
                JsonObject message = new JsonObject();
                message.addProperty("id", "stopCommunication");
                stoppedUser.sendMessage(message);
                stoppedUser.clear();
            }
            stopperUser.clear();
        }
    }

    public void releasePipeline(UserSession session) {
        String sessionId = session.getSessionId();
        // set to null the endpoint of the other user

        if (pipelines.containsKey(sessionId)) {
            pipelines.get(sessionId).release();
            pipelines.remove(sessionId);
        }
        session.setWebRtcEndpoint(null);
        session.setPlayingWebRtcEndpoint(null);

        UserSession stoppedUser =
                (session.getCallingFrom() != null) ? userRegistry.getByName(session.getCallingFrom())
                        : userRegistry.getByName(session.getCallingTo());
        stoppedUser.setWebRtcEndpoint(null);
        stoppedUser.setPlayingWebRtcEndpoint(null);
    }

    private void play(final UserSession session, JsonObject jsonMessage) throws IOException {
        String userName = jsonMessage.get("user").getAsString();
        log.info("Playing recorded call of user '{}'", userName);

        JsonObject response = new JsonObject();
        response.addProperty("id", "playResponse");

        if (userRegistry.getByName(userName) != null &&
                userRegistry.getBySession(session.getSession()) != null
        ) {

            log.info("kurento client = {}", kurentoClient);

            final PlayMediaPipeline playMediaPipeline =
                    new PlayMediaPipeline(kurentoClient, userName, session.getSession());

            session.setPlayingWebRtcEndpoint(playMediaPipeline.getWebRtc());

            // stream 등록
            playMediaPipeline.getPlayer().addEndOfStreamListener(new EventListener<EndOfStreamEvent>() {
                @Override
                public void onEvent(EndOfStreamEvent endOfStreamEvent) {
                    UserSession userSession = userRegistry
                            .getBySession(session.getSession());

                    releasePipeline(userSession);
                    playMediaPipeline.sendPlayEnd(userSession.getSession());
                }
            });

            playMediaPipeline.getWebRtc().addIceCandidateFoundListener(
                new EventListener<IceCandidateFoundEvent>() {
                    @Override
                    public void onEvent(IceCandidateFoundEvent iceCandidateFoundEvent) {
                        log.info("ice candidate found event = {}", iceCandidateFoundEvent);
                        JsonObject response = new JsonObject();
                        response.addProperty("id", "iceCandidate");
                        response.add("candidate", JsonUtils.toJsonObject(
                                iceCandidateFoundEvent.getCandidate()
                        ));
                        try {
                            synchronized (session) {
                                session.getSession()
                                        .sendMessage(new TextMessage(response.toString()));
                            }
                        } catch (IOException e) {
                            log.info("message = {}", e.getMessage());
                        }
                    }
                }
            );

            String sdpOffer = jsonMessage.get("sdpOffer").getAsString();
            String sdpAnswer = playMediaPipeline.generateSdpAnswer(sdpOffer);

            response.addProperty("response", "accepted");
            response.addProperty("adpAnswer", sdpAnswer);

            log.debug("play media pipeline played");
            playMediaPipeline.play();
            pipelines.put(session.getSessionId(), playMediaPipeline.getPipeline());

            synchronized (session.getSession()) {
                session.sendMessage(response);
            }

            playMediaPipeline.getWebRtc().gatherCandidates();
        } else {
            response.addProperty("response", "rejected");
            response.addProperty("error", "No recording for user '" + userName
                    + "'. Please type a correct user in the 'Peer' field.");
            session.getSession().sendMessage(new TextMessage(response.toString()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
            throws Exception {
        stop(session);
        userRegistry.removeBySession(session);
        super.afterConnectionClosed(session, status);
    }
}
