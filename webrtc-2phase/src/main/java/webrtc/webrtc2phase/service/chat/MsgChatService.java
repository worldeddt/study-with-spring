package webrtc.webrtc2phase.service.chat;


import org.springframework.stereotype.Service;
import webrtc.webrtc2phase.controller.presentations.CreateRoom;
import webrtc.webrtc2phase.dto.ChatRoomDto;
import webrtc.webrtc2phase.dto.ChatRoomMap;
import webrtc.webrtc2phase.dto.ChatType;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MsgChatService {

    public ChatRoomDto createChatRoom(
            CreateRoom createRoom
    ) {
        // roomName 와 roomPwd 로 chatRoom 빌드 후 return
        ChatRoomDto room = ChatRoomDto.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(createRoom.getRoomName())
                .roomPwd(createRoom.getRoomPwd()) // 채팅방 패스워드
                .secretChk(createRoom.isSecretChk()) // 채팅방 잠금 여부
                .userCount(0) // 채팅방 참여 인원수
                .maxUserCnt(createRoom.getMaxUserCnt()) // 최대 인원수 제한
                .build();

        room.setUserList(new ConcurrentHashMap<>());

        // msg 타입이면 ChatType.MSG
        room.setChatType(ChatType.MSG);

        // map 에 채팅룸 아이디와 만들어진 채팅룸을 저장
        ChatRoomMap.getInstance().getChatRooms().put(
                room.getRoomId(), room);

        return room;
    }


    // 채팅방 유저 리스트에 유저 추가
    public String addUser(Map<String, ChatRoomDto> chatRoomMap, String roomId, String userName){
        ChatRoomDto room = chatRoomMap.get(roomId);
        String userUUID = UUID.randomUUID().toString();

        // 아이디 중복 확인 후 userList 에 추가
        //room.getUserList().put(userUUID, userName);

        // hashmap 에서 concurrentHashMap 으로 변경
        ConcurrentHashMap<String, String> userList = (ConcurrentHashMap<String, String>)room.getUserList();
        userList.put(userUUID, userName);


        return userUUID;
    }

    // 채팅방 유저 이름 중복 확인
    public String isDuplicateName(Map<String, ChatRoomDto> chatRoomMap, String roomId, String username){
        ChatRoomDto room = chatRoomMap.get(roomId);
        String tmp = username;

        // 만약 userName 이 중복이라면 랜덤한 숫자를 붙임
        // 이때 랜덤한 숫자를 붙였을 때 getUserlist 안에 있는 닉네임이라면 다시 랜덤한 숫자 붙이기!
        while(room.getUserList().containsValue(tmp)){
            int ranNum = (int) (Math.random()*100)+1;

            tmp = username+ranNum;
        }

        return tmp;
    }

    // 채팅방 userName 조회
    public String findUserNameByRoomIdAndUserUUID(Map<String, ChatRoomDto> chatRoomMap, String roomId, String userUUID){
        ChatRoomDto room = chatRoomMap.get(roomId);
        return (String) room.getUserList().get(userUUID);
    }

    // 채팅방 전체 userlist 조회
    public ArrayList<String> getUserList(Map<String, ChatRoomDto> chatRoomMap, String roomId){
        ArrayList<String> list = new ArrayList<>();

        ChatRoomDto room = chatRoomMap.get(roomId);

        // hashmap 을 for 문을 돌린 후
        // value 값만 뽑아내서 list 에 저장 후 reutrn
        room.getUserList().forEach((key, value) -> list.add((String) value));
        return list;
    }

    // 채팅방 특정 유저 삭제
    public void delUser(Map<String, ChatRoomDto> chatRoomMap, String roomId, String userUUID){
        ChatRoomDto room = chatRoomMap.get(roomId);
        room.getUserList().remove(userUUID);
    }
}
