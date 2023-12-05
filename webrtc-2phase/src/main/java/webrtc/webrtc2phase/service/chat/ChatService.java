package webrtc.webrtc2phase.service.chat;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webrtc.webrtc2phase.dto.ChatRoomDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final KurentoManager kurentoManager;
    private final MsgChatService msgChatService;
    private final RtcChatService rtcChatService;


    public List<ChatRoomDto> findAllRoom() {

    }

}
