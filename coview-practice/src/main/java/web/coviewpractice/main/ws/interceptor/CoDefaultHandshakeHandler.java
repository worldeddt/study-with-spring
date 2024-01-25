package web.coviewpractice.main.ws.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import web.coviewpractice.model.dto.StompPrincipal;

import java.security.Principal;
import java.util.Map;

@Component
public class CoDefaultHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(
            ServerHttpRequest request, WebSocketHandler webSocketHandler, Map<String, Object> attribute) {

        return new StompPrincipal();
    }
}
