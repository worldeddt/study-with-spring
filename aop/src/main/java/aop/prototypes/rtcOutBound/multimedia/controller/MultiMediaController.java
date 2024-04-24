package aop.prototypes.rtcOutBound.multimedia.controller;


import aop.prototypes.rtcOutBound.multimedia.application.MultiMediaService;
import aop.prototypes.rtcOutBound.multimedia.controller.dto.MultiMedia;
import lombok.RequiredArgsConstructor;
import org.checkerframework.common.reflection.qual.GetMethod;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MultiMediaController {
    private final MultiMediaService multiMediaService;

    @GetMapping("/multi/{roomId}")
    public void get(@PathVariable(value = "roomId") String roomId) {
        multiMediaService.get(roomId);
    }

    @PostMapping("/multi")
    public void save(@RequestBody MultiMedia multiMedia) {
        multiMediaService.save(multiMedia.getRoomId());
    }
}
