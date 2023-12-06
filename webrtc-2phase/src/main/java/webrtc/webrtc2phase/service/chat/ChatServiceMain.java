package webrtc.webrtc2phase.service.chat;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import webrtc.webrtc2phase.controller.presentations.CreateRoom;
import webrtc.webrtc2phase.dto.ChatRoomDto;
import webrtc.webrtc2phase.dto.ChatRoomMap;
import webrtc.webrtc2phase.dto.ChatType;
import webrtc.webrtc2phase.dto.KurentoRoomDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceMain {


    //todo 방 개설, 방안 인원 배정, 채팅반 조회
    private final KurentoManager kurentoManager;
    private final MsgChatService msgChatService;
    private final RtcChatService rtcChatService;

    public List<ChatRoomDto> findAllRoom() {

        List<ChatRoomDto> chatRooms = new ArrayList<>(
                ChatRoomMap.getInstance().getChatRooms().values()
        );

        Collections.reverse(chatRooms);

        return chatRooms;
    }

    public ChatRoomDto findRoomById(String roomId) {
        return ChatRoomMap.getInstance().getChatRooms().get(roomId);
    }

    public ChatRoomDto createChatRoom(
            CreateRoom createRoom
    ) {
        if (createRoom.getChatType().equals("msgChat")) {
            return
                    msgChatService.createChatRoom(
                            createRoom
                    );
        }

        return rtcChatService.createChatRoom(createRoom);
    }

    // 채팅방 비밀번호 조회
    public boolean confirmPwd(String roomId, String roomPwd) {
//        String pwd = chatRoomMap.get(roomId).getRoomPwd();

        return roomPwd.equals(ChatRoomMap.getInstance().getChatRooms().get(roomId).getRoomPwd());

    }

    // 채팅방 인원+1
    public void plusUserCnt(String roomId){
        log.info("cnt {}",ChatRoomMap.getInstance().getChatRooms().get(roomId).getUserCount());
        ChatRoomDto room = ChatRoomMap.getInstance().getChatRooms().get(roomId);
        room.setUserCount(room.getUserCount()+1);
    }

    // 채팅방 인원-1
    public void minusUserCnt(String roomId){
        ChatRoomDto room = ChatRoomMap.getInstance().getChatRooms().get(roomId);
        int roomCnt = room.getUserCount()-1;
        if (roomCnt < 0) {
            roomCnt = 0;
        }
        room.setUserCount(roomCnt);
    }

    // maxUserCnt 에 따른 채팅방 입장 여부
    public boolean chkRoomUserCnt(String roomId){
        ChatRoomDto room = ChatRoomMap.getInstance().getChatRooms().get(roomId);

        if (room.getUserCount() + 1 > room.getMaxUserCnt()) {
            return false;
        }

        return true;
    }

    // 채팅방 삭제
    public void delChatRoom(String roomId){

        try {
            // 채팅방 타입에 따라서 단순히 채팅방만 삭제할지 업로드된 파일도 삭제할지 결정
            ChatRoomDto room = ChatRoomMap.getInstance().getChatRooms().remove(roomId);

            if (room.getChatType().equals(ChatType.MSG)) {
                // MSG 채팅방은 사진도 추가 삭제
                // 채팅방 안에 있는 파일 삭제
//                fileService.deleteFileDir(roomId);
            } else {
                KurentoRoomDto kurentoRoom = (KurentoRoomDto) room;
                kurentoManager.removeRoom(kurentoRoom);
            }

            log.info("삭제 완료 roomId : {}", roomId);

        } catch (Exception e) { // 만약에 예외 발생시 확인하기 위해서 try catch
            System.out.println(e.getMessage());
        }

    }
}
