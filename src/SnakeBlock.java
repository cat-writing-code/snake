import java.awt.Color;

public class SnakeBlock extends Block{
    private int xVel, yVel;

    public SnakeBlock(int x, int y, int size) {
        super(x, y, size);
        color = new Color(79, 121, 66);
        xVel = 0;
        yVel = 0;
    }

    // get block x velocity
    public int getXVelocity() {
        return xVel;
    }

    // get block y velocity
    public int getYVelocity() {
        return yVel;
    }

    // set block x velocity
    public void setXVel(int v) {
        xVel = v;
    }

    // set block y velocity
    public void setYVel(int v) {
        yVel = v;
    }

    // update location
    public void updateLocation() {
        setX(x+xVel);
        setY(y+yVel);
    }
}
