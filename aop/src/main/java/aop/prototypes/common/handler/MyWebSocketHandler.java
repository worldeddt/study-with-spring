package aop.prototypes.common.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class MyWebSocketHandler extends TextWebSocketHandler {

    // 현재 활성화된 WebSocket 세션을 추적하는 맵
    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // WebSocket 연결이 열리면 호출됨
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        System.out.println("WebSocket connection established: " + session.getId());
    }

    // WebSocket 연결이 닫히면 호출됨
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        System.out.println("WebSocket connection closed: " + session.getId());
    }

    // 현재 활성화된 모든 WebSocket 세션을 반환
    public Map<String, WebSocketSession> getActiveSessions() {
        return sessions;
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("session :{}", session.getId());
        log.error("exception :{}", exception.getStackTrace());
    }
}
