package webchat.webrtc3phase.presentation.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import webchat.webrtc3phase.dto.ChatMessage;
import webchat.webrtc3phase.presentation.request.CreateRoom;
import webchat.webrtc3phase.service.RoomService;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
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
    public List<String> rooms(@PathVariable String subsId) {
        log.info("message = {}", subsId);

        return roomService.findRoomAll(subsId);
    }
}
