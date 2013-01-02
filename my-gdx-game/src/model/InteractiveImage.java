package model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class InteractiveImage {

    public static final float SIZE = 1f;

    Texture texture;
    Vector2 position = new Vector2();

    public InteractiveImage(Texture texture, Vector2 pos) {
        this.texture = texture;
        this.position = pos;
    }

    public Texture getTexture() {
        return texture;
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
