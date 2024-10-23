import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class StatsPanel extends JPanel implements ActionListener{

    private int velocity;  // Variable to store the current velocity
    Timer timer;

    public StatsPanel() {
        this.velocity = 0;  // Initialize velocity to 0
        timer = new Timer(10, this);
    }

    // Method to update the velocity from the drag event
    public void updateVelocity(int newVelocity) {
        this.velocity = newVelocity;
        repaint();  // Repaint the panel to update the speed bars
    }

    // Helper method to map velocity values to bar heights
    public static int convertValue(int value, int min1, int max1, int min2, int max2) {
        return min2 + ((value - min1) * (max2 - min2)) / (max1 - min1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("I was called");
        // Set background color for the panel
        g.setColor(Color.ORANGE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Max possible velocity (based on maximum drag distance in the window)
        int min1 = 0;
        int max1 = (int) Math.sqrt(Math.pow(800, 2) + Math.pow(600, 2)); // Hypotenuse for 800x600 window
        int min2 = 0;
        int max2 = 300; // Max bar height (300 pixels)

        // Convert the velocity to a mapped height for the speed bar
        int mappedHeight = convertValue(velocity, min1, max1, min2, max2);

        // Cap the height at 300 (or max2) to avoid overflow
        mappedHeight = Math.min(mappedHeight, max2);

        // Draw the three bars based on the mappedHeight
        int barX = 125;
        int barWidth = 50;

        // Yellow bar (low speed)
        if (mappedHeight < 100) {
            g.setColor(Color.YELLOW);
            g.fillRect(barX, 370 - mappedHeight, barWidth, mappedHeight);
        }
        // Green bar (medium speed)
        else if (mappedHeight < 200) {
            g.setColor(Color.YELLOW);
            g.fillRect(barX, 370 - 100, barWidth, 100);
            g.setColor(Color.GREEN);
            g.fillRect(barX, 370 - mappedHeight, barWidth, mappedHeight - 100);
        }
        // Red bar (high speed)
        else {
            g.setColor(Color.YELLOW);
            g.fillRect(barX, 370 - 100, barWidth, 100);
            g.setColor(Color.GREEN);
            g.fillRect(barX, 270 - 100, barWidth, 100);
            g.setColor(Color.RED);
            g.fillRect(barX, 170 - (mappedHeight - 200), barWidth, mappedHeight - 200);
        }
    }

    public void actionPerformed(ActionEvent e) {

    }
}
