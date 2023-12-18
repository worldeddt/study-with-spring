package webchat.webrtc3phase.presentation.controller;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import webchat.webrtc3phase.service.RoomService;

import java.util.List;




@SpringBootTest
class RoomControllerTest {

    @Autowired
    private RoomService roomService;

    @Test
    public void findAllRooms() {
        List<String> roomAll = this.roomService.findRoomAll("room:info:ferimRoom");

        Assertions.assertNotEquals(roomAll.size(), 0);
    }
}