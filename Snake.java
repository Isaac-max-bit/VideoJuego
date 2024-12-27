import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Snake extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    // Snake
    ArrayList<Tile> snakeBody;
    Tile SnakeHead;

    // Food
    Tile Food;
    Random random;

    // Game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    Snake(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);

        snakeBody = new ArrayList<Tile>();
        SnakeHead = new Tile(5, 5);
        snakeBody.add(SnakeHead);

        Food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();

        // Add KeyListener to the JPanel to listen for key presses
        addKeyListener(this);
        setFocusable(true);  // Important: This makes the JPanel focusable to receive key events
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Drawing the grid
        for (int i = 0; i < boardWidth / tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }

        // Drawing the food
        g.setColor(Color.red);
        g.fillRect(Food.x * tileSize, Food.y * tileSize, tileSize, tileSize);

        // Drawing the snake
        g.setColor(Color.green);
        for (Tile segment : snakeBody) {
            g.fillRect(segment.x * tileSize, segment.y * tileSize, tileSize, tileSize);
        }
    }

    public void placeFood() {
        Food.x = random.nextInt(boardWidth / tileSize);
        Food.y = random.nextInt(boardHeight / tileSize);
    }

    public boolean collisions(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        // Check for collision with the body before moving
        for (int i = 1; i < snakeBody.size(); i++) {
            Tile snakepart = snakeBody.get(i);
            if (collisions(SnakeHead, snakepart)) {
                gameLoop.stop();
                JOptionPane.showMessageDialog(this, "Game Over!");
                return;  // Stop the game if collision occurs with itself
            }
        }

        // Eat food if collision occurs
        if (collisions(SnakeHead, Food)) {
            snakeBody.add(new Tile(Food.x, Food.y));
            placeFood();
        }

        // Snake Body movement
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakepart = snakeBody.get(i);
            if (i == 0) {
                snakepart.x = SnakeHead.x;
                snakepart.y = SnakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakepart.x = prevSnakePart.x;
                snakepart.y = prevSnakePart.y;
            }
        }

        // Create a new head based on the velocity
        Tile newHead = new Tile(SnakeHead.x + velocityX, SnakeHead.y + velocityY);

        // Check for collision with walls
        if (newHead.x < 0 || newHead.x >= boardWidth / tileSize || newHead.y < 0 || newHead.y >= boardHeight / tileSize) {
            gameLoop.stop();
            JOptionPane.showMessageDialog(this, "Game Over! You hit the wall.");
            return;
        }

        // Add the new head to the front of the snake
        snakeBody.add(0, newHead);
        SnakeHead = newHead;

        // Remove the last segment of the snake (tail) if no food is eaten
        if (!(SnakeHead.x == Food.x && SnakeHead.y == Food.y)) {
            snakeBody.remove(snakeBody.size() - 1);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // We are not using this method for movement, so it's left empty
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Respond to arrow keys
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Empty method as we don't need it for this simple snake game
    }
}
