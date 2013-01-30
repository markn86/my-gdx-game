package view;

import java.util.HashMap;
import java.util.Map;

import screens.GameScreen;

import lib.Sound;
import model.Bullet;
import model.Flamer;
import model.Block;
import model.Player;
import model.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import controller.WorldController;

public class WorldRenderer {

    // The world.
    private World world;

    // The controller.
    private WorldController controller;

    // The player.
    Player player;

    // The world the player will be playing in.
    public static Pixmap level = new Pixmap(Gdx.files.internal("images/levels.png"));

    // Textures.
    private Texture blockTexture;
    public static Map<String, Texture> interactiveTextures;
    public static Map<String, Texture> flamerTextures;
    public static Map<String, Texture> playerTextures;
    public static Map<String, Texture> heartTextures;
    private Texture bulletTexture;

    public static SpriteBatch spriteBatch;
    private int width;
    private int height;
    private float ppuX; // Pixels per unit on the X axis.
    private float ppuY; // Pixels per unit on the Y axis.

    public WorldRenderer(World world) {
        this.world = world;
        controller = new WorldController(world);

        player = world.getPlayer();
        spriteBatch = new SpriteBatch();
        playerTextures = new HashMap<String, Texture>();
        flamerTextures = new HashMap<String, Texture>();
        interactiveTextures = new HashMap<String, Texture>();
        heartTextures = new HashMap<String, Texture>();
        loadTextures();
        Sound.load();
    }

    private void loadTextures() {
        // Store player textures.
        playerTextures.put("walking_left_1", new Texture(Gdx.files.internal("images/wizard_01_left.png")));
        playerTextures.put("walking_left_2", new Texture(Gdx.files.internal("images/wizard_02_left.png")));
        playerTextures.put("walking_right_1", new Texture(Gdx.files.internal("images/wizard_01_right.png")));
        playerTextures.put("walking_right_2", new Texture(Gdx.files.internal("images/wizard_02_right.png")));

        // Block textures.
        blockTexture = new Texture(Gdx.files.internal("images/brown_block.png"));

        // Flamer Guy texture.
        flamerTextures.put("flame_guy_left", new Texture(Gdx.files.internal("images/flame_guy_left.png")));
        flamerTextures.put("flame_guy_right", new Texture(Gdx.files.internal("images/flame_guy_right.png")));

        // Heart textures.
        heartTextures.put("full", new Texture(Gdx.files.internal("images/heart.png")));
        heartTextures.put("empty", new Texture(Gdx.files.internal("images/empty_heart.png")));

        // Interactive Image textures.
        interactiveTextures.put("left_arrow", new Texture(Gdx.files.internal("images/left_arrow.png")));
        interactiveTextures.put("right_arrow", new Texture(Gdx.files.internal("images/right_arrow.png")));
        interactiveTextures.put("jump", new Texture(Gdx.files.internal("images/jump.png")));
        interactiveTextures.put("fire", new Texture(Gdx.files.internal("images/fire_button.png")));

        // Bullet texture.
        bulletTexture = new Texture(Gdx.files.internal("images/bullet_orange.png"));
    }

    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
        ppuX = width / GameScreen.CAMERA_WIDTH;
        ppuY = height / GameScreen.CAMERA_HEIGHT;
    }

    public void render(float delta) {
        // Update the position of everything.
        controller.update(delta);

        // Draw the elements.
        drawBlocks(world);
        drawPlayer(world);
        drawFlamers(world);
        drawBullets(world);

        // Check here if we need to start transition between screens.
        float x = player.getPosition().x;
        float y = player.getPosition().y;
        float w = Player.WIDTH;
        float h = Player.HEIGHT;

        if (y < 5) {
            this.transition(0, -1);
        }
        if (y > GameScreen.CAMERA_HEIGHT - w + 5) {
            this.transition(0, 1);
        }
        if (x < 5) {
            this.transition(-1, 0);
        }
        if (x > GameScreen.CAMERA_WIDTH - h + 5) {
            this.transition(1, 0);
        }
    }

    public void transition(int xa, int ya) {
        player.getPosition().x -= xa * GameScreen.CAMERA_WIDTH;
        player.getPosition().y -= ya * GameScreen.CAMERA_HEIGHT;

        if (ya != 0) {
            player.getPosition().y -= 10;
        }

        world = new World(32, 24, xa, ya, (int) player.getPosition().x, (int) (player.getPosition().y + ya * 5));
        player = world.getPlayer();
        controller = new WorldController(world);
    }

    private void drawBlocks(World world) {
        for (Block block : world.getBlocks()) {
            spriteBatch.draw(blockTexture, block.getPosition().x * ppuX, block.getPosition().y * ppuY, Block.SIZE * ppuX, Block.SIZE * ppuY);
        }
    }

    private void drawPlayer(World world) {
        // Get the texture.
        Texture texture = playerTextures.get(player.getPlayerImage());
        Color c = spriteBatch.getColor();
        // If they have been hit we want to flash their image.
        if (player.timeSinceHit < 2) {
            if (Math.sin((double) System.currentTimeMillis()) > 0) {
                // Set alpha to 0.3.
                spriteBatch.setColor(c.r, c.g, c.b, .3f);
            }
        }
        spriteBatch.draw(texture, player.getPosition().x * ppuX, player.getPosition().y * ppuY, Player.WIDTH * ppuX, Player.HEIGHT * ppuY);
        spriteBatch.setColor(c.r, c.g, c.b, 1f);
    }

    private void drawFlamers(World world) {
        for (Flamer flamer : world.getFlamers()) {
            spriteBatch.draw(flamerTextures.get(flamer.getFlamerImage()), flamer.getPosition().x * ppuX, flamer.getPosition().y * ppuY, Flamer.WIDTH * ppuX, Flamer.HEIGHT * ppuY);
        }
    }

    private void drawBullets(World world) {
        for (Bullet bullet : world.getBullets()) {
            spriteBatch.draw(bulletTexture, bullet.getPosition().x * ppuX, bullet.getPosition().y * ppuY, Bullet.WIDTH * ppuX, Bullet.HEIGHT * ppuY);
        }
    }
}
