package kurento.onetoonecallrecording.messinger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kurento.onetoonecallrecording.media.PlayMediaPipeline;
import kurento.onetoonecallrecording.user.UserRegistry;
import kurento.onetoonecallrecording.user.UserSession;
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
    private final ConcurrentHashMap<String, MediaPipeline> pipelines =
            new ConcurrentHashMap<>();

    private final Gson gson = new Gson();

    @Autowired
    private KurentoClient kurento;

    @Autowired
    private UserRegistry registry;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
        UserSession user = registry.getBySession(session);

        if (user != null) {
            log.debug("Incoming message from user '{}': {}", user.getName(), jsonMessage);
        } else {
            log.debug("Incoming message from new user: {}", jsonMessage);
        }

        log.info("message id = {}", jsonMessage.get("id").getAsString());

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
                play(user, jsonMessage);
                break;
            case "onIceCandidate": {
                JsonObject candidate = jsonMessage.get("candidate").getAsJsonObject();

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
    }

    private void register(WebSocketSession session, JsonObject jsonMessage) throws IOException {
        String name = jsonMessage.getAsJsonPrimitive("name").getAsString();

        UserSession caller = new UserSession(session, name);
        String responseMsg = "accepted";
        if (name.isEmpty()) {
            responseMsg = "rejected: empty user name";
        } else if (registry.exists(name)) {
            responseMsg = "rejected: user '" + name + "' already registered";
        } else {
            registry.register(caller);
        }

        JsonObject response = new JsonObject();
        response.addProperty("id", "registerResponse");
        response.addProperty("response", responseMsg);
        caller.sendMessage(response);
    }

    private void call(UserSession caller, JsonObject jsonMessage) throws IOException {
        String to = jsonMessage.get("to").getAsString();
        String from = jsonMessage.get("from").getAsString();
        JsonObject response = new JsonObject();

        if (registry.exists(to)) {
            caller.setSdpOffer(jsonMessage.getAsJsonPrimitive("sdpOffer").getAsString());
            caller.setCallingTo(to);

            response.addProperty("id", "incomingCall");
            response.addProperty("from", from);

            UserSession callee = registry.getByName(to);
            callee.sendMessage(response);
            callee.setCallingFrom(from);
        } else {
            response.addProperty("id", "callResponse");
            response.addProperty("response", "rejected");
            response.addProperty("message", "user '" + to + "' is not registered");

            caller.sendMessage(response);
        }
    }

    private void incomingCallResponse(final UserSession callee, JsonObject jsonMessage)
            throws IOException {
        String callResponse = jsonMessage.get("callResponse").getAsString();
        String from = jsonMessage.get("from").getAsString();
        final UserSession calleer = registry.getByName(from);
        String to = calleer.getCallingTo();

        if ("accept".equals(callResponse)) {
            log.debug("Accepted call from '{}' to '{}'", from, to);

            CallMediaPipeline callMediaPipeline = new CallMediaPipeline(kurento, from, to);
            pipelines.put(calleer.getSessionId(), callMediaPipeline.getPipeline());
            pipelines.put(callee.getSessionId(), callMediaPipeline.getPipeline());

            callee.setWebRtcEndpoint(callMediaPipeline.getCalleeWebRtcEp());
            callMediaPipeline.getCalleeWebRtcEp().addIceCandidateFoundListener(
                    new EventListener<IceCandidateFoundEvent>() {

                        @Override
                        public void onEvent(IceCandidateFoundEvent event) {
                            JsonObject response = new JsonObject();
                            response.addProperty("id", "iceCandidate");
                            response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
                            try {
                                synchronized (callee.getSession()) {
                                    callee.getSession().sendMessage(new TextMessage(response.toString()));
                                }
                            } catch (IOException e) {
                                log.debug(e.getMessage());
                            }
                        }
                    });

            String calleeSdpOffer = jsonMessage.get("sdpOffer").getAsString();
            String calleeSdpAnswer = callMediaPipeline.generateSdpAnswerForCallee(calleeSdpOffer);
            JsonObject startCommunication = new JsonObject();
            startCommunication.addProperty("id", "startCommunication");
            startCommunication.addProperty("sdpAnswer", calleeSdpAnswer);

            synchronized (callee) {
                callee.sendMessage(startCommunication);
            }

            callMediaPipeline.getCalleeWebRtcEp().gatherCandidates();

            String callerSdpOffer = registry.getByName(from).getSdpOffer();

            calleer.setWebRtcEndpoint(callMediaPipeline.getCallerWebRtcEp());
            callMediaPipeline.getCallerWebRtcEp().addIceCandidateFoundListener(
                    new EventListener<IceCandidateFoundEvent>() {

                        @Override
                        public void onEvent(IceCandidateFoundEvent event) {
                            JsonObject response = new JsonObject();
                            response.addProperty("id", "iceCandidate");
                            response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
                            try {
                                synchronized (calleer.getSession()) {
                                    calleer.getSession().sendMessage(new TextMessage(response.toString()));
                                }
                            } catch (IOException e) {
                                log.debug(e.getMessage());
                            }
                        }
                    });

            String callerSdpAnswer = callMediaPipeline.generateSdpAnswerForCaller(callerSdpOffer);

            JsonObject response = new JsonObject();
            response.addProperty("id", "callResponse");
            response.addProperty("response", "accepted");
            response.addProperty("sdpAnswer", callerSdpAnswer);

            synchronized (calleer) {
                calleer.sendMessage(response);
            }

            callMediaPipeline.getCallerWebRtcEp().gatherCandidates();

            callMediaPipeline.record();

        } else {
            JsonObject response = new JsonObject();
            response.addProperty("id", "callResponse");
            response.addProperty("response", "rejected");
            calleer.sendMessage(response);
        }
    }

    public void stop(WebSocketSession session) throws IOException {
        // Both users can stop the communication. A 'stopCommunication'
        // message will be sent to the other peer.
        UserSession stopperUser = registry.getBySession(session);
        if (stopperUser != null) {
            UserSession stoppedUser =
                    (stopperUser.getCallingFrom() != null) ? registry.getByName(stopperUser.getCallingFrom())
                            : stopperUser.getCallingTo() != null ? registry.getByName(stopperUser.getCallingTo())
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

        if (pipelines.containsKey(sessionId)) {
            pipelines.get(sessionId).release();
            pipelines.remove(sessionId);
        }
        session.setWebRtcEndpoint(null);
        session.setPlayingWebRtcEndpoint(null);

        // set to null the endpoint of the other user
        UserSession stoppedUser =
                (session.getCallingFrom() != null) ? registry.getByName(session.getCallingFrom())
                        : registry.getByName(session.getCallingTo());
        stoppedUser.setWebRtcEndpoint(null);
        stoppedUser.setPlayingWebRtcEndpoint(null);
    }

    private void play(final UserSession session, JsonObject jsonMessage) throws IOException {
        String user = jsonMessage.get("user").getAsString();
        log.info("Playing recorded call of user '{}'", user);

        JsonObject response = new JsonObject();
        response.addProperty("id", "playResponse");

        if (registry.getByName(user) != null && registry.getBySession(session.getSession()) != null) {
            final PlayMediaPipeline playMediaPipeline =
                    new PlayMediaPipeline(kurento, user, session.getSession());

            //session 에 파이프라인을 설정하는데 web rtc에서 앤드포인트를 꺼내와서 pipeline
            // 앤드 포인트에 설정하여 준다.
            session.setPlayingWebRtcEndpoint(playMediaPipeline.getWebRtc());

            playMediaPipeline.getPlayer().addEndOfStreamListener(new EventListener<EndOfStreamEvent>() {
                @Override
                public void onEvent(EndOfStreamEvent event) {
                    UserSession user = registry.getBySession(session.getSession());
                    releasePipeline(user);
                    playMediaPipeline.sendPlayEnd(session.getSession());
                }
            });

            playMediaPipeline.getWebRtc().addIceCandidateFoundListener(
                    new EventListener<IceCandidateFoundEvent>() {

                        @Override
                        public void onEvent(IceCandidateFoundEvent event) {
                            JsonObject response = new JsonObject();
                            response.addProperty("id", "iceCandidate");
                            response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
                            try {
                                synchronized (session) {
                                    session.getSession().sendMessage(new TextMessage(response.toString()));
                                }
                            } catch (IOException e) {
                                log.debug(e.getMessage());
                            }
                        }
                    });

            String sdpOffer = jsonMessage.get("sdpOffer").getAsString();
            String sdpAnswer = playMediaPipeline.generateSdpAnswer(sdpOffer);

            response.addProperty("response", "accepted");

            response.addProperty("sdpAnswer", sdpAnswer);

            playMediaPipeline.play();
            pipelines.put(session.getSessionId(), playMediaPipeline.getPipeline());
            synchronized (session.getSession()) {
                session.sendMessage(response);
            }

            playMediaPipeline.getWebRtc().gatherCandidates();

        } else {
            response.addProperty("response", "rejected");
            response.addProperty("error", "No recording for user '" + user
                    + "'. Please type a correct user in the 'Peer' field.");
            session.getSession().sendMessage(new TextMessage(response.toString()));
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
