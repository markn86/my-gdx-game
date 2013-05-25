package lib;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

    public static Map<String, Texture> interactiveTextures;
    public static Map<String, Texture> flamerTextures;
    public static Map<String, Texture> playerTextures;
    public static Map<String, Texture> heartTextures;

    public static Animation playerLeftWalk;
    public static Animation playerRightWalk;

    public static Animation flamerLeftWalk;
    public static Animation flamerRightWalk;

    public static Texture blockTexture;
    public static Texture bulletTexture;
    public static Pixmap level;

    public static void load() {
        // Load the textures.
        // Player textures.
        playerTextures = new HashMap<String, Texture>();
        playerTextures.put("walking_1", new Texture(Gdx.files.internal("images/wizard_1.png")));
        playerTextures.put("walking_2", new Texture(Gdx.files.internal("images/wizard_2.png")));

        // Set the player animations.
        TextureRegion[] leftWalkFrames = new TextureRegion[2];
        TextureRegion[] rightWalkFrames = new TextureRegion[2];
        leftWalkFrames[0] = new TextureRegion(playerTextures.get("walking_1"));
        leftWalkFrames[1] = new TextureRegion(playerTextures.get("walking_2"));
        for (int i = 0; i < rightWalkFrames.length; i++) {
            TextureRegion frame = new TextureRegion(leftWalkFrames[i]);
            frame.flip(true, false);
            rightWalkFrames[i] = frame;
        }

        playerLeftWalk = new Animation(0.25f, leftWalkFrames);
        playerRightWalk = new Animation(0.25f, rightWalkFrames);

        // Block textures.
        blockTexture = new Texture(Gdx.files.internal("images/brown_block.png"));

        // Flamer Guy texture.
        flamerTextures = new HashMap<String, Texture>();
        flamerTextures.put("walking_1", new Texture(Gdx.files.internal("images/flamer_1.png")));

        // Set the flamer animations.
        leftWalkFrames = new TextureRegion[1];
        rightWalkFrames = new TextureRegion[1];
        leftWalkFrames[0] = new TextureRegion(flamerTextures.get("walking_1"));
        for (int i = 0; i < rightWalkFrames.length; i++) {
            TextureRegion frame = new TextureRegion(leftWalkFrames[i]);
            frame.flip(true, false);
            rightWalkFrames[i] = frame;
        }

        flamerLeftWalk = new Animation(0.25f, leftWalkFrames);
        flamerRightWalk = new Animation(0.25f, rightWalkFrames);

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
