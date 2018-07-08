package events;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoardControl implements KeyListener {

    private GameEvents events;

    public KeyBoardControl(GameEvents events) {
        System.out.println("sdaaaaaaaaaa");
        this.events = events;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        events.alertKeyPressed(keyEvent);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        events.alertKeyPressed(keyEvent);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        events.alertKeyReleased(keyEvent);
    }
}