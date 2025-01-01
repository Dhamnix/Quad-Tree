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
    public int[][] searchSubspacesWithRange(int x1, int y1, int x2, int y2) {
        // Create a new result image, initially filled with white (255)
        int[][] resultImage = new int[image.length][image[0].length];
        for (int i = 0; i < resultImage.length; i++) {
            for (int j = 0; j < resultImage[0].length; j++) {
                resultImage[i][j] = 255; // white
            }
        }

        // Iterate over the borders of the rectangle (first and last rows and columns)
        for (int i = y1; i < y2 && i < image.length; i++) {
            for (int j = x1; j < x2 && j < image[0].length; j++) {
                // Check if the pixel is in a leaf node
                if (isLeafNodeAtPosition(j, i)) {
                    // Fill the corresponding leaf node into the result image
                    fillLeafNodeInImage(resultImage, j, i);
                }
            }
        }

        return resultImage;
    }
    
    // Masks subspaces that overlap with a rectangle
    public int[][] mask(int x1, int y1, int x2, int y2) {
        // Create a new result image, initially filled with white (255)
        int[][] resultImage = new int[image.length][image[0].length];
        for (int i = 0; i < resultImage.length; i++) {
            for (int j = 0; j < resultImage[0].length; j++) {
                resultImage[i][j] = 00; // black af
            }
        }

        // Iterate over the borders of the rectangle (first and last rows and columns)
        for (int i = y1; i < y2 && i < image.length; i++) {
            for (int j = x1; j < x2 && j < image[0].length; j++) {
                // Check if the pixel is in a leaf node
                if (isLeafNodeAtPosition(j, i)) {
                    // Fill the corresponding leaf node into the result image
                    fillLeafNodeInImage(resultImage, j, i);
                }
            }
        }

        return resultImage;
    }

    // Check if a given pixel is part of a leaf node
    private boolean isLeafNodeAtPosition(int x, int y) {
        return findLeafNodeAtPosition(root, x, y, 0, 0, image[0].length, image.length);
    }

    // Recursive function to find the leaf node at a specific position
    private boolean findLeafNodeAtPosition(Node node, int x, int y, int xStart, int yStart, int width, int height) {
        if (node == null) return false;
        if (node.isLeaf) {
            // If it's a leaf node, return true
            return true;
        }

        // Calculate mid-points to divide the area into subspaces
        int midX = xStart + width / 2;
        int midY = yStart + height / 2;

        // Check which quadrant the pixel (x, y) belongs to
        if (x < midX && y < midY) {
            return findLeafNodeAtPosition(node.topLeft, x, y, xStart, yStart, width / 2, height / 2);
        } else if (x >= midX && y < midY) {
            return findLeafNodeAtPosition(node.topRight, x, y, midX, yStart, width / 2, height / 2);
        } else if (x < midX && y >= midY) {
            return findLeafNodeAtPosition(node.bottomLeft, x, y, xStart, midY, width / 2, height / 2);
        } else {
            return findLeafNodeAtPosition(node.bottomRight, x, y, midX, midY, width / 2, height / 2);
        }
    }

    // Function to fill the corresponding leaf node's area in the result image
    private void fillLeafNodeInImage(int[][] resultImage, int x, int y) {
        fillLeafNodeInImageRecursive(root, x, y, 0, 0, image[0].length, image.length, resultImage);
    }

    // Recursive function to fill the leaf node in the result image
    private void fillLeafNodeInImageRecursive(Node node, int x, int y, int xStart, int yStart, int width, int height, int[][] resultImage) {
        if (node == null) return;

        if (node.isLeaf) {
            // If it's a leaf node, fill the entire area corresponding to this leaf node
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (xStart + j < resultImage[0].length && yStart + i < resultImage.length) {
                        resultImage[yStart + i][xStart + j] = node.data[i][j];
                    }
                }
            }
        } else {
            // Calculate mid-points
            int midX = xStart + width / 2;
            int midY = yStart + height / 2;

            // Check which quadrant the pixel belongs to and recursively fill
            if (x < midX && y < midY) {
                fillLeafNodeInImageRecursive(node.topLeft, x, y, xStart, yStart, width / 2, height / 2, resultImage);
            } else if (x >= midX && y < midY) {
                fillLeafNodeInImageRecursive(node.topRight, x, y, midX, yStart, width / 2, height / 2, resultImage);
            } else if (x < midX && y >= midY) {
                fillLeafNodeInImageRecursive(node.bottomLeft, x, y, xStart, midY, width / 2, height / 2, resultImage);
            } else {
                fillLeafNodeInImageRecursive(node.bottomRight, x, y, midX, midY, width / 2, height / 2, resultImage);
            }
        }
    }

    // Compresses the image into smaller size
    public int[][] compress(int newSize) {
        int [][] data = toInt();
        int oldWidth = data[0].length;
        int oldHeight = data.length;

        // محاسبه اندازه زیرمجموعه‌ها برای هر بخش
        int blockSizeWidth = oldWidth / newSize;
        int blockSizeHeight = oldHeight / newSize;

        // ایجاد تصویر جدید با سایز جدید
        int[][] compressedImage = new int[newSize][newSize];

        // پیمایش هر بلوک و میانگین‌گیری
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                int sum = 0;
                int pixelCount = 0;

                // محاسبه میانگین پیکسل‌ها در بلوک (i, j)
                for (int x = i * blockSizeHeight; x < (i + 1) * blockSizeHeight; x++) {
                    for (int y = j * blockSizeWidth; y < (j + 1) * blockSizeWidth; y++) {
                        // اطمینان از اینکه به محدوده معتبر پیکسل دسترسی داریم
                        if (x < oldHeight && y < oldWidth) {
                            sum += data[x][y];
                            pixelCount++;
                        }
                    }
                }

                // قرار دادن میانگین در تصویر جدید
                compressedImage[i][j] = sum / pixelCount;
            }
        }

        return compressedImage;
    }

    // Masks Photoshop
    public int[][] maskPhotoshop(int x1, int y1, int x2, int y2) {
        // ایجاد یک آرایه جدید با ابعاد تصویر اصلی
        int[][] resultImage = new int[image.length][image[0].length];

        // پر کردن کل تصویر با رنگ مشکی
        for (int i = 0; i < resultImage.length; i++) {
            for (int j = 0; j < resultImage[0].length; j++) {
                resultImage[i][j] = 0; // مقدار مشکی
            }
        }

        // پیمایش ناحیه مستطیل و کپی کردن مقادیر از تصویر اصلی
        for (int i = y1; i < y2 && i < image.length; i++) {
            for (int j = x1; j < x2 && j < image[0].length; j++) {
                resultImage[i][j] = image[i][j]; // کپی مقدار اصلی
            }
        }

        return resultImage; // بازگشت تصویر نهایی
    }
    public int[][] maskPhotoshopInverse(int x1, int y1, int x2, int y2) {
        // ایجاد یک آرایه جدید با ابعاد تصویر اصلی
        int[][] resultImage = new int[image.length][image[0].length];

        // پر کردن کل تصویر با رنگ سفید
        for (int i = 0; i < resultImage.length; i++) {
            for (int j = 0; j < resultImage[0].length; j++) {
                resultImage[i][j] = 255; // مقدار سفید
            }
        }

        // پیمایش ناحیه مستطیل و کپی کردن مقادیر از تصویر اصلی
        for (int i = y1; i < y2 && i < image.length; i++) {
            for (int j = x1; j < x2 && j < image[0].length; j++) {
                resultImage[i][j] = image[i][j]; // کپی مقدار اصلی
            }
        }
        return resultImage;
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
    //Crop
    public int[][] crop(int xStart, int yStart, int xEnd, int yEnd) {
        // اطمینان از اینکه مختصات ورودی معتبر باشند
        if (xStart < 0 || yStart < 0 || xEnd > image[0].length || yEnd > image.length || xStart >= xEnd || yStart >= yEnd) {
            throw new IllegalArgumentException("Invalid crop coordinates");
        }

        // ایجاد آرایه جدید برای ذخیره بخش برش داده شده
        int croppedWidth = xEnd - xStart;
        int croppedHeight = yEnd - yStart;
        int[][] croppedImage = new int[croppedHeight][croppedWidth];

        // کپی کردن مقادیر از تصویر اصلی به آرایه جدید
        for (int i = 0; i < croppedHeight; i++) {
            for (int j = 0; j < croppedWidth; j++) {
                croppedImage[i][j] = image[yStart + i][xStart + j];
            }
        }

        // بازگشت تصویر کراپ شده
        return croppedImage;
    }

}
