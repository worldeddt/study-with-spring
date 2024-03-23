package aop.prototypes.openCV.application;


import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;


@Service
public class ImageProcessor {
    private final double newWidth = 200L;
    private final double newHeight = 200L;

    public void processImage() {
        String imagePath = "/Users/eddy/Desktop/study/study-with-spring/aop/src/main/java/aop/prototypes/openCV/samples/IMG_1533.jpg";
        // 이미지 로드
        Mat imread = Imgcodecs.imread("IMG_1533", 1);

        // 이미지 크기 조정
        Mat resizedImage = new Mat();

        Size newSize = new Size(newWidth, newHeight);
        Imgproc.resize(imread, resizedImage, newSize);
    }
}
