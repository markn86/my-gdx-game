package model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {

    public static final float SPEED = 160f;
    public static final float HEIGHT = 8f;
    public static final float WIDTH = 8f;

    public Vector2 position;
    public Vector2 velocity = new Vector2();
    public boolean facingLeft;

    public Bullet(Vector2 position, boolean facingLeft) {
        this.position = position;
        if (facingLeft) {
            velocity.x = -SPEED;
        } else {
            velocity.x = SPEED;
        }
        this.facingLeft = facingLeft;
    }

    public void update(float delta) {
        position.add(velocity.tmp().mul(delta));
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
}
