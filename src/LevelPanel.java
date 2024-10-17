import java.awt.*;
import javax.swing.*;

public class LevelPanel extends JPanel {
    // Constructor to set up the panel
    int levels;
    public LevelPanel(int totalLevels) {
        this.levels = totalLevels;
        this.setPanelLayout();
    }

    // Method to add a component to the panel
    public void setPanelLayout() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Use FlowLayout by default

        for(int i = 0; i < this.levels; i++) {
            JPanel p1 = new JPanel();
            p1.setBackground(Color.white);
            p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));

            JLabel levelName = new JLabel("Level " + Integer.toString(i+1));
            JButton playButton = new JButton("Play");
            playButton.setPreferredSize(new Dimension(200,50));
            if(i>0){             this.add(Box.createRigidArea(new Dimension(50,10)));
            }

            p1.add(Box.createRigidArea(new Dimension(10,50)));
            p1.add(levelName);

            p1.add(Box.createRigidArea(new Dimension(300,50)));
            p1.add(playButton);

            p1.add(Box.createRigidArea(new Dimension(10,50)));
            this.add(p1);
        }

    }

    // Method to set a custom layout
    public void setCustomLayout(LayoutManager layout) {
        this.setLayout(layout);
    }
    
    // Method to change the background color
    public void changeBackgroundColor(Color color) {
        this.setBackground(color);
    }
}
