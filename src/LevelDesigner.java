import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class LevelDesigner extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
    int ballX = 400; // Initial ball position (x)
    int ballY = 300; // Initial ball position (y)
    int ballRadius = 10; // Ball radius
    double ballSpeedX = 0; // Ball speed in x direction
    double ballSpeedY = 0; // Ball speed in y direction
    int currentMouseX, currentMouseY, dragStartX, dragStartY;
    int holeX = 20; 
    int holeY = 20;
    double friction = 0.96;
    private static final int maxSpeed = 45;//Maximum velocity of the ball
    int meterHeight = 300;     // Max height of the velocity meter
    int velocity;              // To store the calculated velocity
    int scaledVelocity;     // To store the scaled velocity valu
    private JPanel statsPanel;
    
    
    

    ArrayList<Rectangle> obstacles; // List of rectangular obstacles
    private Image obstacleTexture;

    Timer animationTimer;

    int levelWidth = 800;
    int levelHeight = 600;

    int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    int statsBarWidth = 300;
    int topBarHeight = 50;

    public LevelDesigner() {
        addMouseListener(this);
        addMouseMotionListener(this);
        animationTimer = new Timer(20, this); // 10ms delay for smooth animation
        obstacles = new ArrayList<>();
        // Generate the frame borders
        generateObstacles();
        //generateRandomObstacles();
        //generateObstacles(obstacles, obstacleTextures); // Add random obstacles to the frame
        try{
            obstacleTexture = ImageIO.read(getClass().getResource("/texture3.jpg"));

        } catch (Exception e) {

        }
        //g.drawImage(obstacleTexture, obstacle.x, obstacle.y, obstacle.width, obstacle.height, null);
        
    }

    // Generate the borders of the frame
    private void generateObstacles() {
        // Left border
        obstacles.add(new Rectangle(0, 0, 10, getHeight()));
        // Right border
        obstacles.add(new Rectangle(getWidth() - 10, 0, 10, getHeight()));
        // Top border
        obstacles.add(new Rectangle(0, 0, getWidth(), 10));
        // Bottom border
        obstacles.add(new Rectangle(0, getHeight() - 10, getWidth(), 10));
        //Level 1
        /*  obstacles.add(new Rectangle(0, 450,300, 40));
        obstacles.add(new Rectangle(60,350, 300, 40));
        obstacles.add(new Rectangle(360,350, 40, 240));
        obstacles.add(new Rectangle(0, 250, 500, 40)); 
        obstacles.add(new Rectangle(500,150, 40, 400));
        obstacles.add(new Rectangle(540,510, 150, 40));
        obstacles.add(new Rectangle(540,350, 150, 40));
        obstacles.add(new Rectangle(640,430, 150, 40));
        obstacles.add(new Rectangle(640,250, 150, 40));
        obstacles.add(new Rectangle(500,0, 40, 100));
        obstacles.add(new Rectangle(200,50, 40, 150));
        obstacles.add(new Rectangle(350,60, 150, 40));
        obstacles.add(new Rectangle(350,150, 150, 40)); */

        obstacles.add(new Rectangle(200,560,500, 40));
        obstacles.add(new Rectangle(240,520,300 ,40));
        obstacles.add(new Rectangle(0,420, 700, 40));
        obstacles.add(new Rectangle(240,460, 300, 40));
        obstacles.add(new Rectangle(240,460, 300, 40));
        obstacles.add(new Rectangle(600,320, 100, 100));
        obstacles.add(new Rectangle(540,220, 260, 40));
        obstacles.add(new Rectangle(500,220, 40, 100));
        obstacles.add(new Rectangle(420,180, 40, 240));
        obstacles.add(new Rectangle(420,140, 300, 40));
        obstacles.add(new Rectangle(420,0, 40, 70));
        obstacles.add(new Rectangle(500,70, 40, 70));
        obstacles.add(new Rectangle(580,0, 40, 70));
        obstacles.add(new Rectangle(680,70, 40, 70));
        obstacles.add(new Rectangle(150,0, 40, 200));
        obstacles.add(new Rectangle(40,160, 140, 40));
        obstacles.add(new Rectangle(0,260, 240, 40));
        obstacles.add(new Rectangle(240,40, 40, 260));
        obstacles.add(new Rectangle(330,0, 40, 340));
        obstacles.add(new Rectangle(90,340, 280, 40));
        

        
       
       
        
    }

    // Generate random obstacles
   /*  public void generateRandomObstacles() {
        Random rand = new Random();
        for (int i = 0; i < 5; i++) { // Generate 5 random rectangles
            int rectX = rand.nextInt(700); // Random x position
            int rectY = rand.nextInt(500); // Random y position
            int rectWidth = rand.nextInt(50) + 50; // Random width between 50 and 100
            int rectHeight = rand.nextInt(50) + 50; // Random height between 50 and 100
            obstacles.add(new Rectangle(rectX, rectY, rectWidth, rectHeight));
        }
    }*/

    

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
        int count = 1;
        for (Rectangle obstacle : obstacles) {
            g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
            g.drawImage(obstacleTexture, obstacle.x, obstacle.y, obstacle.width, obstacle.height, null);


            count += 1;
        }

        // Draw the borders
        g.setColor(Color.GREEN); // Set color for borders
        for (Rectangle border : obstacles) {
            g.drawRect(border.x, border.y, border.width, border.height); // Draw borders as outlines
        }

        // Draw line showing the drag direction
        g.setColor(Color.BLACK);
        g.drawLine(ballX, ballY, currentMouseX, currentMouseY);

     
        
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

        limitBallSpeed();

        // Update the ball position
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Slow down the ball (friction)
        ballSpeedX *= friction;
        ballSpeedY *= friction;

        double speed = Math.sqrt(ballSpeedX * ballSpeedX + ballSpeedY * ballSpeedY);
    
        // If the overall speed is below a small threshold, stop the ball
        if (speed < 3) {
         ballSpeedX = 0;
         ballSpeedY = 0;
    }
   
    

       

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

    private void limitBallSpeed() {
            
        double speed = Math.sqrt(ballSpeedX * ballSpeedX + ballSpeedY * ballSpeedY);
    
        if (speed > maxSpeed) {
            // Scale down the velocity to keep it within the maximum speed
            double scale = maxSpeed / speed;
            ballSpeedX *= scale;
            ballSpeedY *= scale;
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
        ballSpeedX = (dragStartX - dragEndX) / 2.0;
        ballSpeedY = (dragStartY - dragEndY) / 2.0;
        System.out.println("ball speed x " + ballSpeedX + " ball speed y " + ballSpeedY);
        // Start the animation
        animationTimer.start();
        repaint();
    }

   

    

    @Override
    public void mouseDragged(MouseEvent e) {
        currentMouseX = e.getX();
        currentMouseY = e.getY();
        // Continuously update the velocity based on the drag
         calculateDragVelocity();  // Calculate new velocity
         scaledVelocity();         // Scale the velocity for the meter

         
         
         repaint(); // Draw the drag line
          // Repaint the stats panel to update the velocity meter
          if (statsPanel != null) {
          statsPanel.repaint();
           }
           
        
    }
    
    public double calculateDragVelocity() {
        int estimatedBallSpeedX = (dragStartX - currentMouseX) / 2;
        int estimatedBallSpeedY = (dragStartY - currentMouseY) / 2;
        velocity = (int) Math.sqrt(estimatedBallSpeedX * estimatedBallSpeedX + estimatedBallSpeedY * estimatedBallSpeedY);
        return velocity;  // Store velocity for later use
    }

    // Method to scale velocity for the meter
    public double scaledVelocity() {
        scaledVelocity =(int) ((velocity / (double) maxSpeed) * meterHeight);
        return scaledVelocity;  // Store scaled velocity
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
            BufferedImage background = ImageIO.read(getClass().getResource("/texture4.jpg"));
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
        
        int frameX = (screenWidth + levelWidth) / 2;
        int frameY = (screenHeight - levelHeight) / 2;
    
        JFrame statsBar = new JFrame();
        statsBar.setUndecorated(true);
        statsBar.setLocation(frameX, frameY); // Centers the frame on the screen
        statsBar.setSize(statsBarWidth, levelHeight);

        
        
       

        // Add a JPanel that will draw rectangles and other components
        JPanel statsPanel = new JPanel() {   
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                
                
                
                
                

                
                
                 // Set background color for the panel
                 g.setColor(Color.ORANGE);
                 g.fillRect(0, 0, getWidth(), getHeight());

                  
                 // Set color for the rectangles
                 g.setColor(Color.BLACK);
                 g.fillRect(123, 168, 54, 303);  
                 g.setColor(Color.DARK_GRAY);
                 g.fillRect(125, 170,50,100);
                 g.setColor(Color.gray);
                 g.fillRect(125, 270,50,100);
                 g.setColor(Color.LIGHT_GRAY);
                 g.fillRect(125, 370,50,100);

                 
                 int meterWidth = 50;   // Width of the meter
                 int meterX = 125;      // X position of the meter
                 int meterY = 170;      // Y position of the meter (top)

                 

              

                 
                  
             
                 // Draw the filled portion of the meter based on velocity
                 if (velocity < maxSpeed / 2) {
                    g.setColor(Color.GREEN); // Set color to green if below threshold
                } else {
                    g.setColor(Color.RED); // Set color to red if above threshold
                }
                 g.fillRect(meterX, meterY + (meterHeight - scaledVelocity), meterWidth, scaledVelocity);
                 System.out.println(scaledVelocity);


                 repaint();
             

                   
                
                 

                 
            
               
            };
        };
    
    
        statsBar.add(statsPanel);
    
        
       
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
    
   