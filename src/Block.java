import java.awt.Color;

public class Block {
    protected int x, y, size;
    protected Color color;

    public Block(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        color = Color.WHITE; // default color
    }

    /*
     * getter fucntions
     */

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /*
     * setter functions
     */

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
