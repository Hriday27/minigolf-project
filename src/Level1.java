
import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;  
import java.util.ArrayList;

public class Level1 extends JPanel{
    JFrame frame;

    public static void main(String[] args) {
        JFrame level1Frame = new JFrame("Level 1");
        level1Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        level1Frame.setSize(new Dimension(800, 800));
        LevelDesigner level1 = new LevelDesigner();

        level1Frame.add(level1);

        level1Frame.setVisible(true);

        ArrayList<Integer> list1 = new ArrayList<>();
        list1.add(235);
        list1.add(750);

        // Second list
        ArrayList<Integer> list2 = new ArrayList<>();
        list2.add(235);
        list2.add(750);

        if (list1.equals(list2)) {
            System.out.println("The lists are equal." + list1 + " || " + list2);
        } else {
            System.out.println("The lists are not equal.");
        }

    }

}
