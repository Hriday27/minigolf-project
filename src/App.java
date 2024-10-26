import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.*;

class ImagePanel extends JComponent{
    private Image image;
    public ImagePanel(Image image) {
        this.image = image;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}

public class App {  
    private static Image background;
    public static void main(String[] args) {  

        JFrame frame = new JFrame("Minigolf");
        frame.setSize(600, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600,550));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout((new BoxLayout(mainPanel, BoxLayout.Y_AXIS)));
        mainPanel.setBounds(0,0,600,550);

        JLabel label = new JLabel("Minigolf");
        label.setFont(new Font("Arial", Font.PLAIN, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        LevelPanel lPanel = new LevelPanel(5);

        mainPanel.add(Box.createRigidArea(new Dimension(100,50)));
        mainPanel.add(label);
        mainPanel.add(lPanel);
        mainPanel.setOpaque(false);

        try {
                      
            //background = ImageIO.read(getClass().getResource("/golf.jpg")); //*TODO: Make image is displayed withoout the user needing to copy the path of golf.jpg */
            ImagePanel ip = new ImagePanel(background);
            ip.setBounds(0,0,600,550);

            layeredPane.add(ip, JLayeredPane.DEFAULT_LAYER);
            layeredPane.add(mainPanel, JLayeredPane.PALETTE_LAYER);

            frame.setLayeredPane(layeredPane);


        } catch (Exception e) {
            System.out.println("Image could not be read");
        }
        frame.setVisible(true);
    }  
}  