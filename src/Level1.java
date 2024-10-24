
import javax.swing.SwingUtilities;




public class Level1 {

    public static void main(String[] args) {
        // Correcting ArrayList initialization
        SwingUtilities.invokeLater(()->{
            // Assuming LevelDesigner is your class that takes these two ArrayLists

            LevelDesigner level1 = new LevelDesigner();
            //level1.initialiseLevel("Level1");
        });
        
    }
    
}
