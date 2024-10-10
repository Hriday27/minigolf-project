
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.*;
import java.sql.Time;

import javax.swing.JPanel;

public class GolfBall extends JPanel implements MouseListener, MouseMotionListener, ActionListener{
    int speed = 100;
    int x = 250;
    int y = 700;
    int width = 50;
    int height = 50;

    int currentMouseX;
    int currentMouseY;
    int dragX;
    int dragY;

    Timer animationTimer;

    double dynamicFC = 0.1;
    double staticFC = 0.3;
    double mass = 0.1;
    double appliedForce = 3;
    double g = 9.81;
    double timeForceApplied = 1;

    double normalForce = g * mass;
    double fKinetic = dynamicFC * normalForce;
    double fNet = appliedForce - fKinetic;

    public GolfBall(){
        animationTimer = new Timer(10, this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int lineX1 = this.x + width/2;
        int lineY1 = this.y + height/2;
        int lineX2 = lineX1 + 3*(this.currentMouseX-this.dragX);
        int lineY2 = lineY1 + 3*(this.currentMouseY-this.dragY);

        Graphics2D g2 = (Graphics2D) g;
        g.drawOval(x,y, 50,50);
        g.drawLine(lineX1, lineY1, lineX2, lineY2);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.currentMouseX = e.getX();
        this.currentMouseY = e.getY();
        int radius = this.width/2;
        if (Math.pow(e.getX()-(this.x + radius),2) + Math.pow(e.getY()-(this.y + radius),2) <= Math.pow(this.width/2,2)){
            System.out.println("You pressed the ball");
        }
    }

    public void mouseReleased(MouseEvent e) {
        animationTimer.start();
    }
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        this.dragX = e.getX();
        this.dragY = e.getY();
        repaint();
    }
    public void mouseMoved(MouseEvent e) {}

    public void actionPerformed(ActionEvent ae) {
        if(fNet>0) {
            this.fNet -= fKinetic;
            double acceleration = this.fNet/mass;
            double speed = acceleration*timeForceApplied;
            y -= speed;
            repaint();
        }

        else {
            animationTimer.stop();
        }
    
    }
}
