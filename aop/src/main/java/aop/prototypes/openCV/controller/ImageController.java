package aop.prototypes.openCV.controller;

import aop.prototypes.openCV.application.ImageCompressor;
import aop.prototypes.openCV.application.ImageProcessor;
import lombok.RequiredArgsConstructor;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RequestMapping("/image")
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageProcessor imageProcessor;
    private final ImageCompressor imageCompressor;

    @GetMapping("/process")
    public void process() {
        imageProcessor.processImage();
    }

    @GetMapping("/compress")
    public void compress() throws IOException {
        String imagePath = "../samples/IMG_1533.jpg";
//        Mat imread = Imgcodecs.imread(imagePath);
        System.out.printf("boolean : "+Imgcodecs.haveImageReader(imagePath));
//        imageCompressor.compressImage(imread);
    }
}
