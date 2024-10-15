import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Level1 {

    public static void main(String[] args) {
        JFrame level1Frame = new JFrame("Level 1");
        level1Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        level1Frame.setSize(new Dimension(800, 800));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 800));

        LevelDesigner level1 = new LevelDesigner();
        level1.setBounds(0, 0, 800, 800);
        level1.setOpaque(false);  // Make LevelDesigner transparent to see the background

        try {
            // Load the background image
            BufferedImage background = ImageIO.read(new File("A:\\Eindhoven\\CBL Project\\Minigolf\\src\\texture4.jpg"));
            ImagePanel ip = new ImagePanel(background);
            ip.setBounds(0, 0, 800, 800);
            
            // Add background to the DEFAULT layer
            layeredPane.add(ip, JLayeredPane.DEFAULT_LAYER);
            
            // Add the LevelDesigner game to the PALETTE layer (above background)
            layeredPane.add(level1, JLayeredPane.PALETTE_LAYER);
            
        } catch (Exception e) {
            System.out.println("No image found");
        }

        level1Frame.add(layeredPane);
        level1Frame.setVisible(true);
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
