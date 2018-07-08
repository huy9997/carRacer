package items;

public class Mud extends Immobile {

    public Mud(int x, int y, String path) {
        super(x, y, path);
    }

    public void setSlowed(Car car) {
        car.slowed = true;
    }
}
