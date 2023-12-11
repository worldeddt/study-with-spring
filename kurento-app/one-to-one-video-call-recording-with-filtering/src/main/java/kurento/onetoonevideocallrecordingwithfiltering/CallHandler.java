package kurento.onetoonevideocallrecordingwithfiltering;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.EventListener;
import org.kurento.client.IceCandidateFoundEvent;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.kurento.jsonrpc.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
        UserSession userSession = userRegistry.getBySession(session);

        if (user != null) {
            log.debug("Incoming message from user '{}': {}", user.getName(), jsonMessage);
        } else {
            log.debug("Incoming message from new user: {}", jsonMessage);
        }

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
        String toName = calleer.getCallingTo();

        if ("accept".equals(callResponse)) {
            log.info("Accepted call from '{}' to '{}'", fromName, toName);

            CallMediaPipeline callMediaPipeline
                    = new CallMediaPipeline(kurentoClient, fromName, toName);
            pipelines.put(calleer.getSessionId(), callMediaPipeline.getPipeline());
            pipelines.put(callee.getSessionId(), callMediaPipeline.getPipeline());

            callee.setWebRtcEndpoint(callMediaPipeline.getCalleeWebRtcEp());
            callMediaPipeline.getCalleeWebRtcEp().addIceCandidateFoundListener(
                new EventListener<IceCandidateFoundEvent>() {
                    @Override
                    public void onEvent(IceCandidateFoundEvent iceCandidateFoundEvent) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("id","iceCandidate");
                        jsonObject.add("candidate",
                                JsonUtils.toJsonObject(iceCandidateFoundEvent.getCandidate()));
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

        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", "callResponse");
            jsonObject.addProperty("response", "rejected");
            calleer.sendMessage(jsonObject);
        }

    }
}
