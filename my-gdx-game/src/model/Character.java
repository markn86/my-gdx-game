package model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Character {

    public enum State {
        IDLE, WALKING, JUMPING, DYING
    }

    public static final float SPEED = 2f; // Units per second.
    public static final float JUMP_VELOCITY = 1f;
    public static final float FALL_VELOCITY = 1f;
    public static final float SIZE = 0.5f; // Half unit.
    public static final float HEIGHT = 0.75f; // Half a unit
    public static final float WIDTH = 0.5f;

    public boolean won = false;
    public int health = 3;
    public boolean isdead = false;
    public boolean shooting = false;
    Vector2 position = new Vector2();
    Vector2 acceleration = new Vector2();
    Vector2 velocity = new Vector2();
    Rectangle bounds = new Rectangle();
    State state = State.IDLE;
    boolean facingLeft = true;
    public boolean falling = false;

    // Keep track of which image we last used to display the character.
    String characterImage = "walking_left_1";

    public Character(Vector2 position) {
        this.position = position;
        this.bounds.height = HEIGHT;
        this.bounds.width = WIDTH;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public void update(float delta) {
        position.add(velocity.tmp().mul(delta));
    }

    public Boolean isFalling() {
        return falling;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void hit() {
        if (health >= 0){
            health--;
        }
    }

    public int getHealth() {
        return health;
    }

    public void isDead(boolean isdead) {
        this.isdead = isdead;
    }

    public String getCharacterImage() {
        return characterImage;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setCharacterImage() {
        if (state == State.WALKING) {
            if (this.isFacingLeft()) {
                if (characterImage == "walking_left_1") {
                    characterImage = "walking_left_2";
                } else {
                    characterImage = "walking_left_1";
                }
            } else {
                if (characterImage == "walking_right_1") {
                    characterImage = "walking_right_2";
                } else {
                    characterImage = "walking_right_1";
                }
            }
        }
    }
}
