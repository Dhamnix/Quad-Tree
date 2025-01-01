public class Main {
    public static void main(String[] args) {
          //  String filePath = "image1_gray.csv"; //mal abol
            String filePath = "C:/Users/User/Desktop/Dataset/image1_gray.csv"; //mal agha ehsan
            int[][] grayscaleImage = GrayscaleImage.readGrayscaleCSV(filePath);
            if (grayscaleImage != null) {
                System.out.println("Grayscale image data loaded successfully.");
                System.out.println("Image dimensions: " + grayscaleImage.length + "x" + grayscaleImage[0].length);

            } else {
                System.out.println("Failed to load grayscale image data.");
            }

        QuadTree tree = new QuadTree(grayscaleImage);

        //tree Depth
        System.out.println("Tree Depth: " + tree.TreeDepth());

        //pixel Depth
        System.out.println("pixel Depth : " + tree.pixelDepth(2,2));

        //compress
         /* // String outputPath = "grayscale_CompressImage312.png"; // male abol
          String outputPath = "C:/Users/User/Desktop/Dataset/grayscale_CompressImage312.png"; //mal agha ehsan
          GrayscaleImage.generateImage(tree.compress(256), outputPath);*/


        int x1 = 0 ;
        int y1 = 0 ;
        int x2 = 500 ;
        int y2 = 500 ;

        String outPath = "C:/Users/User/Desktop/Dataset/grayscale_image_searchRange.png";
        GrayscaleImage.generateImage(tree.searchSubspacesWithRange(x1,y1,x2,y2), outPath);

        String outputPath1 = "C:/Users/User/Desktop/Dataset/grayscale_maskPhotoshopi.png";
        GrayscaleImage.generateImage(tree.maskPhotoshopInverse(x1,y1,x2,y2),outputPath1);

        String outputPath2 = "C:/Users/User/Desktop/Dataset/grayscale_maksed.png";
        GrayscaleImage.generateImage(tree.mask(x1,y1,x2,y2),outputPath2);

     /*   //mask
        String outputPath1 = "C:/Users/User/Desktop/Dataset/grayscale_maksed.png";
        GrayscaleImage.generateImage(tree.mask(x1,y1,x2,y2),outputPath1);
        // mask inverse
        String outPath = "C:/Users/User/Desktop/Dataset/grayscale_image_searchRange.png";
        GrayscaleImage.generateImage(tree.searchSubspacesWithRange(x1,y1,x2,y2), outPath);
        // Crop
        String outPathCrop = "C:/Users/User/Desktop/Dataset/grayscale_image_crop.png";
        GrayscaleImage.generateImage(tree.crop(x1,y1,x2,y2), outPathCrop);
        tree = new QuadTree(tree.crop(x1,y1,x2,y2));


           String outputPath = "C:/Users/User/Desktop/Dataset/grayscale_image312.png";
           GrayscaleImage.generateImage(tree.toInt(), outputPath);*/
    }
}
