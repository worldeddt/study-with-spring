package aop.prototypes.imageIO.controller;


import aop.prototypes.imageIO.application.ImageCompression;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image/IO")
@RequiredArgsConstructor
public class ImageIOController {

    private final ImageCompression imageCompression;

    @GetMapping("/compress")
    public void compress() {
        imageCompression.main();
    }
}
