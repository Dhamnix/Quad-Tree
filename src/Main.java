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
     System.out.println("pixel Depth : " + tree.pixelDepth(2,2));
        String outputPath = "C:/Users/User/Desktop/Dataset/grayscale_CompressImage312.png";
          GrayscaleImage.generateImage(tree.compress(256), outputPath);




          //  String outputPath = "C:/Users/User/Desktop/Dataset/grayscale_image312.png";
          //   GrayscaleImage.generateImage(tree.toInt(), outputPath);
    }
}
