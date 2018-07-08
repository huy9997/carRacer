package items;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sprite {
    private BufferedImage image;

    public Sprite() {
        this.image = loadImage("src/resources/placeholder.jpg");
    }

    public Sprite (String path) {
        this.image = loadImage(path);
    }

    private static BufferedImage loadImage(String path){
        try {
            File image = new File(path);
            return ImageIO.read(image);
            //returns buffered image.
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}