
import java.awt.*;
import javax.swing.*; 
import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
public class Level1{

    public static void main(String[] args) {
        JFrame level1Frame = new JFrame("Level 1");
        level1Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        level1Frame.setSize(new Dimension(800, 800));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800,800));

        LevelDesigner level1 = new LevelDesigner();

        try {
            BufferedImage background = ImageIO.read(new File("A:\\Eindhoven\\CBL Project\\Minigolf\\src\\golf.jpg"));
            layeredPane.add(level1, JLayeredPane.PALETTE_LAYER);

        } catch (Exception e) {
        }

        level1Frame.add(level1);
        level1Frame.setVisible(true);

    }

}
