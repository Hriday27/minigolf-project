import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;


public class LevelDesigner extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
    int ballX = 400; // Initial ball position (x)
    int ballY = 300; // Initial ball position (y)
    int ballRadius = 10; // Ball radius
    double ballSpeedX = 0; // Ball speed in x direction
    double ballSpeedY = 0; // Ball speed in y direction

    int currentMouseX, currentMouseY, dragStartX, dragStartY;

    int holeX = 20; 
    int holeY = 20;
    int holeWidth = 30;
    int holeHeight = 30;
    
    double friction = 0.96;
    private static final int maxSpeed = 45;//Maximum velocity of the ball
    
    ArrayList<Rectangle> powerUps;
    ArrayList<Integer> powerUpType;
    private Image speedUpTexture; 
    private Image speedDownTexture;

    ArrayList<Rectangle> obstacles; // List of rectangular obstacles
    ArrayList<Integer> obstacleType;

    private Image obstacleTexture;
    private Image teleportTexture;
    private Image dynamiteTexture;
    private Image explodedTexture;
    private boolean exploded = false;

    Timer animationTimer;

    int levelWidth = 800;
    int levelHeight = 600;

    int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    int statsBarWidth = 300;
    int topBarHeight = 50;

    int totalTries;
    private JLabel scoreCounter;

    public LevelDesigner() {
        this.setLayout(null);
        addMouseListener(this);
        addMouseMotionListener(this);

        // variable initialisation
        animationTimer = new Timer(1, this); // 10ms delay for smooth animation
        obstacles = new ArrayList<>();
        obstacleType = new ArrayList<>();
        powerUps = new ArrayList<>();
        powerUpType = new ArrayList<>();

        // Generate the frame borders
        generateObstacles();
        //generateRandomObstacles();
        generatePowerUps();
        //generateObstacles(obstacles, obstacleTextures); // Add random obstacles to the frame
        
        // initialise images
        try{
            obstacleTexture = ImageIO.read(getClass().getResource("/texture3.jpg"));
            speedUpTexture = ImageIO.read(getClass().getResource("/Speed Up.png"));
            speedDownTexture = ImageIO.read(getClass().getResource("/Speed Down.png"));
            teleportTexture = ImageIO.read(getClass().getResource("/teleportPanel.png"));
            dynamiteTexture = ImageIO.read(getClass().getResource("/dynamitePanel.png"));
            explodedTexture = ImageIO.read(getClass().getResource("/explosion2.jpg"));

        } catch (Exception e) {
            System.out.println("No image found");
        }

        // constants for window positioning
        int frameX = (screenWidth-levelWidth)/2;
        int frameY = (screenHeight-levelHeight)/2;
        // the main game frame
        JFrame mainGameFrame = new JFrame("Mini Golf Game");
        mainGameFrame.setUndecorated(true);
        mainGameFrame.setLocation(frameX, frameY); // Centers the frame on the screen
        
        JPanel masterPanel = new JPanel();
        masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));
        
        // Create header panel with BorderLayout
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(800, 50)); // Set preferred size for the header panel
        headerPanel.setLayout(new BorderLayout()); // Use BorderLayout to position components
        masterPanel.add(headerPanel, BorderLayout.NORTH); // Add header panel to the north of the master panel
        headerPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        // Add a label to the header panel
        JLabel levelLabel = new JLabel("Level 1");
        levelLabel.setFont(new Font("Monospaced", Font.BOLD, 24)); // Set font for the label
        levelLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerPanel.add(levelLabel, BorderLayout.WEST); // Add label to the west (left) of the header panel

        // Create a styled close button
        JButton closeButton = new JButton(" X ");
        closeButton.setFont(new Font("Monospaced", Font.BOLD, 24)); // Set font for the button
        closeButton.setBackground(Color.RED); // Set background color
        closeButton.setForeground(Color.WHITE); // Set text color
        closeButton.setBorder(BorderFactory.createLineBorder(Color.white)); // Set rounded border
        closeButton.setFocusPainted(false); // Remove focus paint on click
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor to hand on hover

        // Add an ActionListener to the close button
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the application when the button is clicked
                mainGameFrame.dispose();
            }
        });

        // Add close button to the header panel
        headerPanel.add(closeButton, BorderLayout.EAST); // Add button to the east (right)

        // Use a separate panel for the close button to push it to the right
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        buttonPanel.add(closeButton);
        headerPanel.add(buttonPanel);
        masterPanel.add(headerPanel);

        // layeredPane for imaging and texturing on game panel
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(levelWidth+statsBarWidth, levelHeight));

        // Add a MouseListener to handle mouse release events
        mainGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGameFrame.setSize(levelWidth+statsBarWidth, levelHeight + topBarHeight);
        //mainGameFrame.add(this);
        mainGameFrame.setVisible(true);

        this.setBounds(0, 0, levelWidth+statsBarWidth, levelHeight);
        this.setOpaque(false);  // Make LevelDesigner transparent to see the background
        //this.setBorder(BorderFactory.createLineBorder(Color.RED, 2));

        try {
            // Load the background image
            BufferedImage background = ImageIO.read(getClass().getResource("/texture4.jpg"));
            ImagePanel ip = new ImagePanel(background);
            ip.setBounds(0, 0, 800, 800);
            
            // Add background to the DEFAULT layer
            layeredPane.add(ip, JLayeredPane.DEFAULT_LAYER);
            
            // Add the LevelDesigner game to the PALETTE layer (above background)
            layeredPane.add(this, JLayeredPane.PALETTE_LAYER);
            
        } catch (Exception e) {
            System.out.println("No image found");
        }

        // score counter label
        scoreCounter = new JLabel("Your Score is: " + totalTries);
        scoreCounter.setBounds(30 + levelWidth, 10, 300, 30);
        scoreCounter.setFont(new Font("Monospaced", Font.BOLD, 24));
        this.add(scoreCounter);

        // Add a JPanel that will draw rectangles and other components
        masterPanel.add(layeredPane);
        mainGameFrame.setVisible(true);
        mainGameFrame.add(masterPanel);
        mainGameFrame.setVisible(true);
    }

    public static int convertValue(int value, int min1, int max1, int min2, int max2) {
        return min2 + ((value - min1) * (max2 - min2)) / (max1 - min1);
    }

    public void generatePowerUps() {
        powerUps.add(new Rectangle(50,50,40,40));
        powerUpType.add(1);
        powerUps.add(new Rectangle(200,200,40,40));
        powerUpType.add(1);
        powerUps.add(new Rectangle(500,500,40,40));
        powerUpType.add(2);
    }

    // Generate the borders of the frame
    private void generateObstacles() {
        // Left border
        obstacles.add(new Rectangle(0, 0, 10, getHeight()));
        obstacleType.add(1); // Add corresponding type

        // Right border
        obstacles.add(new Rectangle(getWidth() - 10, 0, 10, getHeight()));
        obstacleType.add(1); // Add corresponding type

        // Top border
        obstacles.add(new Rectangle(0, 0, getWidth(), 10));
        obstacleType.add(1); // Add corresponding type

        // Bottom border
        obstacles.add(new Rectangle(0, getHeight() - 10, getWidth(), 10));
        obstacleType.add(1); // Add corresponding type

        // Custom obstacle
        obstacles.add(new Rectangle(50, 50, 100, 100));
        obstacleType.add(2); // Add corresponding type for teleport obstacle

        obstacles.add(new Rectangle(600, 500, 100, 100));
        obstacleType.add(2); // Add corresponding type for teleport obstacle

        obstacles.add(new Rectangle(300,500,100,100));
        obstacleType.add(3); // Add corresponding type for teleport obstacle
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
            obstacleType.add(1);
        }
    }

    

    @Override
    public void paintComponent(Graphics g) {
        if(!exploded) {
            super.paintComponent(g);

            //Draw the hole
            g.setColor(Color.black);
            g.fillOval(holeX,holeY,holeHeight, holeWidth);

            // Draw the ball
            g.setColor(Color.white);
            g.fillOval(ballX - ballRadius, ballY - ballRadius, ballRadius * 2, ballRadius * 2);

            // Draw obstacles
            g.setColor(Color.RED);
            int count = 0;
            for (Rectangle obstacle : obstacles) {
                g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
                if (obstacleType.get(count) == 1) {
                    // Draw normal texture
                    g.drawImage(obstacleTexture, obstacle.x, obstacle.y, obstacle.width, obstacle.height, null);
                } else if (obstacleType.get(count) == 2) {
                    // teleport texture
                    g.drawImage(teleportTexture, obstacle.x, obstacle.y, obstacle.width, obstacle.height, null);
                } else if (obstacleType.get(count) == 3) {
                    // teleport texture
                    g.drawImage(dynamiteTexture, obstacle.x, obstacle.y, obstacle.width, obstacle.height, null);
                }
                
                count++;
            }

            // Draw Powerups
            int count2 = 0;
            for (Rectangle powerUp : powerUps) {
                if(powerUpType.get(count2) == 1) {
                    g.drawImage(speedUpTexture, powerUp.x, powerUp.y, powerUp.width, powerUp.height, null);
                } else if (powerUpType.get(count2) == 2) {
                    g.drawImage(speedDownTexture, powerUp.x, powerUp.y, powerUp.width, powerUp.height, null);
                }
                count2++;
            }


            // Draw line showing the drag direction
            g.setColor(Color.BLACK);
            g.drawLine(ballX, ballY, currentMouseX, currentMouseY);

            // draw stats
            // Set color for the rectangles
            g.setColor(Color.BLACK);
            g.fillRect(123+levelWidth, 168, 54, 303);

            int actualVelocity = 2 * (int) Math.pow(Math.pow(dragStartX-currentMouseX,2)+Math.pow(dragStartY-currentMouseY, 2),0.5);
            int mappedVelocity = convertValue(actualVelocity, 0, (int) Math.pow(Math.pow(800,2)+Math.pow(600,2),0.5), 0, 300);

            // Draw the speed meters
            int meterBaseY = 470;  // This represents the bottom position of the meter (change to your desired base height)
            int meterX = 125 + levelWidth;  // X position for all meters
            int meterWidth = 50;  // Width of the meter

            // Initialize cumulative height (how much of the bar has been filled so far)
            int filledHeight = 0;

            // Draw the yellow section (velocity 0-100)
            if (mappedVelocity > 0 && mappedVelocity <= 100) {
                g.setColor(Color.yellow);
                g.fillRect(meterX, meterBaseY - mappedVelocity, meterWidth, mappedVelocity);
                filledHeight = mappedVelocity;
            } else if (mappedVelocity > 100) {
                g.setColor(Color.yellow);
                g.fillRect(meterX, meterBaseY - 100, meterWidth, 100);
                filledHeight = 100;
            }

            // Draw the green section (velocity 100-200)
            if (mappedVelocity > 100 && mappedVelocity <= 200) {
                g.setColor(Color.green);
                int greenHeight = mappedVelocity - 100;
                g.fillRect(meterX, meterBaseY - filledHeight - greenHeight, meterWidth, greenHeight);
                filledHeight += greenHeight;
            } else if (mappedVelocity > 200) {
                g.setColor(Color.green);
                g.fillRect(meterX, meterBaseY - filledHeight - 100, meterWidth, 100);
                filledHeight += 100;
            }

            // Draw the red section (velocity 200-300)
            if (mappedVelocity > 200 && mappedVelocity <= 300) {
                g.setColor(Color.red);
                int redHeight = mappedVelocity - 200;
                g.fillRect(meterX, meterBaseY - filledHeight - redHeight, meterWidth, redHeight);
            } else if (mappedVelocity > 300) {
                g.setColor(Color.red);
                g.fillRect(meterX, meterBaseY - filledHeight - 100, meterWidth, 100);
            }
        } else {
            g.drawImage(explodedTexture, 0,0,levelWidth,levelHeight,null);

            JLabel youLost = new JLabel("Death by TNT");
            youLost.setBounds(60 + levelWidth, 550, 300, 30);
            youLost.setFont(new Font("Monospaced", Font.BOLD, 24));
            youLost.setForeground(Color.red);
            this.add(youLost);

            JButton restart = new JButton("Restart");
            restart.setBounds(50 + levelWidth, 475, 200, 50);
            restart.setFont(new Font("Monospaced", Font.BOLD, 24));
            restart.setBackground(Color.red);
            restart.setForeground(Color.white);
            restart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            restart.setFocusPainted(false); // Remove focus paint on click
             // Change cursor to hand on hover

            // Add an ActionListener to the close button
            restart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Close the application when the button is clicked
                    Level1 l1 = new Level1();
                    l1.main(null);
                }
            });
            this.add(restart);

            animationTimer.stop();
        }
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
        if (ballX + ballRadius > getWidth() - statsBarWidth) {
            ballX = getWidth() - statsBarWidth - ballRadius; // Reset position
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
        int count2 = 0;
        for (Rectangle obstacle : obstacles) {
            if (new Rectangle(ballX - ballRadius, ballY - ballRadius, ballRadius * 2, ballRadius * 2).intersects(obstacle)) {
                if(obstacleType.get(count2) == 2) {
                    // To store the indices where the value equals 2
                    ArrayList<Integer> indices = new ArrayList<>();

                    // Loop through the list and check for occurrences of 2
                    for (int i = 0; i < obstacleType.size(); i++) {
                        if (obstacleType.get(i) == 2) {
                            indices.add(i);
                        }
                    }

                    if(indices.get(0) == count2) {
                        ballSpeedX = 0;
                        ballSpeedY = 0;
                        ballX = obstacles.get(indices.get(1)).x + obstacles.get(indices.get(1)).width/2;
                        ballY = obstacles.get(indices.get(1)).y - 20;
                    } else {
                        ballSpeedX = 0;
                        ballSpeedY = 0;
                        ballX = obstacles.get(indices.get(0)).x + obstacles.get(indices.get(0)).width/2;
                        ballY = obstacles.get(indices.get(0)).y - 20;
                    }
                } else if (obstacleType.get(count2) == 3) {
                    exploded = true;
                    
                    try {
                        // Open an audio input stream.

                        File soundFile = new File("sounds/explosion.wav");
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            
                        // Get a sound clip resource.
                        Clip clip = AudioSystem.getClip();
            
                        // Open the audio clip and load the samples from the audio input stream.
                        clip.open(audioStream);
            
                        // Start playing the sound.
                        clip.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Resolve the collision based on the original position
                    
                    resolveCollision(originalX, originalY, obstacle);
                } 
                
            }
            count2++;
        }

        // Check for collisions with the power ups
        int count = 0;
        for (Rectangle powerUp: powerUps) {
            if(new Rectangle(ballX - ballRadius, ballY - ballRadius, ballRadius * 2, ballRadius * 2).intersects(powerUp) &&
            powerUpType.get(count) == 1) {
                ballSpeedX *= 10;
                ballSpeedY *= 10;
            } else if (new Rectangle(ballX - ballRadius, ballY - ballRadius, ballRadius * 2, ballRadius * 2).intersects(powerUp) &&
            powerUpType.get(count) == 2) {
                ballSpeedX /= 2;
                ballSpeedY /= 2;
            }
            count++;
        }
        
        // check if ball in hole 
        if (new Rectangle(ballX - ballRadius, ballY - ballRadius, ballRadius * 2, ballRadius * 2).
        intersects(new Rectangle(holeX, holeY, holeWidth, holeHeight)) && Math.abs(ballSpeedX) < 20 && 
        Math.abs(ballSpeedY) < 20) {
            System.out.println("Ball in the hole");
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
        totalTries += 1;
        int dragEndX = e.getX();
        int dragEndY = e.getY();

        // Calculate the speed of the ball based on the drag distance
        ballSpeedX = (dragStartX - dragEndX) / 2.0;
        ballSpeedY = (dragStartY - dragEndY) / 2.0;
        System.out.println("ball speed x " + ballSpeedX + " ball speed y " + ballSpeedY);
        // Start the animation
        animationTimer.start();
        repaint();
        scoreCounter.setText("Your Score is: " + totalTries);
    }

   

    

    @Override
    public void mouseDragged(MouseEvent e) {
        currentMouseX = e.getX();
        currentMouseY = e.getY();
        repaint(); // Draw the drag line
        
    }

    double calculateDragVelocity() {
        int estimatedBallSpeedX =  (dragStartX - currentMouseX) / 2;
        int estimatedBallSpeedY = (dragStartY - currentMouseY)  / 2;;
        return Math.sqrt(estimatedBallSpeedX * estimatedBallSpeedX + estimatedBallSpeedY * estimatedBallSpeedY);
    }

    int meterHeight = 300;

    int velocity = (int) Math.round(calculateDragVelocity());

    double scaledVelocity (){
        return (velocity / maxSpeed) * meterHeight;
    }

    int scaledVelocityInt = (int) Math.round(scaledVelocity());


    // Unused but required by the interface
    public void mouseMoved(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

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
    
   