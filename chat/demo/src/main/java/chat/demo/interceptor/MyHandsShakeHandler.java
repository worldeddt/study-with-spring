package chat.demo.interceptor;


import chat.demo.model.StompPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;


@Slf4j
@Component
public class MyHandsShakeHandler extends DefaultHandshakeHandler {


    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        StompPrincipal stompPrincipal = new StompPrincipal();
        log.info("stompPrincipal : {}", stompPrincipal.getName());

        return stompPrincipal;
    }
}
