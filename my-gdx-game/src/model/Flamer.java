package model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Flamer {

    public enum State {
        IDLE, WALKING, JUMPING, DYING
    }

    public static final float SPEED = 1f; // Units per second.
    public static final float HEIGHT = 0.75f; // Half a unit
    public static final float WIDTH = 0.5f;

    public boolean won = false;
    public int health = 3;
    public float timeSinceHit = 0;
    public boolean isdead = false;
    public boolean shooting = false;

    Vector2 position = new Vector2();
    Vector2 velocity = new Vector2();

    State state = State.IDLE;
    public boolean facingLeft = false;
    public boolean falling = false;

    public Flamer(Vector2 position) {
        this.position = position;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public Rectangle getBounds() {
        Rectangle bounds = new Rectangle();
        bounds.width = WIDTH;
        bounds.height = HEIGHT;
        bounds.x = position.x;
        bounds.y = position.y;

        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void update(float delta) {
        position.add(velocity.tmp().mul(delta));
        timeSinceHit += delta;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void hit() {
        if (health >= 0){
            health--;
        }
        // Reset the counter.
        timeSinceHit = 0;
    }

    public boolean isDead() {
        if (health <= 0) {
            return true;
        }

        return false;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public String getFlamerImage() {
        if (this.facingLeft) {
            return "flame_guy_left";
        } else {
            return "flame_guy_right";
        }
    }
}
