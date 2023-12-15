package webchat.webrtc3phase.handler;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import webchat.webrtc3phase.domain.Room;
import webchat.webrtc3phase.presentation.request.CreateRoom;
import webchat.webrtc3phase.service.RoomService;

@Slf4j
public class ChatHandler extends TextWebSocketHandler {

    private final Gson gson = new Gson();

    @Autowired
    private RoomService roomService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);

        Thread t = new Thread();

        log.info("session id = {}", session.getId());
        log.info("message id = {}", jsonMessage.get("id").getAsString());

        switch (jsonMessage.get("id").getAsString()) {

            case "requestRoom" :
                Room room = roomService.createRoom(
                        CreateRoom.init(
                                jsonMessage.get("senderId").getAsString(),
                                jsonMessage.get("subsId").getAsString(),
                                session.getId(),
                                jsonMessage.get("roomName").getAsString(),
                                null
                        )
                );

                log.info("room id = {}", room.getRoomId());
                break;
            default:
                break;
        }


        super.handleTextMessage(session, message);
    }
}
