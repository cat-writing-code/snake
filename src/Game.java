import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Game extends JPanel implements ActionListener, KeyListener {
    private int width, height, blockSize, score;
    private SnakeBlock snakeHead;
    private ArrayList<SnakeBlock> snake;
    private FoodBlock food;
    private Random ran;
    private Timer timer;
    private boolean gameOver;
    private Set<Integer> pressedKeys;

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
        gameOver = false;
        pressedKeys = new HashSet<>();

        snake = new ArrayList<>();
        snakeHead = new SnakeBlock(6*blockSize, 7*blockSize, blockSize);
        snake.add(snakeHead);

        food = new FoodBlock(0, 0, blockSize);
        changeFoodLocation();

        timer = new Timer(150, this);
        timer.start();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (gameOver == false) {
            drawFood(g);
            drawSnake(g);
            drawGrid(g);
            displayScore(g);
            
            gameOver = checkGameOver();
        } else {
            gameOver(g);
        }
        
    }

    // draw grid
    public void drawGrid(Graphics g) {
        //draw empty space around board
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, blockSize, height);
        g.fillRect(width-blockSize, 0, blockSize, height);
        g.fillRect(0, 0, width, blockSize);
        g.fillRect(0, height-blockSize, width, blockSize);

        g.setColor(new Color(70,70,70));
        for (int i = blockSize; i <= width-blockSize; i += blockSize) {
            g.drawLine(i, blockSize, i, height-blockSize);
        }
        for (int j = blockSize; j <= height-blockSize; j += blockSize) {
            g.drawLine(blockSize, j, width-blockSize, j);
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

    // draw game over
    public void gameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("game over!", (width/2)-80, 90);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("score: " + score, (width/2)-35, 120);

        g.drawString("press space for a new game", (width/2)-130, 320);
    }

    // change food location
    public void changeFoodLocation() {
        int maxX = width - blockSize;
        int maxY = height - blockSize;
        int cols = (maxX - blockSize)/blockSize;
        int rows = (maxY - blockSize)/blockSize;

        int x = ran.nextInt(cols) * blockSize + blockSize;
        int y = ran.nextInt(rows) * blockSize + blockSize;

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
            growSnake();
            return true;
        }
        return false;
    }

    // check if snake head collides with wall
    public boolean snakeWallCollision() {
        return snakeHead.getX() < blockSize || snakeHead.getX() >= width-blockSize || snakeHead.getY() < blockSize || snakeHead.getY() >= height-blockSize;
    }


    // check if snake head collides with self
    public boolean snakeSelfCollision() {
        for (int i = 1; i < snake.size(); i++) {
            if (snakeHead.getX() == snake.get(i).getX() && 
                snakeHead.getY() == snake.get(i).getY()) {
                    return true;
            }
        }
        return false;
    }

    // check if game over
    public boolean checkGameOver() {
        return snakeSelfCollision() || snakeWallCollision();
    }

    // move snake
    public void move() {
        int[] oldX = new int[snake.size()];
        int[] oldXVel = new int[snake.size()];
        int[] oldY = new int[snake.size()];
        int[] oldYVel = new int[snake.size()];

        // store old positions and velocities
        for (int i = 0; i < snake.size(); i++) {
            oldX[i] = snake.get(i).getX();
            oldXVel[i] = snake.get(i).getXVelocity();
            oldY[i] = snake.get(i).getY();
            oldYVel[i] = snake.get(i).getYVelocity();
        }

        // move head
        snakeHead.updateLocation();

        // move the other body segments and update its velocity
        for (int i = 1; i < snake.size(); i++) {
            snake.get(i).setX(oldX[i-1]);
            snake.get(i).setXVel(oldXVel[i-1]);
            snake.get(i).setY(oldY[i-1]);
            snake.get(i).setYVel(oldYVel[i-1]);
        }

        // check if the snake has eaten some food
        checkSnakeFoodCollision();
    }

    // grow snake
    public void growSnake() {
        int posCurrTail = snake.size()-1;
        SnakeBlock newTail;
        SnakeBlock currTail = snake.get(posCurrTail);
        int y = currTail.getY();
        int x = currTail.getX();

        // based off of current direction of the tail, add new tail
        if (currTail.getXVelocity() == 0) { // either going up or down
            if (currTail.getYVelocity() < 0) { // going up
                y += blockSize;
            } else { // going down
                y += -1*blockSize;
            }
            newTail = new SnakeBlock(x, y, blockSize);
            newTail.setYVel(currTail.getYVelocity());
        } else { // either going left or right
            if (currTail.getXVelocity() > 0) { // going right
                x += -1*blockSize;
            } else { // going left
                x += blockSize;
            }
            newTail = new SnakeBlock(x, y, blockSize);
            newTail.setXVel(currTail.getXVelocity());
        }
        snake.add(newTail);
    }

    // reset game
    public void resetGame() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        score = 0;
        gameOver = false;
        snake.clear();
        ran = new Random();
        gameOver = false;
        pressedKeys = new HashSet<>();

        snake = new ArrayList<>();
        snakeHead = new SnakeBlock(6*blockSize, 7*blockSize, blockSize);
        snake.add(snakeHead);

        food = new FoodBlock(0, 0, blockSize);
        changeFoodLocation();

        timer = new Timer(150, this);
        timer.start();
        repaint();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }


    //key movements
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        //ignore repeat key
        if (pressedKeys.contains(key)) {
            return;
        }

        pressedKeys.add(key);

        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_UP && 
                !(snakeHead.getYVelocity() == blockSize && snake.size() > 1)) {
                snakeHead.setXVel(0);
                snakeHead.setYVel(-1*blockSize);
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN && 
                !(snakeHead.getYVelocity() == -1*blockSize && snake.size() > 1)) {
                snakeHead.setXVel(0);
                snakeHead.setYVel(blockSize);
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT && 
                !(snakeHead.getXVelocity() == blockSize && snake.size() > 1)) {
                snakeHead.setXVel(-1*blockSize);
                snakeHead.setYVel(0);
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT&& 
                !(snakeHead.getXVelocity() == -1*blockSize && snake.size() > 1)) {
                snakeHead.setXVel(blockSize);
                snakeHead.setYVel(0);
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                resetGame();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        // this is to help when someone is holding down a key
        pressedKeys.remove(e.getKeyCode());
    }
}