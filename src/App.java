import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.File; /**imports the File( loading the image) */
import java.io.IOException;/*Imports the exception to handle errors when loading the image.*/ 
import javax.imageio.ImageIO;/* loading images from a file).*/ 
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class App {  
  
    public static void main(String[] args) {  
        // Create a new JFrame (the window)
        JFrame frame = new JFrame("Swing Example");

        // Set the size of the window
        frame.setSize(600, 800);

        // Set the default close operation (exit when the window is closed)
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JPanel with no layout manager (null layout)
        JPanel panel = new JPanel();
        panel.setLayout(null);  // Disable layout manager for absolute positioning

        // Create a JLabel
        JLabel label = new JLabel("Minigolf");

        // Set custom coordinates and size for the label
        label.setBounds(frame.getWidth()/2-100, 50, 200, 30);  // x, y, width, height

        // Add the label to the panel
        panel.add(label);

        // Add the panel to the frame
        frame.add(panel);

        // Make the window visible
        frame.setVisible(true);
    }  
}  