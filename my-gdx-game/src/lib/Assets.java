package lib;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public static Map<String, Texture> interactiveTextures;
    public static Map<String, Texture> flamerTextures;
    public static Map<String, Texture> playerTextures;
    public static Map<String, Texture> heartTextures;
    public static Texture blockTexture;
    public static Texture bulletTexture;
    public static Pixmap level;

    public static void load() {
        // Load the textures.
        // Player textures.
        playerTextures = new HashMap<String, Texture>();
        playerTextures.put("walking_left_1", new Texture(Gdx.files.internal("images/wizard_01_left.png")));
        playerTextures.put("walking_left_2", new Texture(Gdx.files.internal("images/wizard_02_left.png")));
        playerTextures.put("walking_right_1", new Texture(Gdx.files.internal("images/wizard_01_right.png")));
        playerTextures.put("walking_right_2", new Texture(Gdx.files.internal("images/wizard_02_right.png")));

        // Block textures.
        blockTexture = new Texture(Gdx.files.internal("images/brown_block.png"));

        // Flamer Guy texture.
        flamerTextures = new HashMap<String, Texture>();
        flamerTextures.put("flame_guy_left", new Texture(Gdx.files.internal("images/flame_guy_left.png")));
        flamerTextures.put("flame_guy_right", new Texture(Gdx.files.internal("images/flame_guy_right.png")));

        // Heart textures.
        heartTextures = new HashMap<String, Texture>();
        heartTextures.put("full", new Texture(Gdx.files.internal("images/heart.png")));
        heartTextures.put("empty", new Texture(Gdx.files.internal("images/empty_heart.png")));

        // Interactive Image textures.
        interactiveTextures = new HashMap<String, Texture>();
        interactiveTextures.put("left_arrow", new Texture(Gdx.files.internal("images/left_arrow.png")));
        interactiveTextures.put("right_arrow", new Texture(Gdx.files.internal("images/right_arrow.png")));
        interactiveTextures.put("jump", new Texture(Gdx.files.internal("images/jump.png")));
        interactiveTextures.put("fire", new Texture(Gdx.files.internal("images/fire_button.png")));

        // Bullet texture.
        bulletTexture = new Texture(Gdx.files.internal("images/bullet_orange.png"));

        // The Level.
        level = new Pixmap(Gdx.files.internal("images/levels.png"));
    }
}
