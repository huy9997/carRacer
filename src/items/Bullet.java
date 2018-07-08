package items;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bullet extends Mobile {

    public double damage = 0;
    public int life;
    public boolean water = false;

    public Bullet(int x, int y, String path, short angle, int type, int widthT, int heightT) {
        super(x, y, path, 6, angle);
        if (type == 1) {
            initAmmo1(widthT, heightT);
        }
        if (type == 2) {
            initAmmo2(widthT, heightT);
        }
    }

    public void explode() {
        this.sprite = new Sprite("src/resources/bulletexplode.gif");
        this.speed = 10;
        this.moveBackwards();
        this.speed = 0;
        this.angle += (short) (Math.random() * 90);
        this.life = 0;
    }

    public void initAmmo1(int widthT, int heightT) {
        double delta = Math.toRadians(this.angle - 90);
        // bullet spawn location
        // accounts for weird behavior when
        // alignment beyond 180 degrees
        if (Math.toRadians(this.angle) >= Math.PI) {
            delta = Math.toRadians(this.angle - 75);
        }
        // x center on tank
        int bx = x + (widthT / 2) - (this.width / 2);
        // x move to front
        bx += Math.round((heightT / 4) * Math.cos(delta));
        // y center on tank
        int by = y + (heightT / 2) - (this.width / 2);
        // move to front
        by += Math.round((heightT / 4) * Math.sin(delta));
        this.x = bx;
        this.y = by;
        this.life = 20;
        this.speed = 18;
        this.damage = 1;
    }

    private void initAmmo2(int widthT, int heightT) {
        double delta = Math.toRadians(this.angle - 90);
        // bullet spawn location
        // x center on tank
        int bx = x + (widthT / 2) - (this.width / 2);
        // x move to front
        bx += Math.round((heightT - 8) * Math.cos(delta));
        // y center on tank
        int by = y + (heightT / 2) - (this.width / 2);
        // move to front
        by += Math.round((heightT - 8) * Math.sin(delta));
        this.x = bx;
        this.y = by;
        this.angle = (short) (this.angle +((Math.random() * 60) - 30));
        this.life = 10;
        this.damage = 0.1;
    }

    @Override
    public void drawMobile(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.x, this.y);
        rotation.rotate(Math.toRadians(this.angle), this.height / 2, this.height / 2);
        Graphics2D graphic2D = (Graphics2D) g;
        graphic2D.drawImage(this.sprite.getImage(), rotation, this);
    }

    @Override
    public boolean checkBorder() {
        if (this.y < 192) {
            return true;
        }
        if (this.y >= 448 - this.height) {
            return true;
        }
        return false;
    }
}