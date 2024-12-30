public class QuadTree {

    private Node root ;


    // Constructor,Bulids the QuadTree recursibley
    public QuadTree(int [][] image){
        root = buildTree(image, 0, 0, image.length, image[0].length);
    }
    private Node buildTree(int[][] image, int xStart, int yStart, int width, int height) {
        // اگر ابعاد ناحیه 1x1 باشد، برگ ایجاد می‌کنیم
        if (width == 1 && height == 1) {
            int[][] data = new int[1][1];
            data[0][0] = image[yStart][xStart];
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
        return findPixelDepth(root, px, py, 0);
    }
    private int findPixelDepth(Node node, int px, int py, int depth) {
        if (node.isLeaf) return depth;

        // بر اساس مکان پیکسل، به فرزند مناسب می‌رویم
        int midX = node.topLeft.data[0].length / 2;
        int midY = node.topLeft.data.length / 2;

        if (px < midX && py < midY) return findPixelDepth(node.topLeft, px, py, depth + 1);
        if (px >= midX && py < midY) return findPixelDepth(node.topRight, px, py, depth + 1);
        if (px < midX && py >= midY) return findPixelDepth(node.bottomLeft, px, py, depth + 1);
        return findPixelDepth(node.bottomRight, px, py, depth + 1);
    }

    // Returns the subspaces that overlap with a rectangle
    private void searchSubspaceWithRange(int x1 , int y1 , int x2 , int y2 ){
        searchRange(root, x1, y1, x2, y2);
    }
    private void searchRange(Node node, int x1, int y1, int x2, int y2) {
        if (node == null) return;

        // چک کردن اینکه آیا این گره با مستطیل هم‌پوشانی دارد
        if (x2 < 0 || x1 > node.topLeft.data[0].length || y2 < 0 || y1 > node.topLeft.data.length) {
            return; // خارج از محدوده
        }

        if (node.isLeaf) {
            // داده‌ها در برگ را بررسی می‌کنیم
            return;
        }

        // بررسی بازگشتی برای چهار زیربخش
        searchRange(node.topLeft, x1, y1, x2, y2);
        searchRange(node.topRight, x1, y1, x2, y2);
        searchRange(node.bottomLeft, x1, y1, x2, y2);
        searchRange(node.bottomRight, x1, y1, x2, y2);
    }

    // Compresses the image into smaller size
    public int[][] compress (int newSize){
        return null;
    }

    // Masks subspaces that overlap with a rectangle
    public int[][] mask (int x1 , int y1 , int x2 , int y2 ){
        return null;
    }

}
