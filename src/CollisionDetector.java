import items.*;
import events.*;

import java.awt.*;
import java.util.ArrayList;

public class CollisionDetector {

    private GameEvents events;
    public Car mobile;
    private ArrayList<Mobile> mobiles;

    public CollisionDetector(Car mobile, ArrayList<Mobile> mobiles, GameEvents events) {
        this.mobile = mobile;
        this.mobiles = mobiles;
        this.events = events;
    }

    public void checkCollisions() {
        Rectangle mobileArea = new Rectangle(this.mobile.getX(), this.mobile.getY() + 24, this.mobile.getHeight(), 10);
        checkWalls(mobileArea, mobile);
    }

    private void checkWalls(Rectangle callerArea, Mobile caller) {
        for (Mobile mobile: this.mobiles) {
            Rectangle itemArea = new Rectangle(mobile.getX(), mobile.getY() + 77, mobile.getHeight(), 10);
            
            if (callerArea.intersects(itemArea)) {
                System.out.println("BIGGGGG  COLLISSSSSIOOON");
                events.alertCollision(caller, mobile);
            }
        }
    }
}