package webchat.webrtc3phase.presentation.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webchat.webrtc3phase.presentation.request.CreateRoom;
import webchat.webrtc3phase.service.RoomService;

import java.io.IOException;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("")
    public String create(@RequestBody CreateRoom createRoom) throws IOException {

        return "roomId";
    }
}
