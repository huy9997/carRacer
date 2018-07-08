import events.GameEvents;
import items.*;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class Player implements Observer {

    private Mobile mobile;
    private int clutchTimer = 0;
    private int count = 0;

    private HashMap<Integer, Boolean> _CONTROLS_P1_;
    private HashMap<Integer, Boolean> _GEARSHIFT_;

    // set controls here
    private final int p1_up = KeyEvent.VK_W;
    private final int p1_down = KeyEvent.VK_S;
    private final int p1_left = KeyEvent.VK_A;
    private final int p1_right = KeyEvent.VK_D;
    private final int p1_clutch = KeyEvent.VK_SPACE;

    private final int p1_gear0 = KeyEvent.VK_0;
    private final int p1_gear1 = KeyEvent.VK_1;
    private final int p1_gear2 = KeyEvent.VK_2;
    private final int p1_gear3 = KeyEvent.VK_3;
    private final int p1_gear4 = KeyEvent.VK_4;
    private final int p1_gear5 = KeyEvent.VK_5;
    private final int p1_gear6 = KeyEvent.VK_6;

    Player (Mobile mobile) {
        this.mobile = mobile;
        this._CONTROLS_P1_ = new HashMap<>();
        this._GEARSHIFT_ = new HashMap<>();

        _CONTROLS_P1_.put(p1_up, false);
        _CONTROLS_P1_.put(p1_down, false);
        _CONTROLS_P1_.put(p1_left, false);
        _CONTROLS_P1_.put(p1_right, false);
        _CONTROLS_P1_.put(p1_clutch, false);

        _GEARSHIFT_.put(p1_gear0, false);
        _GEARSHIFT_.put(p1_gear1, false);
        _GEARSHIFT_.put(p1_gear2, false);
        _GEARSHIFT_.put(p1_gear3, false);
        _GEARSHIFT_.put(p1_gear4, false);
        _GEARSHIFT_.put(p1_gear5, false);
        _GEARSHIFT_.put(p1_gear6, false);
    }

    @Override
    public void update(Observable o, Object arg) {
        GameEvents events = (GameEvents) arg;
        int type = events.getType();

        if (type == 1) {
            int keyCode = (int) events.getTarget();
            setControlStatus(keyCode, true);
        }
        if (type == 2) {
            int keyCode = (int) events.getTarget();
            setControlStatus(keyCode, false);
        }
        if (type == 3) {
            resolveCollision((Item) events.getCaller(), (Item) events.getTarget());
        }
    }

    // manages all cases for collisions, mostly does the math to reposition items that collide
    private void resolveCollision(Item caller, Item target) {
        // type Car vs type PowerUP
        if (caller instanceof Car && target instanceof PowerUP) {
            if (((PowerUP) target).isTriggered()) {
                ((PowerUP) target).activatePowerUp((Car) caller);
                ((PowerUP) target).setTriggered(true);
            }
        }

        // type Car vs type Car
        if (caller instanceof Car && target instanceof Car) {
            int xdisp = caller.getWidth() - Math.abs(caller.getX() - target.getX());
            int ydisp = caller.getHeight() - Math.abs(caller.getY() - target.getY());

            if (xdisp < ydisp) {
                if (caller.getX() > target.getX()) {
                    caller.setX(caller.getX() + (xdisp / 2));
                    target.setX(target.getX() - (xdisp / 2));
                }
                if (caller.getX() <= target.getX()) {
                    caller.setX(caller.getX() - (xdisp / 2));
                    target.setX(target.getX() + (xdisp / 2));
                }
            }
            if (xdisp >= ydisp) {
                if (caller.getY() > target.getY()) {
                    caller.setY(caller.getY() + (ydisp / 2));
                    target.setY(target.getY() - (ydisp / 2));
                }
                if (caller.getY() <= target.getY()) {
                    caller.setY(caller.getY() - (ydisp / 2));
                    target.setY(target.getY() + (ydisp / 2));
                }
            }
            ((Mobile) caller).checkBorder();
            ((Mobile) target).checkBorder();
        }

        // type Car vs type BreakableWall
        if (caller instanceof Car && target instanceof BreakableWall) {
            if (((BreakableWall) target).getDurability() > 0) {
                int xdisp = caller.getWidth() - Math.abs(caller.getX() - target.getX());
                int ydisp = caller.getHeight() - Math.abs(caller.getY() - target.getY());

                if (xdisp < ydisp) {
                    if (caller.getX() > target.getX()) {
                        caller.setX(caller.getX() + xdisp);
                    }
                    if (caller.getX() <= target.getX()) {
                        caller.setX(caller.getX() - xdisp);
                    }
                }
                if (xdisp >= ydisp) {
                    if (caller.getY() > target.getY()) {
                        caller.setY(caller.getY() + ydisp);
                    }
                    if (caller.getY() <= target.getY()) {
                        caller.setY(caller.getY() - ydisp);
                    }
                }
                ((Mobile) caller).checkBorder();
            }
        }

        // type Car vs type Mud
        if (caller instanceof Car && target instanceof Mud) {
            ((Mud) target).setSlowed((Car) caller);
        }

        // type Bullet vs type Car
        if (caller instanceof Bullet && target instanceof Car) {
            if (((Mobile) caller).getSpeed() != 0) {
                // bullet ricochet
                if (((Car) target).getPowerUp() > 0) {
                    double bangle = Math.toRadians(((Mobile) caller).getAngle());
                    double tangle = Math.toRadians(((Mobile) target).getAngle());
                    int diff = Math.negateExact((int) (Math.toDegrees(bangle - tangle)));
                    ((Bullet) caller).setAngle((short) (((Bullet) caller).getAngle() + diff));
                }
                else {
                    ((Bullet) caller).setSpeed(0);
                    ((Bullet) caller).explode();
                }
            }
        }

        // type Bullet vs type BreakableWall
        if (caller instanceof Bullet && target instanceof BreakableWall) {
            if (((BreakableWall) target).getDurability() > 0) {
                if (((Bullet) caller).life > 1) {
                    ((BreakableWall) target).setDurability((int) (((BreakableWall) target).getDurability() - ((Bullet) caller).damage));
                    ((Bullet) caller).life = 1;
                }
                if (((BreakableWall) target).getDurability() <= 0) {
                    (target).setSprite(new Sprite("resources/bwalldestroyed.bmp"));
                }
            }
        }

        if (caller instanceof Car && target instanceof Mobile) {
            ((Car) caller).setGameOver(true);
        }
        ((Car) caller).setGameOver(true);
    }

    private void setControlStatus(int keyCode, Boolean status) {
        if (_CONTROLS_P1_.containsKey(keyCode)) {
            _CONTROLS_P1_.replace(keyCode, status);
        }
        if (_GEARSHIFT_.containsKey(keyCode)) {
            _GEARSHIFT_.replace(keyCode, status);
        }
    }

    public void updateMobiles() {
        if (_CONTROLS_P1_.get(this.p1_up) || _CONTROLS_P1_.get(this.p1_left))
            mobile.rotateLeft();

        if (_CONTROLS_P1_.get(this.p1_down) || _CONTROLS_P1_.get(this.p1_right))
            mobile.rotateRight();

        if (_CONTROLS_P1_.get(this.p1_clutch)) {
            ((Car) mobile).getGearbox().engageClutch();
            clutchTimer = 5;
        } else if (clutchTimer > 0) {
            clutchTimer--;
        } else {
            ((Car) mobile).getGearbox().disengageClutch();
        }

        updateGearshift();
        moveCar();
    }

    private void updateGearshift() {
        if (clutchTimer > 0) {
            if (_GEARSHIFT_.get(this.p1_gear1)) {
                ((Car) mobile).getGearbox().setGear(1);
                ((Car) mobile).changedGear();
            }
            if (_GEARSHIFT_.get(this.p1_gear2)) {
                ((Car) mobile).getGearbox().setGear(2);
                ((Car) mobile).changedGear();
            }
            if (_GEARSHIFT_.get(this.p1_gear3)) {
                ((Car) mobile).getGearbox().setGear(3);
                ((Car) mobile).changedGear();
            }
            if (_GEARSHIFT_.get(this.p1_gear4)) {
                ((Car) mobile).getGearbox().setGear(4);
                ((Car) mobile).changedGear();
            }
            if (_GEARSHIFT_.get(this.p1_gear5)) {
                ((Car) mobile).getGearbox().setGear(5);
                ((Car) mobile).changedGear();
            }
            if (_GEARSHIFT_.get(this.p1_gear6)) {
                ((Car) mobile).getGearbox().setGear(6);
                ((Car) mobile).changedGear();
            }
        }

        if (_GEARSHIFT_.get(this.p1_gear0))
            ((Car) mobile).getGearbox().setGear(0);
    }

    private void moveCar() {
        if (((Car) mobile).getGearbox().getGear() > 0) {
            mobile.setSpeed(mobile.getSpeed() + 1);
        }
        if (mobile.getX() > 24800) {
            System.out.println("LAP: " + ++count);
            if (((Car) mobile).getGearbox().getGear() > 3) {
                mobile.setX(mobile.getSpeed());
            } else {
                mobile.setX(0);
            }
        }
        ((Car) mobile).accelerate();
    }
}