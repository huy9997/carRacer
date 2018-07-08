package events;

import java.awt.event.KeyEvent;
import java.util.Observable;

public class GameEvents extends Observable {

    private Object caller, target;
    private int type = 0;

    public void alertKeyPressed(KeyEvent keyEvent) {
        this.type = 1;
        this.target = keyEvent.getKeyCode();
        this.setChanged();
        this.notifyObservers(this);
    }

    public void alertKeyReleased(KeyEvent keyEvent) {
        this.type = 2;
        this.target = keyEvent.getKeyCode();
        this.setChanged();
        this.notifyObservers(this);
    }

    public void alertCollision(Object caller, Object target) {
        this.type = 3;
        this.caller = caller;
        this.target = target;
        this.setChanged();
        this.notifyObservers(this);
    }

    public int getType() {
        return type;
    }

    public Object getCaller() {
        return caller;
    }

    public Object getTarget() {
        return target;
    }
}