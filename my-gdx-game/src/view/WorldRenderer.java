package view;

import java.util.HashMap;
import java.util.Map;

import model.Bullet;
import model.Flamer;
import model.InteractiveImage;
import model.Block;
import model.Character;
import model.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldRenderer {

    public static final float CAMERA_WIDTH = 10f;
    public static final float CAMERA_HEIGHT = 7f;

    private final World world;
    private final OrthographicCamera cam;

    // Textures.
    private Texture blockTexture;
    private final Map<String, Texture> flamerTextures;
    private final Map<String, Texture> characterTextures;
    private final Map<String, Texture> heartTextures;
    private Texture bulletTexture;

    private final SpriteBatch spriteBatch;
    private int width;
    private int height;
    private float ppuX; // Pixels per unit on the X axis.
    private float ppuY; // Pixels per unit on the Y axis.

    public WorldRenderer(World world) {
        this.world = world;
        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        this.cam.update();
        spriteBatch = new SpriteBatch();
        characterTextures = new HashMap<String, Texture>();
        flamerTextures = new HashMap<String, Texture>();
        heartTextures = new HashMap<String, Texture>();
        loadTextures();
    }

    private void loadTextures() {
        // Store character textures.
        characterTextures.put("walking_left_1", new Texture(Gdx.files.internal("images/wizard_01_left.png")));
        characterTextures.put("walking_left_2", new Texture(Gdx.files.internal("images/wizard_02_left.png")));
        characterTextures.put("walking_right_1", new Texture(Gdx.files.internal("images/wizard_01_right.png")));
        characterTextures.put("walking_right_2", new Texture(Gdx.files.internal("images/wizard_02_right.png")));

        // Block textures.
        blockTexture = new Texture(Gdx.files.internal("images/block2.png"));

        // Flamer Guy texture.
        flamerTextures.put("flame_guy_left", new Texture(Gdx.files.internal("images/flame_guy_left.png")));
        flamerTextures.put("flame_guy_right", new Texture(Gdx.files.internal("images/flame_guy_right.png")));

        // Heart textures.
        heartTextures.put("full", new Texture(Gdx.files.internal("images/heart.png")));
        heartTextures.put("empty", new Texture(Gdx.files.internal("images/empty_heart.png")));

        // Bullet texture.
        bulletTexture = new Texture(Gdx.files.internal("images/bullet_orange.png"));
    }

    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
        ppuX = width / CAMERA_WIDTH;
        ppuY = height / CAMERA_HEIGHT;
    }

    public void render() {
        spriteBatch.begin();
        drawBlocks();
        drawInteractiveImages();
        drawCharacter();
        drawFlamers();
        drawHearts();
        drawBullets();
        spriteBatch.end();
    }

    private void drawBlocks() {
        for (Block block : world.getBlocks()) {
            spriteBatch.draw(blockTexture, block.getPosition().x * ppuX, block.getPosition().y * ppuY, Block.SIZE * ppuX, Block.SIZE * ppuY);
        }
    }

    private void drawInteractiveImages() {
        InteractiveImage image = world.getInteractiveImage("leftArrow");
        spriteBatch.draw(image.getTexture(), image.getPosition().x * ppuX, image.getPosition().y * ppuY, InteractiveImage.SIZE * ppuX, InteractiveImage.SIZE * ppuY);

        image = world.getInteractiveImage("rightArrow");
        spriteBatch.draw(image.getTexture(), image.getPosition().x * ppuX, image.getPosition().y * ppuY, InteractiveImage.SIZE * ppuX, InteractiveImage.SIZE * ppuY);

        image = world.getInteractiveImage("jumpIcon");
        spriteBatch.draw(image.getTexture(), image.getPosition().x * ppuX, image.getPosition().y * ppuY, InteractiveImage.SIZE * ppuX, InteractiveImage.SIZE * ppuY);

        image = world.getInteractiveImage("fireIcon");
        spriteBatch.draw(image.getTexture(), image.getPosition().x * ppuX, image.getPosition().y * ppuY, InteractiveImage.SIZE * ppuX, InteractiveImage.SIZE * ppuY);
    }

    private void drawCharacter() {
        Character character = world.getCharacter();
        spriteBatch.draw(characterTextures.get(character.getCharacterImage()), character.getPosition().x * ppuX, character.getPosition().y * ppuY, Character.WIDTH * ppuX, Character.HEIGHT * ppuY);
    }

    private void drawFlamers() {
        for (Flamer flamer : world.getFlamers()) {
            spriteBatch.draw(flamerTextures.get(flamer.getFlamerImage()), flamer.getPosition().x * ppuX, flamer.getPosition().y * ppuY, Flamer.WIDTH * ppuX, Flamer.HEIGHT * ppuY);
        }
    }

    private void drawHearts() {
        // Get the characters health.
        Character character = world.getCharacter();
        int health = character.health;
        // Draw the full hearts.
        for (int i = 0; i < health; i++) {
            spriteBatch.draw(heartTextures.get("full"), (i * 0.5f) * ppuX, 6.2f * ppuY, 0.5f * ppuX, 0.5f * ppuY);
        }
        // Now draw empty hearts.
        for (int i = health; i < 3; i++) {
            spriteBatch.draw(heartTextures.get("empty"), (i * 0.5f) * ppuX, 6.2f * ppuY, 0.5f * ppuX, 0.5f * ppuY);
        }
    }

    private void drawBullets() {
        for (Bullet bullet : world.getBullets()) {
            spriteBatch.draw(bulletTexture, bullet.getPosition().x * ppuX, bullet.getPosition().y * ppuY, Bullet.WIDTH * ppuX, Bullet.HEIGHT * ppuY);
        }
    }
}
