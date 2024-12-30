public class Main {
    public static void main(String[] args) {
            String filePath = "C:/Users/User/Desktop/Dataset/image1_gray.csv"; // Update with the actual file path
            int[][] grayscaleImage = GrayscaleImage.readGrayscaleCSV(filePath);

            if (grayscaleImage != null) {
                System.out.println("Grayscale image data loaded successfully.");
                System.out.println("Image dimensions: " + grayscaleImage.length + "x" + grayscaleImage[0].length);

                // Example: Print the first row
                for (int i = 0; i < grayscaleImage[0].length; i++) {
                    System.out.print(grayscaleImage[0][i] + " ");
                }
            } else {
                System.out.println("Failed to load grayscale image data.");
            }
            String outputPath = "C:/Users/User/Desktop/Dataset/grayscale_image.png";
            GrayscaleImage.generateImage(grayscaleImage, outputPath);
            System.out.println("Image Generated to " + outputPath);
        }
}
