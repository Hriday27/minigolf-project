public class rough {
import java.io.File; /**imports the File( loading the image) */
import java.io.IOException;/*Imports the exception to handle errors when loading the image.*/ 
import javax.imageio.ImageIO;/* loading images from a file).*/ 
     public class Background extends JPanel {
    private Image image;

    public Background(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath)); // Load the image
        } catch (IOException e) { //in case no iamge is found
            e.printStackTrace();
        }
    }
        @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // Draw the image scaled to panel size
        }
    }

 
}
