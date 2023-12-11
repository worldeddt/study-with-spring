package kurento.onetoonevideocall;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.EventListener;
import org.kurento.client.IceCandidate;
import org.kurento.client.IceCandidateFoundEvent;
import org.kurento.client.KurentoClient;
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

    @Autowired
    private KurentoClient kurentoClient;

    @Autowired
    private UserRegistry userRegistry;

    private final ConcurrentHashMap<String, CallMediaPipeline> pipelines
            = new ConcurrentHashMap<>();

    private static final Gson gson = new GsonBuilder().create();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);

        UserSession user = userRegistry.getBySession(session);

        if (user != null) {
            log.debug("Incoming message from user '{}': {}", user.getName(), jsonMessage);
        } else {
            log.debug("Incoming message from new user: {}", jsonMessage);
        }

        switch (jsonMessage.get("id").getAsString()) {
            case "register" :
                try {
                    log.info("register message = {}", jsonMessage);
                    register(session, jsonMessage);
                } catch (Exception e) {
                    handleErrorResponse(e, session, "registerResponse");
                }
                break;
            case "call":
                try {
                    log.info("call message = {}", jsonMessage);
                    call(user, jsonMessage);
                } catch (Exception e) {
                    handleErrorResponse(e, session, "callResponse");
                }
                break;
            case "incomingCallResponse" :
                log.info("incomingCallResponse message = {}", jsonMessage);
                incomingCallResponse(user, jsonMessage);
                break;
            case "onIceCandidate" :
                log.info("onIceCandidate message = {}", jsonMessage);
                JsonObject candidate = jsonMessage.get("candidate").getAsJsonObject();

                if (user != null) {
                    IceCandidate newCandidate
                            = new IceCandidate(
                                    candidate.get("candidate").getAsString(),
                            candidate.get("sdpMid").getAsString(),
                            candidate.get("sdpMLineIndex").getAsInt()
                    );
                    user.addCandidate(newCandidate);
                }
                break;
            case "stop" :
                stop(session);
                break;
            default:
                break;

        }
    }

    private void handleErrorResponse(
            Throwable throwable, WebSocketSession session, String responseId
    ) throws  IOException {
        stop(session);
        log.error(throwable.getMessage(), throwable);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", responseId);
        jsonObject.addProperty("response", "rejected");
        jsonObject.addProperty("messages", throwable.getMessage());
        session.sendMessage(new TextMessage(jsonObject.toString()));
    }

    private void register(WebSocketSession session, JsonObject jsonMessage) throws IOException {
        String name = jsonMessage.getAsJsonPrimitive("name").getAsString();

        UserSession caller = new UserSession(session, name);
        String responseMessage = "accepted";
        if (name.isEmpty())
        {
            responseMessage = "rejected: empty user name ";
        }
        else if (userRegistry.exists(name))
        {
            responseMessage = " rejected: user '"+name+"' already registered";
        }
        else
        {
            userRegistry.register(caller);
        }

        JsonObject response = new JsonObject();
        response.addProperty("id", "registerResponse");
        response.addProperty("response", responseMessage);
        caller.sendMessage(response);
    }

    private void call(UserSession caller, JsonObject jsonMessage) throws IOException {
        String to = jsonMessage.get("to").getAsString();
        String from = jsonMessage.get("from").getAsString();
        JsonObject response = new JsonObject();

        if (userRegistry.exists(to)) {
            caller.setSdpOffer(jsonMessage.getAsJsonPrimitive("sdpOffer").getAsString());
            caller.setCallingTo(to);

            response.addProperty("id", "incomingCall");
            response.addProperty("from", from);

            UserSession callee = userRegistry.getByName(to);
            callee.sendMessage(response);
            callee.setCallingFrom(from);
        } else {
            response.addProperty("id", "callResponse");
            response.addProperty("response", "rejected: user '"+to+"' is not registered");

            caller.sendMessage(response);
        }
    }

    public void stop(WebSocketSession session) throws IOException {
        String sessionId = session.getId();

        if (pipelines.containsKey(sessionId)) {
            pipelines.get(sessionId).release();
            CallMediaPipeline pipeline = pipelines.remove(sessionId);
            pipeline.release();

            UserSession stopperUser = userRegistry.getBySession(session);

            if (stopperUser != null) {
                UserSession stopperedUser =
                        (stopperUser.getCallingFrom() != null)
                                ? userRegistry.getByName(stopperUser.getCallingFrom())
                                : stopperUser.getCallingTo() != null
                                ? userRegistry.getByName(stopperUser.getCallingTo())
                                : null;

                if (stopperedUser != null) {
                    JsonObject message = new JsonObject();
                    message.addProperty("id", "stopCommunication");
                    stopperedUser.sendMessage(message);
                    stopperedUser.clear();
                }
                stopperUser.clear();

            }
        }
    }

    private void incomingCallResponse(final UserSession callee, JsonObject jsonMessage) throws IOException {

        String callResponse = jsonMessage.get("callResponse").getAsString();
        String fromName = jsonMessage.get("from").getAsString();

        final UserSession calleer = userRegistry.getByName(fromName);
        String toName = calleer.getCallingTo();

        if ("accept".equals(callResponse)) {
            log.info("Accepted call from '{}' to '{}'", fromName, toName);

            CallMediaPipeline pipeline = null;
            try {
                pipeline = new CallMediaPipeline(kurentoClient);

                pipelines.put(calleer.getSessionId(), pipeline);
                pipelines.put(callee.getSessionId(), pipeline);

                callee.setWebRtcEndpoint(pipeline.getCalleeWebRtcEp());
                pipeline.getCalleeWebRtcEp().addIceCandidateFoundListener(
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
                                log.info(e.getMessage());
                            }
                        }
                    });

                calleer.setWebRtcEndpoint(pipeline.getCallerWebRtcEp());
                pipeline.getCallerWebRtcEp().addIceCandidateFoundListener(
                    new EventListener<IceCandidateFoundEvent>() {
                        @Override
                        public void onEvent(IceCandidateFoundEvent iceCandidateFoundEvent) {
                            JsonObject response = new JsonObject();
                            response.addProperty("id", "iceCandidate");
                            response.add("candidate", JsonUtils.toJsonObject(
                                    iceCandidateFoundEvent.getCandidate()
                            ));

                            try {
                                synchronized (calleer.getSession()) {
                                    calleer.getSession().sendMessage(new TextMessage(response.toString()));
                                }
                            } catch (IOException e) {
                                log.info(e.getMessage());
                            }
                        }
                    }
                );

                String calleeSdpOffer = jsonMessage.get("sdpOffer").getAsString();
                String calleeSdpAnswer = pipeline.generateSdpAnswerForCallee(calleeSdpOffer);

                JsonObject startCommunication = new JsonObject();
                startCommunication.addProperty("id", "startCommunication");
                startCommunication.addProperty("sdpAnswer", calleeSdpAnswer);

                synchronized (callee) {
                    callee.sendMessage(startCommunication);
                }

                pipeline.getCalleeWebRtcEp().gatherCandidates();

                String callerSdpOffer = userRegistry.getByName(fromName).getSdpOffer();
                String callerSdpAnswer = pipeline.generateSdpAnswerForCaller(callerSdpOffer);

                JsonObject response = new JsonObject();
                response.addProperty("id", "callResponse");
                response.addProperty("response", "accepted");
                response.addProperty("sdpAnswer", callerSdpAnswer);

                synchronized (calleer) {
                    calleer.sendMessage(response);
                }

                pipeline.getCallerWebRtcEp().gatherCandidates();

            } catch (Exception e) {
                log.error(e.getMessage(), e);

                if (pipeline != null) pipeline.release();

                pipelines.remove(calleer.getSessionId());
                pipelines.remove(callee.getSessionId());

                JsonObject response = new JsonObject();
                response.addProperty("id", "callResponse");
                response.addProperty("response", "rejected");
                calleer.sendMessage(response);

                response = new JsonObject();
                response.addProperty("id", "stopCommunication");
                callee.sendMessage(response);
            }
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", "callResponse");
            jsonObject.addProperty("response", "rejected");
            calleer.sendMessage(jsonMessage);
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        stop(session);
        userRegistry.removeBySession(session);
        super.afterConnectionClosed(session, status);
    }
}
