import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Game extends JPanel implements ActionListener, KeyListener {

  private class Tile {
    int x;
    int y;

    Tile(int x, int y) {
      this.x = x;
      this.y = y;

    }

    public String toString() {
      return "{ X: " + x + ", Y: " + y + " }";
    }
  }

  int boardWidth;
  int boardHeight;
  int tileSize = 25;

  Tile snakeHead;
  Tile food;

  // Game Logic
  Timer gameLoop;
  int velocityX;
  int velocityY;

  ArrayList<Tile> snakeBody;

  Random random;

  boolean gameOver = false;

  public Game(int boardWidth, int boardHeight) {

    this.boardWidth = boardWidth;
    this.boardHeight = boardHeight;

    snakeHead = new Tile(5, 5);
    snakeBody = new ArrayList<Tile>();

    addKeyListener(this);
    setFocusable(true);

    random = new Random();

    deployFood();
    setPreferredSize(new Dimension(this.boardHeight, this.boardWidth));
    setBackground(Color.ORANGE);

    velocityX = 0;
    velocityY = 0;

    gameLoop = new Timer(100, this);
    gameLoop.start();

  }

  public void draw(Graphics g) {

    g.setColor(Color.RED);
    g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
    g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);
    g.setColor(Color.BLUE);
    g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
    g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

    for (int i = 0; i < snakeBody.size(); i++) {
      Tile snakePart = snakeBody.get(i);
      g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
    }

    g.setFont(new Font("Arial", Font.PLAIN, 16));
    if (gameOver) {
      g.setColor(Color.red);
      g.drawString("GAME OVER! YOUR SCORE IS " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);

    } else {
      g.drawString("YOUR SCORE IS " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
    }

  }

  public boolean collision(Tile t1, Tile t2) {
    return t1.x == t2.x && t1.y == t2.y;
  }

  public void deployFood() {
    food = new Tile(random.nextInt(boardWidth / tileSize), random.nextInt(boardHeight / tileSize));
  }

  public void move() {
    if (collision(snakeHead, food)) {

      snakeBody.add(new Tile(snakeHead.x, snakeHead.y));
      deployFood();

    }

    for (int i = snakeBody.size() - 1; i > 0; i--) {
      Tile currentPart = snakeBody.get(i);
      Tile prevPart = snakeBody.get(i - 1);
      currentPart.x = prevPart.x;
      currentPart.y = prevPart.y;
    }

    if (snakeBody.size() > 0) {
      Tile firstPart = snakeBody.get(0);
      firstPart.x = snakeHead.x;
      firstPart.y = snakeHead.y;
    }

    snakeHead.x += velocityX;
    snakeHead.y += velocityY;

    for (int i = 0; i < snakeBody.size(); i++) {
      Tile snakePart = snakeBody.get(i);
      if (collision(snakeHead, snakePart)) {
        gameOver = true;
      }
    }

    if (snakeHead.x * tileSize < 0 || snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight
        || snakeHead.x * tileSize > boardWidth) {
      gameOver = true;
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    move();
    repaint();
    if (gameOver) {
      gameLoop.stop();
      System.out.println("Your points is " + snakeBody.size());
    }

  }

  public void keyPressed(KeyEvent e) {
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

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void keyTyped(KeyEvent e) {
  }

  public void keyReleased(KeyEvent e) {
  }

}
