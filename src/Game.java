import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements ActionListener, KeyListener {
    private int width, height, blockSize, score;
    private SnakeBlock snakeHead;
    private ArrayList<SnakeBlock> snake;
    private FoodBlock food;
    private Random ran;
    private Timer timer;

    public Game(int width, int height) {
        this.width = width;
        this.height = height;
        blockSize = 25;
        score = 0;
        setPreferredSize(new Dimension(this.width, this.height));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        ran = new Random();

        snake = new ArrayList<>();
        snakeHead = new SnakeBlock(6*blockSize, 7*blockSize, blockSize);
        snake.add(snakeHead);

        food = new FoodBlock(0, 0, blockSize);
        changeFoodLocation();

        timer = new Timer(100, this);
        timer.start();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        drawFood(g);

        // draw snake
        g.setColor(snakeHead.getColor());
        g.fillRect(snakeHead.getX(), snakeHead.getY(), blockSize, blockSize);

        drawGrid(g);
        displayScore(g);
    }

    // draw grid
    public void drawGrid(Graphics g) {
        g.setColor(new Color(70,70,70));
        for (int i = 0; i < width; i += blockSize) {
            g.drawLine(i, 0, i, height);
        }
        for (int j = 0; j < height; j += blockSize) {
            g.drawLine(0, j, width, j);
        }

    }

    // draw snake
    public void drawSnake(Graphics g) {
        for (SnakeBlock block : snake) {
            g.setColor(block.getColor());
            g.fillRect(block.getX(), block.getY(), blockSize, blockSize);
        }
    }

    // draw food
    public void drawFood(Graphics g) {
        g.setColor(food.getColor());
        g.fillRect(food.getX(), food.getY(), blockSize, blockSize);
    }

    // display score
    public void displayScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("score: " + score, 10, 20);
    }

    // change food location
    public void changeFoodLocation() {
        int x = ran.nextInt(width / blockSize) * blockSize;
        int y = ran.nextInt(height / blockSize) * blockSize;
        food.setX(x);
        food.setY(y);

        if (checkFoodCollision()) {
            changeFoodLocation();  
        }
    }

    // check if food is on snake when trying to change food location
    public boolean checkFoodCollision() {
        for (SnakeBlock s : snake) {
            if (s.getX() == food.getX() && s.getY() == food.getY()) {
                return true;
            }
        }

        return false;
    }

    // check if snake head is on food
    public boolean checkSnakeFoodCollision() {
        if (snakeHead.getX() == food.getX() && snakeHead.getY() == food.getY()) {
            changeFoodLocation();
            score++;
            return true;
        }
        return false;
    }

    // check if snake head collides with wall
    public boolean snakeWallCollision() {
        return false;
    }


    // check if snake head collides with self
    public boolean snakeSelfCollision() {
        return false;
    }

    // check if game over
    public boolean checkGameOver() {
        if (snakeSelfCollision() || snakeWallCollision()) {
            return true;
        }

        return false;
    }

    // move snake
    public void move() {
        for (SnakeBlock s : snake) {
            s.updateLocation();
        }

        // check if the snake has eaten some food
        checkSnakeFoodCollision();
    }

    // grow snake

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }


    //key movements
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            snakeHead.setXVel(0);
            snakeHead.setYVel(-1*blockSize);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            snakeHead.setXVel(0);
            snakeHead.setYVel(blockSize);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            snakeHead.setXVel(-1*blockSize);
            snakeHead.setYVel(0);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            snakeHead.setXVel(blockSize);
            snakeHead.setYVel(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
