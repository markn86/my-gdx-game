package model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class InteractiveImage {

    public static final float SIZE = 1f;

    Texture texture;
    Vector2 position = new Vector2();
    Rectangle bounds = new Rectangle();

    public InteractiveImage(Texture texture, Vector2 pos) {
        this.texture = texture;
        this.position = pos;
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
        this.bounds.x = position.x;
        this.bounds.y = position.y;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Float getWidth() {
        return bounds.width;
    }

    public Float getHeight() {
        return bounds.height;
    }
}
