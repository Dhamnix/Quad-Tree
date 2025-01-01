import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

public class GrayscaleImage {

    public static int[][] readGrayscaleCSV(String filePath) {
        int[][] grayscaleData = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip the first line (indices)
            br.readLine();

            // Read the second line (grayscale values)
            String secondLine = br.readLine();
            String[] values = secondLine.split(",");
            int totalCells = values.length;

            // Compute dimensions
            int dimension = (int) Math.sqrt(totalCells);
            if (dimension * dimension != totalCells) {
                throw new IllegalArgumentException("Total cells do not form a perfect square.");
            }

            // Initialize the array
            grayscaleData = new int[dimension][dimension];

            // Populate the array
            int index = 0;
            for (int row = 0; row < dimension; row++) {
                for (int col = 0; col < dimension; col++) {
                    grayscaleData[row][col] = Integer.parseInt(values[index].trim());
                    index++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in the file: " + e.getMessage());
        }

        return grayscaleData;
    }

    public static void generateImage(int[][] grayscaleData, String outputPath) {
        int height = grayscaleData.length;
        int width = grayscaleData[0].length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int grayValue = grayscaleData[y][x];
                int rgb = (grayValue << 16) | (grayValue << 8) | grayValue; // Gray RGB value
                image.setRGB(x, y, rgb);
            }
        }

        try {
            File outputFile = new File(outputPath);
            ImageIO.write(image, "png", outputFile);
            System.out.println("Image saved to: " + outputPath);
        } catch (IOException e) {
            System.err.println("Error saving the image: " + e.getMessage());
        }
    }
}
