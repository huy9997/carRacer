
package items;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Mobile extends Item {

    int vx, vy;
    short angle;
    int speed;
    boolean slowed = false;

    public Mobile(int x, int y, String path, int speed, short angle) {
        super(x, y, path);
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.speed = speed;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public void setAngle(short angle) {
        this.angle = angle;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public short getAngle() {
        return this.angle;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void rotateLeft() {
        this.angle -= 10;
    }

    public void rotateRight() {
        this.angle += 10;
    }

    public void moveBackwards() {
        int speed = this.speed;
        if (slowed) {
            this.speed = 3;
        }
        this.vx = (int) Math.round(this.speed / 2 * Math.cos(Math.toRadians(this.angle)));
        this.vy = (int) Math.round(this.speed / 2 * Math.sin(Math.toRadians(this.angle)));
        this.speed = speed;
        this.slowed = false;
        this.y -= this.vx * (-1);
        this.x -= this.vy;
        checkBorder();
    }

    public void moveForwards() {
        int speed = this.speed;
        if (slowed) {
            this.speed = 3;
        }
        this.vx = (int) Math.round(this.speed * Math.cos(Math.toRadians(this.angle)));
        this.vy = (int) Math.round(this.speed * Math.sin(Math.toRadians(this.angle)));
        this.speed = speed;
        this.slowed = false;
        this.y += this.vx * (-1);
        this.x += this.vy;
        checkBorder();
    }

    public void drawMobile(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.x, this.y);
        rotation.rotate(Math.toRadians(this.angle), this.height / 2, this.height / 2);
        Graphics2D graphic2D = (Graphics2D) g;
        graphic2D.drawImage(this.sprite.getImage(), rotation, this);
    }

    public boolean checkBorder() {
        boolean collision = false;
        if (this.y < 170) {
            this.y = 170;
            collision = true;
        }
        if (this.y >= 352) {
            this.y = 352;
            collision = true;
        }
        return collision;
    }
}