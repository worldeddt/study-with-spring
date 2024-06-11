package aop.prototypes.common.controller;


import aop.prototypes.media.common.config.KurentoConfig;
import aop.prototypes.media.common.properties.KurentoProperties;
import aop.prototypes.common.controller.dto.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final String allRoomKey = "rooms";
    private final RedisTemplate<String, List<Room>> redisTemplateRoom;
    private final KurentoConfig kurentoConfig;
    private final KurentoProperties kurentoProperties;

    @GetMapping("/room/kurento/{status}")
    public void kurentoSync(@PathVariable(value = "status") String kurentoStatus) {

        List<String> urls = kurentoProperties.urls();
        kurentoConfig.changeKurentoStatus(urls.get(1), kurentoStatus);
    }

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
