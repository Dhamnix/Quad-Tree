import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GrayscaleImageGenerator {

    public static void generateImage(int[][] grayscaleData, String outputPath) {
        int width = 512;
        int height = 512;

        // Create a BufferedImage with the specified width and height
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // Set pixel values from the grayscaleData array
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int grayValue = grayscaleData[y][x]; // Assuming values are 0-255
                int rgb = (grayValue << 16) | (grayValue << 8) | grayValue; // Create RGB value
                image.setRGB(x, y, rgb);
            }
        }

        // Write the image to the specified file path
        try {
            File outputFile = new File(outputPath);
            ImageIO.write(image, "png", outputFile);
            System.out.println("Image saved to: " + outputPath);
        } catch (IOException e) {
            System.err.println("Error saving the image: " + e.getMessage());
        }
    }
}
