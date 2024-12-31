public class Node {
    public boolean isLeaf;  // آیا برگ است؟
    public int[][] data;    // داده‌ها (در صورت برگ بودن)
    public Node topLeft, topRight, bottomLeft, bottomRight; // فرزندان

    // گره برگ
    Node(int[][] data) {
        this.isLeaf = true;
        this.data = data;
        this.topLeft = this.topRight = this.bottomLeft = this.bottomRight = null;
    }

    // گره داخلی (غیر برگ)
    Node(Node topLeft, Node topRight, Node bottomLeft, Node bottomRight) {
        this.isLeaf = false;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.data = null ;
    }
}
