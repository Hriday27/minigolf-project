
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.*;
import java.sql.Time;
import static java.lang.Math.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    int dragX;
    int dragY;

    int velocityX = 0;
    int velocityY = 0;

    Timer animationTimer;

    double dynamicFC = 0.3;
    double staticFC = 0.6;
    double mass = 0.1;
    double appliedForce;
    double g = 9.81;
    double timeForceApplied = 0.1;

    double normalForce = g * mass;
    double fKinetic = dynamicFC * normalForce;
    double fNet;
    double moveMagnitude = 0.3;


    ArrayList<Rectangle> obstacles;
    ArrayList<ArrayList<Integer>> collisionObjects = new ArrayList<>();

    public LevelDesigner(){
        animationTimer = new Timer(1, this);
        obstacles = new ArrayList<>();
        generateObstacle(300, 400, 100, 200);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void generateObstacle(int x, int y, int width, int height) {
        obstacles.add(new Rectangle(x,y,width,height));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int lineX1 = this.x + radius/2;
        int lineY1 = this.y + radius/2;
        int lineX2 = lineX1 + 3*(this.currentMouseX-this.dragX);
        int lineY2 = lineY1 + 3*(this.currentMouseY-this.dragY);

        // ball and hole
        g.setColor(Color.green);
        g.fillOval(x,y, radius,radius);
        g.setColor(Color.red);
        g.drawLine(lineX1, lineY1, currentMouseX, currentMouseY);
        g.setColor(Color.BLACK);
        g.fillOval(300,100, 30,30);

        // add obstacles
        g.setColor(Color.RED);
        for (Rectangle rect : obstacles) {
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    public void addCollisionObject(Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.RED);
        g.fillRect(x,y,width,height);

        ArrayList<Integer> objectDetails = new ArrayList<>(); 
        objectDetails.add(x); 
        objectDetails.add(y);
        objectDetails.add(width);
        objectDetails.add(height);

        collisionObjects.add(objectDetails);
    }

    public ArrayList<ArrayList<Integer>> returnObjectPerimeter(ArrayList<Integer> objectProperties) {
        ArrayList<ArrayList<Integer>> pointsList = new ArrayList<>();
        int x = objectProperties.get(0);
        int y = objectProperties.get(1);
        int width = objectProperties.get(2);
        int height = objectProperties.get(3);

        // horizontal sides
        for(int i = x; i <= x + width; i++) {
            ArrayList<Integer> side1 = new ArrayList<>();
            side1.add(i);
            side1.add(y);

            ArrayList<Integer> side2 = new ArrayList<>();
            side2.add(i);
            side2.add(y+height);

            pointsList.add(side1);
            pointsList.add(side2);
        }

        // vertical sides
        for (int i = y; i<=y+height; i++) {
            ArrayList<Integer> side1 = new ArrayList<>();
            side1.add(x);
            side1.add(i);

            ArrayList<Integer> side2 = new ArrayList<>();
            side2.add(x+width);
            side2.add(i);

            pointsList.add(side1);
            pointsList.add(side2);
        }

        return pointsList;
    }

    public ArrayList<ArrayList<Integer>> returnCirclePoints() {
        ArrayList<ArrayList<Integer>> pointsList = new ArrayList<>();
        
        // the four points in a circle 90 degrees apart on the perimeter
        // these are the only points that will actually collide with the objects

        for (int i = 0; i < 4; i++) {
            double theta = Math.toRadians(90*i);
            double pointX = x + radius * Math.cos(theta);
            double pointY = y + radius * Math.sin(theta);
            pointsList.add(new ArrayList<Integer>(){{
                add((int)pointX);
                add((int)pointY);
            }});
        }
        

        return pointsList;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragStartX = e.getX();
        dragStartY = e.getY();
    }

    public void mouseReleased(MouseEvent e) {
        dragEndX = e.getX();
        dragEndY = e.getY();

        velocityX = (dragStartX-dragEndX)/5;
        velocityY = (dragStartY-dragEndY)/5;

        animationTimer.start();
    }
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        currentMouseX = e.getX();
        currentMouseY = e.getY();
        repaint();
    }
    public void mouseMoved(MouseEvent e) {}
    /*
    public void actionPerformed(ActionEvent ae) {
        if(fNet>0) {
            boolean moveornot = false;
            ArrayList<ArrayList<Integer>> circlePoints = returnCirclePoints();
            this.fNet -= fKinetic;
            double acceleration = this.fNet/mass;
            double speed = acceleration * timeForceApplied;
            double xVector = currentMouseX - dragX;
            double yVector = currentMouseY - dragY;
            double constant = sqrt(speed/(pow(xVector, 2) + pow(yVector, 2)));

            if(constant > 0) {
                for(int i = 0; i < collisionObjects.size(); i++) {    
                    for (int j = 0; j < circlePoints.size(); j++) {
                        if(circlePoints.get(j).get(0) <= collisionObjects.get(i).get(0) + collisionObjects.get(i).get(2) && 
                            circlePoints.get(j).get(0)>=collisionObjects.get(i).get(0)) {
                            if(circlePoints.get(j).get(1).equals(collisionObjects.get(i).get(1)) || 
                            circlePoints.get(j).get(1).equals(collisionObjects.get(i).get(1)+collisionObjects.get(i).get(3))) {
                                double x = xVector;
                                double y = yVector;
                                xVector = y;
                                yVector = x;
                                //moveMagnitude *= -1;
                                moveornot = true;
                                break;
                                //System.out.print(true);

    
                                //moveMagnitude = moveMagnitude * -1;
    
                            }
                        }
                    }
                }
                if (moveornot==true) {
                    System.out.print(true);
                    x += -moveMagnitude*xVector;
                    y += -moveMagnitude*yVector;
                } else {
                    x += moveMagnitude*xVector;
                    y += moveMagnitude*yVector;
                }
                
                System.out.println("Speed: " + speed + "| xVector: " + xVector + "| yVector: " + yVector + "| constant: " + constant + " move| " + moveMagnitude);
                System.out.println("+++++ " + returnCirclePoints().get(1));
                System.out.println("----- " + returnObjectPerimeter(collisionObjects.get(1)).get(400));

                repaint();
            }

        }

        else {
            animationTimer.stop();
        }
    
    }
        */
        public void actionPerformed(ActionEvent ae) {
            x += velocityX;
            y += velocityY;

            velocityX *= 0.98;
            velocityY *= 0.98;

            if (Math.abs(velocityX) < 0.1 && Math.abs(velocityY) < 0.1) {
                velocityX = 0;
                velocityY = 0;
            }

            // Collision detection and resolution
            for (Rectangle rect : obstacles) {
                if (new Rectangle(x,y,radius,radius).intersects(rect)) {
                    performCollisionLogic(rect);
                    System.out.print(true + " ");
                    System.out.print("circle y " + y + " rectangle y " + (rect.y + rect.height));
                    System.out.println();
                }
            }
            
            repaint();
            
        }

        public void performCollisionLogic(Rectangle rect) {
            // Check if the ball is colliding from the sides or the top/bottom
            if (x < rect.x && y > rect.y) { // Left of the rectangle
                //x = rect.x - ballRadius;
                velocityX = -Math.abs(velocityX);
            } else if (x < rect.x + rect.width && y > rect.y) { // Right of the rectangle
                //ballX = rect.x + rect.width + ballRadius;
                velocityX = Math.abs(velocityX);
            } else if (y < rect.y) { // Above the rectangle
                System.out.print(true);
                //ballY = rect.y - ballRadius;
                velocityY = -Math.abs(velocityY);
            } else if (y > rect.y + rect.height) { // Below the rectangle
                //ballY = rect.y + rect.height + ballRadius;
                velocityY = Math.abs(velocityY);
            }
        }
        
    }