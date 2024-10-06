
import java.awt.Dimension;

import javax.swing.JFrame;

public class Level1 {
    public static void main(String[] args) {
        JFrame level1Frame = new JFrame("Level 1");
        level1Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        level1Frame.setSize(new Dimension(600, 800));
        GolfBall golfBall = new GolfBall();
        level1Frame.add(golfBall);
        level1Frame.setVisible(true);;
    }
}
