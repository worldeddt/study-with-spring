package webrtcproto2.webrtc.handler;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import webrtcproto2.webrtc.domain.Room;
import webrtcproto2.webrtc.domain.WebSocketMessage;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Slf4j
public class SocketHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private LinkedHashMap<String, Room> sessionIdToRoomMap = new LinkedHashMap<String, Room>();
    private static final String MSG_TYPE_TEXT = "text";
    private static final String MSG_TYPE_OFFER = "offer";
    private static final String MSG_TYPE_ANSWER = "answer";
    private static final String MSG_TYPE_ICE = "ice";
    private static final String MSG_TYPE_JOIN = "join";
    private static final String MSG_TYPE_LEAVE = "leave";
    

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sendMessage(session, new WebSocketMessage(
                "Server",
                MSG_TYPE_JOIN, Boolean.toString(!sessionIdToRoomMap.isEmpty()),
                null, null
        ));
    }

    @Override
    public void handleMessage(WebSocketSession session, 
                              org.springframework.web.socket.WebSocketMessage<?> message) throws Exception {
        try {

            WebSocketMessage  webSocketMessage = 
                    objectMapper.readValue((JsonParser) message.getPayload(), WebSocketMessage.class);
            
            String userName = webSocketMessage.getFrom();
            String data = webSocketMessage.getData();
            
            
            Room room;

            switch (message.getType()) {
                // text message from client has been received
                case MSG_TYPE_TEXT:
                    log.debug("[ws] Text message: {}", message.getData());
                    // message.data is the text sent by client
                    // process text message if needed
                    break;

                // process signal received from client
                case MSG_TYPE_OFFER:
                case MSG_TYPE_ANSWER:
                case MSG_TYPE_ICE:
                    Object candidate = message.getCandidate();
                    Object sdp = message.getSdp();
                    log.debug("[ws] Signal: {}",
                            candidate != null
                                    ? candidate.toString().substring(0, 64)
                                    : sdp.toString().substring(0, 64));

                    Room rm = sessionIdToRoomMap.get(session.getId());
                    if (rm != null) {
                        Map<String, WebSocketSession> clients = roomService.getClients(rm);
                        for(Map.Entry<String, WebSocketSession> client : clients.entrySet())  {
                            // send messages to all clients except current user
                            if (!client.getKey().equals(userName)) {
                                // select the same type to resend signal
                                sendMessage(client.getValue(),
                                        new WebSocketMessage(
                                                userName,
                                                message.getType(),
                                                data,
                                                candidate,
                                                sdp));
                            }
                        }
                    }
                    break;

                // identify user and their opponent
                case MSG_TYPE_JOIN:
                    // message.data contains connected room id
                    log.debug("[ws] {} has joined Room: #{}", userName, message.getData());
                    room = roomService.findRoomByStringId(data)
                            .orElseThrow(() -> new IOException("Invalid room number received!"));
                    // add client to the Room clients list
                    roomService.addClient(room, userName, session);
                    sessionIdToRoomMap.put(session.getId(), room);
                    break;

                case MSG_TYPE_LEAVE:
                    // message data contains connected room id
                    log.debug("[ws] {} is going to leave Room: #{}", userName, message.getData());
                    // room id taken by session id
                    room = sessionIdToRoomMap.get(session.getId());
                    // remove the client which leaves from the Room clients list
                    Optional<String> client = roomService.getClients(room).entrySet().stream()
                            .filter(entry -> Objects.equals(entry.getValue().getId(), session.getId()))
                            .map(Map.Entry::getKey)
                            .findAny();
                    client.ifPresent(c -> roomService.removeClientByName(room, c));
                    break;

                // something should be wrong with the received message, since it's type is unrecognizable
                default:
                    log.debug("[ws] Type of the received message {} is undefined!", message.getType());
                    // handle this if needed
            }

        } catch (IOException e) {
            log.debug("An error occured: {}", e.getMessage());
        }
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("exception error : {}", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessionIdToRoomMap.remove(session.getId());
        log.debug("session was removed = {}",session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessage(WebSocketSession session, WebSocketMessage message) throws Exception {
        String json = objectMapper.writeValueAsString(message);
        session.sendMessage(new TextMessage(json));
    }
}
