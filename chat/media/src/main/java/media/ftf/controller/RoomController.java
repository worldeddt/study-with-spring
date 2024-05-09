package media.ftf.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.ftf.application.interfaces.RoomApiService;
import media.ftf.dto.request.RoomRequest;
import media.ftf.dto.response.CommonResponse;
import media.ftf.dto.response.RoomResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class RoomController {
    private final RoomApiService roomApiService;
    @PostMapping("/api/post/room")
    public CommonResponse<RoomResponse> createRoom(@RequestBody RoomRequest roomRequest) {
        final var roomResponse = roomApiService.createRoom(roomRequest);
        return CommonResponse.ok(roomResponse);
    }

}
