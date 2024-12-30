import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GrayscaleImageReader {

    public static int[][] readGrayscaleImage(String filePath) {
        int[][] image = new int[512][512];
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < 512) {
                String[] values = line.split(",");
                for (int col = 0; col < values.length && col < 512; col++) {
                    image[row][col] = Integer.parseInt(values[col].trim());
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in file: " + e.getMessage());
        }
        return image;
    }

    public static void main(String[] args) {
        String filePath = "path/to/your/image1_gray.csv"; // Update with the actual file path
        int[][] grayscaleImage = readGrayscaleImage(filePath);

        // Example: Print the first row of the image
        for (int i = 0; i < grayscaleImage[0].length; i++) {
            System.out.print(grayscaleImage[0][i] + " ");
        }
    }
}
