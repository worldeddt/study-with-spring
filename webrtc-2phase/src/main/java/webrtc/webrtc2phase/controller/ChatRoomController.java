package webrtc.webrtc2phase.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import webrtc.webrtc2phase.controller.presentations.CreateRoom;
import webrtc.webrtc2phase.controller.presentations.request.RequestCreateRoom;
import webrtc.webrtc2phase.dto.ChatRoomDto;
import webrtc.webrtc2phase.dto.ChatRoomMap;
import webrtc.webrtc2phase.dto.ChatType;
import webrtc.webrtc2phase.service.chat.ChatServiceMain;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatServiceMain chatServiceMain;

    @PostMapping("/createroom")
    public ModelAndView createRoom(
            RequestCreateRoom requestCreateRoom,
            RedirectAttributes redirectAttributes
    ) {
        ChatRoomDto chatRoomDto =
                chatServiceMain.createChatRoom(
                        CreateRoom.of(
                                requestCreateRoom.getRoomName(),
                                requestCreateRoom.getRoomPwd(),
                                Boolean.parseBoolean(
                                        requestCreateRoom.getSecretChk()
                                ),
                                Integer.parseInt(requestCreateRoom.getMaxUserCnt()),
                                requestCreateRoom.getChatType()
                            )
                        );

        log.info("CREATE Chat Room [{}]", chatRoomDto);

        redirectAttributes.addFlashAttribute("roomName", chatRoomDto);
        return new ModelAndView( "roomlist");
    }


    // 채팅방 입장 화면
    // 파라미터로 넘어오는 roomId 를 확인후 해당 roomId 를 기준으로
    // 채팅방을 찾아서 클라이언트를 chatroom 으로 보낸다.
    @GetMapping("/room")
    public ModelAndView roomDetail(Model model,
                             String roomId){

        log.info("roomId {}", roomId);

        // principalDetails 가 null 이 아니라면 로그인 된 상태!!
//        if (principalDetails != null) {
//            // 세션에서 로그인 유저 정보를 가져옴
//            model.addAttribute("user", principalDetails.getUser());
//        }

        model.addAttribute("user", "BASIC_USER");
        ChatRoomDto room = ChatRoomMap.getInstance().getChatRooms().get(roomId);
        model.addAttribute("room", room);

        if (ChatType.MSG.equals(room.getChatType())) {
            return new ModelAndView("chatroom");
        }else{
            String uuid = UUID.randomUUID().toString().split("-")[0];
            model.addAttribute("uuid", uuid);
            model.addAttribute("roomId", room.getRoomId());
            model.addAttribute("roomName", room.getRoomName());

            return new ModelAndView("kurentoroom");
        }
    }


    @PostMapping("/confirmPwd/{roomId}")
    public boolean confirmPwd(@PathVariable String roomId,
                              @RequestParam String roomPwd) {

        return chatServiceMain.confirmPwd(roomId, roomPwd);
    }

    // 채팅방 삭제
    @GetMapping("/delRoom/{roomId}")
    public ModelAndView delChatRoom(@PathVariable String roomId){

        // roomId 기준으로 chatRoomMap 에서 삭제, 해당 채팅룸 안에 있는 사진 삭제
        chatServiceMain.delChatRoom(roomId);

        return new ModelAndView("roomlist");
    }

    // 유저 카운트
    @GetMapping("/chkUserCnt/{roomId}")
    @ResponseBody
    public boolean chUserCnt(@PathVariable String roomId){

        return chatServiceMain.chkRoomUserCnt(roomId);
    }
}
