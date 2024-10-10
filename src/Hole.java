import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JPanel;

public class Hole extends JPanel{
    @Override
      public void paint(Graphics g){
          g.setColor(Color.BLACK);
          g.fillOval(300,300, 30,30);
      }
}