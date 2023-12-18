package webchat.webrtc3phase.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import webchat.webrtc3phase.controller.dto.CreateRoom;
import webchat.webrtc3phase.controller.dto.RoomList;
import webchat.webrtc3phase.service.RoomService;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/room")
@RequiredArgsConstructor
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("")
    public String create(@RequestBody CreateRoom createRoom) throws IOException {

        return "roomId";
    }

    @GetMapping("/all/{subsId}")
    public ResponseEntity rooms(@PathVariable String subsId) {
        log.info("message = {}", subsId);

        List<String> roomAll = roomService.findRoomAll(subsId);
        RoomList init = RoomList.init(roomAll);

        return ResponseEntity.ok(init);
    }
}

