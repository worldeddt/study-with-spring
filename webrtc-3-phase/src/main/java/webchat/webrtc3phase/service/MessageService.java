package webchat.webrtc3phase.service;


import org.springframework.stereotype.Service;
import webchat.webrtc3phase.controller.dto.AdditionalUser;
import webchat.webrtc3phase.controller.dto.FindUserName;
import webchat.webrtc3phase.dto.ChatRoomDto;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageService {



    public String additionalUser(AdditionalUser additionalUser) {
        ChatRoomDto chatRoom = additionalUser.getChatRoom();
        String userUUID = UUID.randomUUID().toString();

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
}
