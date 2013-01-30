package model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Block {

    public static final float SIZE = 20f;

    Vector2 position = new Vector2();

    public Block(Vector2 pos) {
        this.position = pos;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        Rectangle bounds = new Rectangle();
        bounds.width = SIZE;
        bounds.height = SIZE;
        bounds.x = position.x;
        bounds.y = position.y;

        return bounds;
    }
}
