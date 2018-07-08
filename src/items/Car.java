package items;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Car extends Mobile {

    private boolean gameOver = false;
    private long score = 0;
    private Sprite tire = new Sprite("resources/coupetire1.png");
    private Transmission gearbox = new Transmission();

    private int poweredUp = 0;
    private int rev = 0;

    public Car(int x, int y, String path, int speed, short angle) {
        super(x, y, path, speed, angle);
    }

    @Override
    public void rotateLeft() {
        if (speed > 5) {
            this.y -= 5 + (speed / 55);
        } else {
            this.y -= speed;
        }
    }

    @Override
    public void rotateRight() {
        if (speed > 5) {
            this.y += 5 + (speed / 84);
        } else {
            this.y += speed;
        }
    }

    @Override
    public void moveForwards() {
        System.out.println("SPEED: " + speed);
        score += speed;
        System.out.println("SCORE: " + score);
        this.vx = (int) Math.round(this.speed * Math.cos(Math.toRadians(this.angle)));
        this.vy = (int) Math.round(this.speed * Math.sin(Math.toRadians(this.angle)));
        this.y += this.vx * (-1);
        this.x += this.vy;
        checkBorder();
    }

    public void accelerate() {
        System.out.println("REV: " + rev);
        if (this.gearbox.getGear() == 0) {
            rev = rev > 0 ? rev - 1 : 0;
        } else {
            if (rev < 100) {
                rev++;
            }
        }
        calculateSpeed();
        moveForwards();
    }

    private int calculateMaxSpeed(int gear) {
        int sum = 0;
        for (;gear > 0; gear--) {
            sum += gear * 10;
        }

        if (sum == 0) {
            return 210;
        }
        return sum;
    }

    private void calculateSpeed() {
        System.out.println("CURRENT GEAR: " + this.gearbox.getGear());

        int max = calculateMaxSpeed(this.gearbox.getGear());
        int min = max - this.gearbox.getGear() * 10;
        if (Math.round(0.1 * rev) - this.gearbox.getGear() > 0) {
            this.speed++;
        } else {
            this.speed--;
        }

        if (this.gearbox.clutch) {
            this.speed--;
        }

        if (this.speed < 0) {
            this.speed = 0;
        }
        if (speed < min) {
            this.gearbox.setGear(0);
        }
        if (this.speed > max) {
            this.speed = max;
        }
    }

    public void changedGear() {
        if (rev > this.getGearbox().getGear() * 5) {
            this.rev -= this.getGearbox().getGear() * 5;
        } else {
            this.rev = 0;
        }
    }

    public void setPoweredUp(int poweredUp) {
        this.poweredUp = poweredUp;
    }

    public int getPowerUp() {
        return this.poweredUp;
    }

    public Transmission getGearbox() {
        return gearbox;
    }

    public int getRev() {
        return this.rev;
    }

    public long getScore() {
        return this.score;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean getGameOveR() {
        return this.gameOver;
    }

    @Override
    public void drawMobile(Graphics g) {
        int delta = ((int) Math.round(Math.random() * (this.speed * 0.01)));
        if (Math.round(Math.random()) == 1) {
            delta *= -1;
        }
        this.y = y + delta;

        AffineTransform rotation = AffineTransform.getTranslateInstance(this.x, this.y);
        rotation.rotate(Math.toRadians(this.angle + delta), this.height / 2, this.height / 2);
        Graphics2D graphic2D = (Graphics2D) g;
        drawTire(graphic2D, (int) Math.cos(delta));
        graphic2D.drawImage(this.sprite.getImage(), rotation, this);
    }

    private void drawTire(Graphics2D g2, int delta) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.x - 6, this.y + 17 + delta);
        rotation.rotate(Math.toRadians(this.angle), this.height / 2, this.height / 2);
        rotation.rotate(this.speed, tire.getImage().getHeight() / 2 + 1, tire.getImage().getHeight() / 2);
        g2.drawImage(this.tire.getImage(), rotation, this);
    }
}