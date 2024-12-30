public class QuadTree {
    // Constructor,Bulids the QuadTree recursibley
    public QuadTree(int [][] image){}

    // Returns the Depth of the tree
    public int TreeDepth(){return 1 ;}

    // Returns the Depth of the pixel in the QuadTree
    public int pixelDepth(int px , int py){return 0 ;}

    // Returns the subspaces that overlap with a rectangle
    private void searchSubspaceWithRange(int x1 , int y1 , int x2 , int y2 ){}

    // Compresses the image into smaller size
    public int[][] compress (int newSize){}

    // Masks subspaces that overlap with a rectangle
    public int[][] mask (int x1 , int y1 , int x2 , int y2 ){}

}
