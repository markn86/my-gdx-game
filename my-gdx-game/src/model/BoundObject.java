package model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BoundObject {

    Vector2 position = new Vector2();
    Vector2 velocity = new Vector2();

    public float width;
    public float height;

    public boolean facingLeft = true;

    public BoundObject(Vector2 pos, float width, float height) {
        this.position = pos;
        this.width = width;
        this.height = height;
    }

    public void update(float delta) {
        position.add(velocity.tmp().mul(delta));
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        Rectangle bounds = new Rectangle();
        bounds.width = width;
        bounds.height = height;
        bounds.x = position.x;
        bounds.y = position.y;

        return bounds;
    }

    public void hit() {
        // Override if needed.
    }

    public boolean isDead() {
        // Override if needed.
        return false;
    }
}
