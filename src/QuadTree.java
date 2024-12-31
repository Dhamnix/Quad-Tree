public class QuadTree {

    private Node root ;
    private int[][] image;

    // Constructor,Bulids the QuadTree recursibley
    public QuadTree(int[][] image) {
        this.image = image;
        root = buildTree(image, 0, 0, image.length, image[0].length);
    }

    private Node buildTree(int[][] image, int xStart, int yStart, int width, int height) {
        // اگر ابعاد ناحیه ۱x۱ باشد، برگ ایجاد می‌کنیم
        if (width == 1 && height == 1) {
            int[][] data = new int[1][1];
            data[0][0] = image[yStart][xStart];
            return new Node(data);
        }

        // بررسی یکنواختی رنگ در زیرفضا
        if (isUniformColor(image, xStart, yStart, width, height)) {
            int[][] data = new int[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    data[i][j] = image[yStart + i][xStart + j];
                }
            }
            return new Node(data);
        }

        // تقسیم فضا به چهار بخش
        int midX = xStart + width / 2;
        int midY = yStart + height / 2;

        Node topLeft = buildTree(image, xStart, yStart, width / 2, height / 2);
        Node topRight = buildTree(image, midX, yStart, width / 2, height / 2);
        Node bottomLeft = buildTree(image, xStart, midY, width / 2, height / 2);
        Node bottomRight = buildTree(image, midX, midY, width / 2, height / 2);

        return new Node(topLeft, topRight, bottomLeft, bottomRight);
    }

    private boolean isUniformColor(int[][] image, int xStart, int yStart, int width, int height) {
        int color = image[yStart][xStart]; // رنگ اولین پیکسل زیرفضا

        for (int i = yStart; i < yStart + height; i++) {
            for (int j = xStart; j < xStart + width; j++) {
                if (image[i][j] != color) {
                    return false; // اگر هر پیکسلی متفاوت باشد، ناحیه یکنواخت نیست
                }
            }
        }

        return true; // همه پیکسل‌ها یکنواخت هستند
    }

    // Returns the Depth of the tree
    public int TreeDepth() {
        return calculateDepth(root);
    }

    private int calculateDepth(Node node) {
        if (node == null) return 0;
        if (node.isLeaf) return 1;
        return 1 + Math.max(Math.max(calculateDepth(node.topLeft), calculateDepth(node.topRight)),
                Math.max(calculateDepth(node.bottomLeft), calculateDepth(node.bottomRight)));
    }

    // Returns the Depth of the pixel in the QuadTree
    public int pixelDepth(int px, int py) {
        if (root == null) {
            throw new IllegalStateException("QuadTree is not initialized properly.");
        }
        return findPixelDepth(root, px, py, 0, 0, image[0].length, image.length, 0);
    }

    private int findPixelDepth(Node node, int px, int py, int xStart, int yStart, int width, int height, int depth) {
        // بررسی اینکه گره تهی یا برگ باشد
        if (node == null) {
            throw new IllegalArgumentException("Node is null");
        }
        if (node.isLeaf) {
            return depth;
        }

        // محاسبه نقاط میانی
        int midX = xStart + width / 2;
        int midY = yStart + height / 2;

        // بررسی اینکه پیکسل در کدام زیرفضا قرار دارد
        if (px < midX && py < midY && node.topLeft != null) {
            return findPixelDepth(node.topLeft, px, py, xStart, yStart, width / 2, height / 2, depth + 1);
        }
        if (px >= midX && py < midY && node.topRight != null) {
            return findPixelDepth(node.topRight, px, py, midX, yStart, width / 2, height / 2, depth + 1);
        }
        if (px < midX && py >= midY && node.bottomLeft != null) {
            return findPixelDepth(node.bottomLeft, px, py, xStart, midY, width / 2, height / 2, depth + 1);
        }
        if (node.bottomRight != null) {
            return findPixelDepth(node.bottomRight, px, py, midX, midY, width / 2, height / 2, depth + 1);
        }

        // اگر هیچ شرطی برقرار نشود
        throw new IllegalStateException("Pixel coordinates do not match any subtree");
    }

    // Returns the subspaces that overlap with a rectangle
    public void searchSubspaceWithRange(int x1, int y1, int x2, int y2) {
        int width = x2 - x1;
        int height = y2 - y1;

        // Initialize result image with white pixels
        int[][] resultImage = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                resultImage[i][j] = 255;  // white
            }
        }

        // Start searching from the root
        searchRange(root, x1, y1, x2, y2, resultImage);

        // Save the result image
        String outPath = "C:/Users/User/Desktop/Dataset/grayscale_image_searchRange.png";
        GrayscaleImage.generateImage(resultImage, outPath);
    }

    private void searchRange(Node node, int x1, int y1, int x2, int y2, int[][] resultImage) {
        if (node == null) return; // Skip null nodes

        // If the node is a leaf, process its data
        if (node.isLeaf) {
            int nodeWidth = node.data[0].length;
            int nodeHeight = node.data.length;

            // Add data to the result image if it overlaps with the search range
            for (int i = 0; i < nodeHeight; i++) {
                for (int j = 0; j < nodeWidth; j++) {
                    // Check if the pixel is within the search range
                    if (x1 <= j && j < x2 && y1 <= i && i < y2) {
                        resultImage[i - y1][j - x1] = node.data[i][j];
                    }
                }
            }
            return; // Exit once a leaf node is processed
        }

        // If the node is not a leaf, recursively check its children (if they exist)
        // We don't need to access node.data for non-leaf nodes.

        if ((node.topLeft.data == null)||(node.topLeft != null && isOverlap(x1, y1, x2, y2, 0, 0, node.topLeft.data[0].length, node.topLeft.data.length))) {
            searchRange(node.topLeft, x1, y1, x2, y2, resultImage);
        }

        if ((node.topRight.data == null)||(node.topRight != null && isOverlap(x1, y1, x2, y2, 0, 0, node.topRight.data[0].length, node.topRight.data.length))) {
            searchRange(node.topRight, x1, y1, x2, y2, resultImage);
        }

        if ((node.bottomLeft.data == null)||(node.bottomLeft != null && isOverlap(x1, y1, x2, y2, 0, 0, node.bottomLeft.data[0].length, node.bottomLeft.data.length))) {
            searchRange(node.bottomLeft, x1, y1, x2, y2, resultImage);
        }

        if ((node.bottomRight.data == null)||(node.bottomRight != null && isOverlap(x1, y1, x2, y2, 0, 0, node.bottomRight.data[0].length, node.bottomRight.data.length))) {
            searchRange(node.bottomRight, x1, y1, x2, y2, resultImage);
        }
    }

    private boolean isOverlap(int x1, int y1, int x2, int y2, int nodeX, int nodeY, int nodeWidth, int nodeHeight) {
        // Returns true if the search rectangle overlaps with the node's region
        return !(x2 <= nodeX || x1 >= nodeX + nodeWidth || y2 <= nodeY || y1 >= nodeY + nodeHeight);
    }



    // Compresses the image into smaller size
    public int[][] compress (int newSize){
        return null;
    }

    // Masks subspaces that overlap with a rectangle
    public int[][] mask (int x1 , int y1 , int x2 , int y2 ){
        return null;
    }
    // Method to convert the QuadTree to an int[][] array
    public int[][] toInt() {
        int[][] result = new int[image.length][image[0].length]; // create the result image
        fillImageFromQuadTree(root, result, 0, 0, image.length, image[0].length); // call the recursive function
        return result;
    }

    // Recursive function to fill the result image
    private void fillImageFromQuadTree(Node node, int[][] result, int xStart, int yStart, int width, int height) {
        if (node == null) return;

        if (node.isLeaf) {
            // If it's a leaf node, copy the data into the result image
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    result[yStart + i][xStart + j] = node.data[i][j];
                }
            }
        } else {
            // Recursively fill the child nodes if it's not a leaf
            int midX = xStart + width / 2;
            int midY = yStart + height / 2;

            // Fill the top-left sub-tree
            if (node.topLeft != null) {
                fillImageFromQuadTree(node.topLeft, result, xStart, yStart, width / 2, height / 2);
            }
            // Fill the top-right sub-tree
            if (node.topRight != null) {
                fillImageFromQuadTree(node.topRight, result, midX, yStart, width / 2, height / 2);
            }
            // Fill the bottom-left sub-tree
            if (node.bottomLeft != null) {
                fillImageFromQuadTree(node.bottomLeft, result, xStart, midY, width / 2, height / 2);
            }
            // Fill the bottom-right sub-tree
            if (node.bottomRight != null) {
                fillImageFromQuadTree(node.bottomRight, result, midX, midY, width / 2, height / 2);
            }
        }
    }

}
