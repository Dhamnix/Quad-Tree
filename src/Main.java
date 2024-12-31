public class Main {
    public static void main(String[] args) {
            String filePath = "C:/Users/User/Desktop/Dataset/image3_gray.csv";
            int[][] grayscaleImage = GrayscaleImage.readGrayscaleCSV(filePath);
            if (grayscaleImage != null) {
                System.out.println("Grayscale image data loaded successfully.");
                System.out.println("Image dimensions: " + grayscaleImage.length + "x" + grayscaleImage[0].length);

            } else {
                System.out.println("Failed to load grayscale image data.");
            }


     QuadTree tree = new QuadTree(grayscaleImage);
     System.out.println("Tree Depth: " + tree.TreeDepth());
     System.out.println("pixel 0,0 Depth : " + tree.pixelDepth(0,0));





           /*  String outputPath = "C:/Users/User/Desktop/Dataset/grayscale_image3.png";
             GrayscaleImage.generateImage(grayscaleImage, outputPath);
             System.out.println("Image Generated to " + outputPath);*/
    }
}
