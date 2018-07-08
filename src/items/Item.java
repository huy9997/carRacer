package items;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public abstract class Item extends JComponent {

    Sprite sprite;
    Rectangle area;
    int x, y, width, height;

    Item(int x, int y, String path){
        this.x = x;
        this.y = y;
        this.sprite = new Sprite(path);
        this.width = this.sprite.getImage().getWidth(null);
        this.height = this.sprite.getImage().getHeight(null);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setX(int a){
        this.x = a;
    }

    public void setY(int b){
        this.y = b;
    }

    public void drawItem(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(0), this.height / 2, this.height / 2);
        Graphics2D graphic2D = (Graphics2D) g;
        graphic2D.drawImage(this.sprite.getImage(), rotation, this);
    }
}