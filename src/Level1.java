
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.SwingUtilities;




public class Level1 {

    public static void main(String[] args) {
        // Correcting ArrayList initialization
        SwingUtilities.invokeLater(()->{
            // Assuming LevelDesigner is your class that takes these two ArrayLists
            ArrayList<Rectangle> rects = new ArrayList<>(Arrays.asList(
                new Rectangle(100, 100, 100, 100)
            ));
            ArrayList<Integer> tNum = new ArrayList<>(Arrays.asList(
                1
            ));
            LevelDesigner level1 = new LevelDesigner();
            level1.obstacles.add(new Rectangle(100, 100, 100, 100));
            //level1.initialiseLevel("Level1");
        });
        
    }
    
}
