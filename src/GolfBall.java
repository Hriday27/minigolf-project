
import java.awt.Graphics;

import javax.swing.JPanel;

public class GolfBall extends JPanel{
    @Override
    public void paint(Graphics g) {
        g.drawOval(100,100, 50,50);
    }


}
