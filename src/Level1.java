
import java.awt.*;
import javax.swing.*; 

public class Level1{
    JFrame frame;

    public static void main(String[] args) {
        JLayeredPane pane;
        JFrame level1Frame = new JFrame("Level 1");
        level1Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        level1Frame.setSize(new Dimension(800, 800));
        LevelDesigner level1 = new LevelDesigner();

        level1Frame.add(level1);

        level1Frame.setVisible(true);

    }

}
