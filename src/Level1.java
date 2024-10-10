
import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;  

public class Level1 extends JPanel{
    JFrame frame;
    GolfBall ball;


    public static void main(String[] args) {
        JFrame level1Frame = new JFrame("Level 1");
        level1Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        level1Frame.setSize(new Dimension(600, 800));
        GolfBall golfBall = new GolfBall();

        level1Frame.add(golfBall);


        level1Frame.setVisible(true);

    }

}
/*
 * public class Level1 {
    public static void main(String[] args) {
        JFrame level1Frame = new JFrame("Level 1");
        level1Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        level1Frame.setSize(new Dimension(600, 800));

        // Set OverlayLayout for stacking components
        JPanel layer = new JPanel();
        layer.setLayout(new OverlayLayout(layer));
        level1Frame.add(layer);




        Hole hole = new Hole();
        layer.add(hole);
        hole.setOpaque(false);

        GolfBall golfBall = new GolfBall();
        layer.add(golfBall);
        golfBall.setOpaque(false); // Make sure thelayer is transparent



        level1Frame.setVisible(true);
    }

}
 * 
 * 
 */