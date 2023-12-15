package webchat.webrtc3phase.presentation.controller;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import webchat.webrtc3phase.service.RoomService;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;




@SpringBootTest
class RoomControllerTest {

    private RoomService roomService;

    @BeforeAll
    public void init() {
        this.roomService  = new RoomService();
    }

    @Test
    public void findAllRooms() {
        List<String> roomAll = this.roomService.findRoomAll("room:info:ferimRoom");

        Assertions.assertNotEquals(roomAll.size(), 0);
    }
}