package model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class InteractiveImage extends BoundObject {

    Texture texture;

    public InteractiveImage(Texture texture, Vector2 position) {
        super(position, 25f, 25f);
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

}
