package model;

import com.badlogic.gdx.math.Vector2;

public class Bullet {

    public static final float SPEED = 6f;
    public static final float HEIGHT = 0.25f;
    public static final float WIDTH = 0.25f;

    public Vector2 position;
    public Vector2 velocity;
    public boolean facingLeft;

    public Bullet(Vector2 position, boolean facingLeft) {
        this.position = position;
        if (facingLeft) {
            velocity.x = -SPEED;
        } else {
            velocity.x = SPEED;
        }
    }
}
