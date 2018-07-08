package items;

public class Transmission {
    private int gear;
    public boolean clutch;

    Transmission() {
        this.gear = 0;
        this.clutch = false;
    }

    public void engageClutch() {
        this.clutch = true;
    }

    public void disengageClutch() {
        this.clutch = false;
    }

    public void setGear(int gear) {
        this.gear = gear;

        System.out.println("GEARSHIFT: " + gear);
    }

    public int getGear() {
        return this.gear;
    }
}
