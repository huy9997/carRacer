package items;

public class BreakableWall extends Immobile {

    private int durability = 2;

    public BreakableWall(int x, int y, String path) {
        super(x, y, path);
    }

    public int getDurability() {
        return this.durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
}