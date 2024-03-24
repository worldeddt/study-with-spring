package aop.prototypes.imageIO.application;


import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


@Service
public class ImageCompression {

    public void main() {
        // 이미지 파일 경로
        String imagePath = "/Users/eddy/Desktop/study/study-with-spring/aop/src/main/java/aop/prototypes/imageIO/samples/IMG_1533.jpg";

        try {
            // 이미지 압축
            BufferedImage compressedImage = compressImage(imagePath);

            System.out.printf("compressed image? :"+compressedImage.getWidth());
            // 압축된 이미지를 파일로 저장
            saveCompressedImageToFile(compressedImage, "./compressed_image.jpg");

            System.out.println("이미지 압축 완료.");
        } catch (IOException e) {
            System.err.println("이미지 압축 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    private static BufferedImage compressImage(String imagePath) throws IOException {

        System.out.printf("imagePath : "+imagePath);
        File file = new File(imagePath);
        System.out.printf("file : "+file.getName());

        // 이미지 파일 읽기
        BufferedImage image = ImageIO.read(new File(imagePath));

        // 이미지 크기 변경 (예: 가로 500px, 세로 자동 비율 유지)
        int targetWidth = 500;
        int targetHeight = (int) ((double) image.getHeight() / image.getWidth() * targetWidth);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(image, 0, 0, targetWidth, targetHeight, null);
        graphics.dispose();

        return resizedImage;
    }

    private static void saveCompressedImageToFile(BufferedImage image, String outputFilePath) throws IOException {
        // 압축된 이미지를 파일로 저장
        ImageIO.write(image, "jpg", new File(outputFilePath));
    }
}

