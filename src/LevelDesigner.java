import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class LevelDesigner extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
    int ballX = 400; // Initial ball position (x)
    int ballY = 300; // Initial ball position (y)
    int ballRadius = 10; // Ball radius
    int ballSpeedX = 0; // Ball speed in x direction
    int ballSpeedY = 0; // Ball speed in y direction
    int currentMouseX, currentMouseY, dragStartX, dragStartY;
    int holeX = 20; 
    int holeY = 20;


    ArrayList<Rectangle> obstacles; // List of rectangular obstacles
    Timer animationTimer;

    int levelWidth = 800;
    int levelHeight = 600;

    int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    int statsBarWidth = 300;
    int topBarHeight = 50;

    public LevelDesigner() {
        obstacles = new ArrayList<>();
        addMouseListener(this);
        addMouseMotionListener(this);
        animationTimer = new Timer(10, this); // 10ms delay for smooth animation

        // Generate the frame borders
        generateFrameBorders();
        generateRandomObstacles(); // Add random obstacles to the frame
    }

    // Generate the borders of the frame
    private void generateFrameBorders() {
        // Left border
        obstacles.add(new Rectangle(0, 0, 10, getHeight()));
        // Right border
        obstacles.add(new Rectangle(getWidth() - 10, 0, 10, getHeight()));
        // Top border
        obstacles.add(new Rectangle(0, 0, getWidth(), 10));
        // Bottom border
        obstacles.add(new Rectangle(0, getHeight() - 10, getWidth(), 10));
    }

    // Generate random obstacles
    public void generateRandomObstacles() {
        Random rand = new Random();
        for (int i = 0; i < 5; i++) { // Generate 5 random rectangles
            int rectX = rand.nextInt(700); // Random x position
            int rectY = rand.nextInt(500); // Random y position
            int rectWidth = rand.nextInt(50) + 50; // Random width between 50 and 100
            int rectHeight = rand.nextInt(50) + 50; // Random height between 50 and 100
            obstacles.add(new Rectangle(rectX, rectY, rectWidth, rectHeight));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Draw the hole
        g.setColor(Color.black);
        g.fillOval(holeX,holeY,30,30);


        // Draw the ball
        g.setColor(Color.white);
        g.fillOval(ballX - ballRadius, ballY - ballRadius, ballRadius * 2, ballRadius * 2);

        // Draw obstacles
        g.setColor(Color.RED);
        for (Rectangle obstacle : obstacles) {
            g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        // Draw the borders
        g.setColor(Color.GREEN); // Set color for borders
        for (Rectangle border : obstacles) {
            g.drawRect(border.x, border.y, border.width, border.height); // Draw borders as outlines
        }

        // Draw line showing the drag direction
        g.setColor(Color.BLACK);
        g.drawLine(ballX, ballY, currentMouseX, currentMouseY);

        /*
         * g.fillRect(0, 0, 5, 600);
        g.fillRect(795, 0, 5, 600);
        g.fillRect(0, 595, 800, 5);
        g.fillRect(0, 0, 800, 5);
         */
        
    }

    // Handle the physics and collisions
    public void actionPerformed(ActionEvent e) {
        moveBall();
        repaint();
    }

    // Move the ball based on its current speed
    public void moveBall() {
        // Store the original position
        int originalX = ballX;
        int originalY = ballY;

        // Update the ball position
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Slow down the ball (friction)
        ballSpeedX *= 0.98;
        ballSpeedY *= 0.98;

        // Stop the ball if it moves too slowly
        if (Math.abs(ballSpeedX) < 1) ballSpeedX = 0;
        if (Math.abs(ballSpeedY) < 1) ballSpeedY = 0;

        // Check for collisions with the edges of the panel
        if (ballX - ballRadius < 0) {
            ballX = ballRadius; // Reset position
            ballSpeedX *= -1; // Reverse direction
        }
        if (ballX + ballRadius > getWidth()) {
            ballX = getWidth() - ballRadius; // Reset position
            ballSpeedX *= -1; // Reverse direction
        }
        if (ballY - ballRadius < 0) {
            ballY = ballRadius; // Reset position
            ballSpeedY *= -1; // Reverse direction
        }
        if (ballY + ballRadius > getHeight()) {
            ballY = getHeight() - ballRadius; // Reset position
            ballSpeedY *= -1; // Reverse direction
        }

        // Check for collisions with the rectangles
        for (Rectangle obstacle : obstacles) {
            if (new Rectangle(ballX - ballRadius, ballY - ballRadius, ballRadius * 2, ballRadius * 2).intersects(obstacle)) {
                // Resolve the collision based on the original position
                resolveCollision(originalX, originalY, obstacle);
            }
        }
    }

    // Method to resolve collision
    private void resolveCollision(int originalX, int originalY, Rectangle obstacle) {
        // Determine which side the ball hit the obstacle
        // Calculate the overlap on both axes
        int overlapLeft = (originalX + ballRadius) - obstacle.x; // Right side of the ball to the left side of the obstacle
        int overlapRight = (obstacle.x + obstacle.width) - (originalX - ballRadius); // Left side of the ball to the right side of the obstacle
        int overlapTop = (originalY + ballRadius) - obstacle.y; // Bottom side of the ball to the top side of the obstacle
        int overlapBottom = (obstacle.y + obstacle.height) - (originalY - ballRadius); // Top side of the ball to the bottom side of the obstacle

        // Find the smallest overlap to determine the direction of the collision
        int minOverlap = Math.min(Math.min(overlapLeft, overlapRight), Math.min(overlapTop, overlapBottom));

        if (minOverlap == overlapLeft) {
            ballX = obstacle.x - ballRadius; // Position the ball to the left of the obstacle
            ballSpeedX *= -1; // Reverse X velocity
        } else if (minOverlap == overlapRight) {
            ballX = obstacle.x + obstacle.width + ballRadius; // Position the ball to the right of the obstacle
            ballSpeedX *= -1; // Reverse X velocity
        } else if (minOverlap == overlapTop) {
            ballY = obstacle.y - ballRadius; // Position the ball above the obstacle
            ballSpeedY *= -1; // Reverse Y velocity
        } else if (minOverlap == overlapBottom) {
            ballY = obstacle.y + obstacle.height + ballRadius; // Position the ball below the obstacle
            ballSpeedY *= -1; // Reverse Y velocity
        }
    }


    // Handle mouse press (start of the shot)
    @Override
    public void mousePressed(MouseEvent e) {
        dragStartX = e.getX();
        dragStartY = e.getY();
    }

    // Handle mouse release (end of the shot)
    @Override
    public void mouseReleased(MouseEvent e) {
        int dragEndX = e.getX();
        int dragEndY = e.getY();

        // Calculate the speed of the ball based on the drag distance
        ballSpeedX = (dragStartX - dragEndX) / 2;
        ballSpeedY = (dragStartY - dragEndY) / 2;

        // Start the animation
        animationTimer.start();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentMouseX = e.getX();
        currentMouseY = e.getY();
        repaint(); // Draw the drag line
    }

    // Unused but required by the interface
    public void mouseMoved(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void addMainGameFrame() {
        // constants for window positioning
        int frameX = (screenWidth-levelWidth)/2;
        int frameY = (screenHeight-levelHeight)/2;

        // the main game frame
        JFrame mainGameFrame = new JFrame("Mini Golf Game");
        mainGameFrame.setUndecorated(true);
        mainGameFrame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.RED, 5));
        mainGameFrame.setLocation(frameX, frameY); // Centers the frame on the screen

        // layeredPane for imaging and texturing on game panel
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(levelWidth, levelHeight));

        // adding the gamepanel to the game mainGameFrame
        LevelDesigner gamePanel = new LevelDesigner();
        mainGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGameFrame.setSize(levelWidth, levelHeight);
        mainGameFrame.add(gamePanel);
        mainGameFrame.setVisible(true);

        gamePanel.setBounds(0, 0, levelWidth, levelHeight);
        gamePanel.setOpaque(false);  // Make LevelDesigner transparent to see the background

        try {
            // Load the background image
            BufferedImage background = ImageIO.read(new File("C:\\Users\\marti\\Desktop\\minigolf-project\\src\\texture4.jpg"));
            ImagePanel ip = new ImagePanel(background);
            ip.setBounds(0, 0, 800, 800);
            
            // Add background to the DEFAULT layer
            layeredPane.add(ip, JLayeredPane.DEFAULT_LAYER);
            
            // Add the LevelDesigner game to the PALETTE layer (above background)
            layeredPane.add(gamePanel, JLayeredPane.PALETTE_LAYER);
            
        } catch (Exception e) {
            System.out.println("No image found");
        }

        mainGameFrame.add(layeredPane);
        mainGameFrame.setVisible(true);
    }

    public void addTopBar(String levelName) {
        int frameX = (screenWidth - levelWidth)/2;
        int frameY = (screenHeight - levelHeight)/2 - topBarHeight;

        JFrame topBar = new JFrame();
        topBar.setUndecorated(true);
        //topBar.getRootPane().setBorder(BorderFactory.createLineBorder(Color.RED, 5));
        topBar.setLocation(frameX,frameY); // Centers the frame on the screen
        topBar.setSize(levelWidth + statsBarWidth, topBarHeight);

        JLabel label = new JLabel(levelName);
        label.setLocation(10,10);

        topBar.add(label);
        topBar.setVisible(true);

    }

    public void addStatsBar() {
        int frameX = (screenWidth+levelWidth)/2;
        int frameY = (screenHeight-levelHeight)/2;

        JFrame statsBar = new JFrame();
        statsBar.setUndecorated(true);
        //topBar.getRootPane().setBorder(BorderFactory.createLineBorder(Color.RED, 5));
        statsBar.setLocation(frameX,frameY); // Centers the frame on the screen
        statsBar.setSize(statsBarWidth, levelHeight);

        JLabel label = new JLabel("Your Stats");
        label.setLocation(10,10);
        
        statsBar.add(label);
        statsBar.setVisible(true);
    }

    public void initialiseLevel(String levelName) {
        addMainGameFrame();
        addTopBar(levelName);
        addStatsBar();
    }

    // ImagePanel class to draw the background image
    static class ImagePanel extends JPanel {
        private BufferedImage image;

        public ImagePanel(BufferedImage img) {
            this.image = img;
            setLayout(null);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    
    }
}
