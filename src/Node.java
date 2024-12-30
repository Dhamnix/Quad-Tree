public class Node {
    boolean isLeaf;  // آیا برگ است؟
    int[][] data;    // داده‌ها (در صورت برگ بودن)
    Node topLeft, topRight, bottomLeft, bottomRight; // فرزندان

    // گره برگ
    Node(int[][] data) {
        this.isLeaf = true;
        this.data = data;
    }

    // گره داخلی (غیر برگ)
    Node(Node topLeft, Node topRight, Node bottomLeft, Node bottomRight) {
        this.isLeaf = false;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }
}
