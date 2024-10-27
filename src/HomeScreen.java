import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class HomeScreen extends JFrame {
    public HomeScreen() {
        // Set up frame properties
        setTitle("Minigolf Menu");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(0, 0);  // Top-left corner
        setLayout(null);
        setResizable(false);

        // Create a JPanel with a scaled GIF background
        BackgroundPanel backgroundPanel = new BackgroundPanel("golf-field.gif");
        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, getWidth(), getHeight());
        add(backgroundPanel);

        // Create the heading label and add it to the background panel
        JLabel headingLabel = new JLabel("Minigolf", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Monospaced", Font.BOLD, 60));
        headingLabel.setBounds(40, 50, 400, 100);
        headingLabel.setForeground(Color.white);  // Adjust text color for visibility
        backgroundPanel.add(headingLabel);

        // Button properties
        String[] buttonLabels = {"Level 1 - Grass (Easy)", "Level 2 - Ice and TNT (Medium)",
                                 "Level 3 - Space and Teleport (Hard)", "Level 4 - Tornado (Surprise)"};
        Color[] buttonColors = {new Color(144, 238, 144), Color.ORANGE, Color.RED, Color.BLACK};
        Color[] hoverColors = {new Color(102, 204, 102), new Color(255, 165, 0),
                               new Color(204, 0, 0), new Color(50, 50, 50)};
        Color[] textColors = {Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE};

        int yPos = 150;
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.setFont(new Font("Monospaced", Font.BOLD, 14));
            button.setBounds(50, yPos, 380, 50);
            button.setBackground(buttonColors[i]);
            button.setForeground(textColors[i]);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(true);
            
            // Hover effect
            final int index = i;
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(hoverColors[index]);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(buttonColors[index]);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    button.setBackground(buttonColors[index].darker());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    button.setBackground(hoverColors[index]);
                }
            });

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LevelDesigner level = new LevelDesigner(index + 1);
                }
            });

            backgroundPanel.add(button);  // Add buttons to the background panel
            yPos += 75; // 50 pixels for button height + 25 pixels for spacing
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomeScreen frame = new HomeScreen();
            frame.setVisible(true);
        });
    }
}

// Custom JPanel to draw the scaled GIF as the background
class BackgroundPanel extends JPanel {
    private ImageIcon backgroundGif;

    public BackgroundPanel(String gifPath) {
        backgroundGif = new ImageIcon(getClass().getResource(gifPath));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the GIF scaled to the panel size
        g.drawImage(backgroundGif.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
}
