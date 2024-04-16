package aop.prototypes.common.controller;


import aop.prototypes.common.controller.dto.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final String allRoomKey = "rooms";
    private final RedisTemplate<String, List<Room>> redisTemplateRoom;

    @GetMapping("/room/list")
    public ResponseEntity<?> findList() {
        return ResponseEntity.ok(redisTemplateRoom.opsForValue().get(allRoomKey));
    }

    @PostMapping("/room")
    public ResponseEntity<?> save(@RequestBody Room room) {
        List<Room> rooms = redisTemplateRoom.opsForValue().get(allRoomKey);
        assert rooms != null;
        rooms.add(room);

        redisTemplateRoom.opsForValue().set(allRoomKey, rooms);

        return ResponseEntity.ok().build();
    }
}
