import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.*;

public class LevelDesigner extends JPanel implements MouseListener, MouseMotionListener, ActionListener{
    int speed = 100;
    int x = 250;
    int y = 250;
    int radius = 20;

    int currentMouseX;
    int currentMouseY;

    int dragStartX;
    int dragStartY;
    int dragEndX;
    int dragEndY;

    int velocityX = 0;
    int velocityY = 0;

    Timer animationTimer;

    ArrayList<Rectangle> obstacles;

    public LevelDesigner(){
        animationTimer = new Timer(1, this);
        obstacles = new ArrayList<>();
        generateObstacle(300, 400, 100, 200);
        generateObstacle(0, 0, 10, 800);
        generateObstacle(790, 0, 10, 800);
        generateObstacle(0, 790, 800, 10);
        generateObstacle(10, 0, 790, 10);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void generateObstacle(int x, int y, int width, int height) {
        obstacles.add(new Rectangle(x, y, width, height));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw ball and aiming line
        g.setColor(Color.green);
        g.fillOval(x, y, radius, radius);
        g.setColor(Color.red);
        g.drawLine(x + radius / 2, y + radius / 2, currentMouseX, currentMouseY);

        // Draw obstacles
        g.setColor(Color.RED);
        for (Rectangle rect : obstacles) {
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragStartX = e.getX();
        dragStartY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragEndX = e.getX();
        dragEndY = e.getY();

        // Calculate initial velocity based on drag distance
        velocityX = (dragEndX - dragStartX) / 5;
        velocityY = (dragEndY - dragStartY) / 5;

        animationTimer.start();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentMouseX = e.getX();
        currentMouseY = e.getY();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        // Update ball position with velocity
        x += velocityX;
        y += velocityY;

        // Simulate friction by reducing velocity gradually
        velocityX *= 0.98;
        velocityY *= 0.98;

        // Stop the ball when the velocity is very low
        if (Math.abs(velocityX) < 0.1 && Math.abs(velocityY) < 0.1) {
            velocityX = 0;
            velocityY = 0;
            animationTimer.stop();
        }

        // Check for collision and respond accordingly
        for (Rectangle rect : obstacles) {
            if (new Rectangle(x, y, radius, radius).intersects(rect)) {
                performCollisionLogic(rect);
            }
        }

        repaint();
    }

    public void performCollisionLogic(Rectangle rect) {
        // Detect if the ball is hitting the obstacle from different sides
        if (x + radius > rect.x && x < rect.x + rect.width) {
            if (y + radius >= rect.y && velocityY > 0) { // Ball hits from above
                velocityY = -Math.abs(velocityY); // Reverse Y velocity
            } else if (y <= rect.y + rect.height && velocityY < 0) { // Ball hits from below
                velocityY = Math.abs(velocityY); // Reverse Y velocity
            }
        }
        if (y + radius > rect.y && y < rect.y + rect.height) {
            if (x + radius >= rect.x && velocityX > 0) { // Ball hits from the left
                velocityX = -Math.abs(velocityX); // Reverse X velocity
            } else if (x <= rect.x + rect.width && velocityX < 0) { // Ball hits from the right
                velocityX = Math.abs(velocityX); // Reverse X velocity
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
}
