package aop.prototypes.common.controller;


import aop.prototypes.common.controller.dto.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final String allRoomKey = "rooms";
    private final RedisTemplate<String, List<Room>> redisTemplateRoom;

    @GetMapping(value = "/room/list")
    public ResponseEntity<?> findList() {
        return ResponseEntity.ok(redisTemplateRoom.opsForValue().get(allRoomKey));
    }

    @PostMapping(value = "/room")
    public ResponseEntity<?> save(@RequestBody Room room) {
        List<Room> rooms = redisTemplateRoom.opsForValue().get(allRoomKey);

        if (rooms == null) {
            List<Room> room1 = new ArrayList<>();
            room1.add(room);
            redisTemplateRoom.opsForValue().set(allRoomKey, room1);
        } else {
            rooms.add(room);
            redisTemplateRoom.opsForValue().set(allRoomKey, rooms);
        }

        return ResponseEntity.ok().build();
    }
}
