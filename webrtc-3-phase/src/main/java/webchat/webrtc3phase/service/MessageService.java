package webchat.webrtc3phase.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import webchat.webrtc3phase.controller.dto.AdditionalUser;
import webchat.webrtc3phase.controller.dto.ChatDto;
import webchat.webrtc3phase.controller.dto.FindUserName;
import webchat.webrtc3phase.dto.ChatRoomDto;

import java.lang.runtime.ObjectMethods;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    public String additionalUser(AdditionalUser additionalUser) {
        ChatRoomDto chatRoom = additionalUser.getChatRoom();
        String userUUID = UUID.randomUUID().toString();

        if (chatRoom == null) return "administrator";

        ConcurrentHashMap<String, String> userList = (ConcurrentHashMap<String, String>) chatRoom.getUserList();
        userList.put(userUUID, additionalUser.getUsername());

        return userUUID;
    }

    public String findUserNameByRoomIdAndUserUUID(FindUserName findUserName){
        ChatRoomDto room = findUserName.getChatRoom();
        return (String) room.getUserList().get(findUserName.getUserUUID());
    }

    public void deleteUser(FindUserName findUserName) {
        ChatRoomDto room = findUserName.getChatRoom();
        room.getUserList().remove(findUserName.getUserUUID());
    }


    public void sendMessage(String message) throws Exception {
        log.info("pub message = {}", message);
        ChatDto chatDto = objectMapper.readValue(message, ChatDto.class);
        messagingTemplate.convertAndSend("/sub/chat/room2/"+chatDto.getRoomId(), chatDto);
    }
}
