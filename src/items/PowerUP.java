package items;

public class PowerUP extends Immobile {

    private boolean triggered = false;
    public long time = 0;

    public PowerUP(int x, int y, String path) {
        super(x, y, path);
    }

    public void activatePowerUp(Car car) {
        car.setPoweredUp(100);
        car.setSprite(new Sprite("src/resources/testtankPOWERUP.png"));
    }

    public boolean isTriggered() {
        return !triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
        this.time = (System.nanoTime() / 1000000000);
    }
}