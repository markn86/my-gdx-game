package model;

import com.badlogic.gdx.math.Vector2;

public class Bullet extends BoundObject {

    public static final float SPEED = 160f;

    public Bullet(Vector2 position, boolean facingLeft) {
        super(position, 8f, 8f);
        if (facingLeft) {
            velocity.x = -SPEED;
        } else {
            velocity.x = SPEED;
        }
        this.facingLeft = facingLeft;
    }
}
