package kurento.onetoonevideocall;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class CallHandler extends TextWebSocketHandler {

    private final UserSession userSession;
    private final UserRegistry userRegistry;
    private static final Gson gson = new GsonBuilder().create();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonObject jsonObject = gson.fromJson(message.getPayload(), JsonObject.class);

        UserSession bySession = userRegistry.getBySession(session);


        switch (jsonObject.get("id").getAsString()) {


            case "":
                break;
            default:
                break;

        }
    }
}
