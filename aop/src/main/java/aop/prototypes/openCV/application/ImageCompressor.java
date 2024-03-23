package aop.prototypes.openCV.application;

import aop.prototypes.openCV.utils.OpenCVUtils;
import org.opencv.core.Mat;
import org.opencv.osgi.OpenCVNativeLoader;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class ImageCompressor {
    public void compressImage(Mat image) throws IOException {
        // Mat 이미지를 BufferedImage로 변환
        BufferedImage bufferedImage = OpenCVUtils.matToBufferedImage(image);

        // 이미지를 JPEG 형식으로 압축하여 저장
        File compressedFile = new File("../samples/compressed_image.jpg");
        ImageIO.write(bufferedImage, "jpeg", compressedFile);
    }
}
