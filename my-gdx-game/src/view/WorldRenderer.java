package view;

import screens.GameScreen;

import lib.Assets;
import model.Bullet;
import model.Flamer;
import model.Block;
import model.InteractiveImage;
import model.Player;
import model.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldRenderer {

    // The world.
    private World world;

    // The player.
    Player player;

    public static SpriteBatch spriteBatch;
    private int width;
    private int height;
    private float ppuX; // Pixels per unit on the X axis.
    private float ppuY; // Pixels per unit on the Y axis.

    private GameScreen gameScreen;

    public WorldRenderer(World world, GameScreen gameScreen) {
        this.world = world;
        this.gameScreen = gameScreen;

        player = world.getPlayer();
        spriteBatch = new SpriteBatch();
    }

    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
        ppuX = width / GameScreen.CAMERA_WIDTH;
        ppuY = height / GameScreen.CAMERA_HEIGHT;
    }

    public void render(float delta) {
        spriteBatch.begin();

        // Draw the elements.
        drawBlocks(world);
        drawPlayer(world);
        drawFlamers(world);
        drawBullets(world);

        // Draw the non-moving images.
        drawInteractiveImages();
        drawHearts();

        spriteBatch.end();
    }

    private void drawBlocks(World world) {
        for (Block block : world.getBlocks()) {
            spriteBatch.draw(Assets.blockTexture, block.getPosition().x * ppuX, block.getPosition().y * ppuY, Block.SIZE * ppuX, Block.SIZE * ppuY);
        }
    }

    private void drawPlayer(World world) {
        if (player.isSpawned) {
            // Get the texture.
            Texture texture = Assets.playerTextures.get(player.getPlayerImage());
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
    }

    private void drawFlamers(World world) {
        for (Flamer flamer : world.getFlamers()) {
            spriteBatch.draw(Assets.flamerTextures.get(flamer.getFlamerImage()), flamer.getPosition().x * ppuX, flamer.getPosition().y * ppuY, Flamer.WIDTH * ppuX, Flamer.HEIGHT * ppuY);
        }
    }

    private void drawBullets(World world) {
        for (Bullet bullet : world.getBullets()) {
            spriteBatch.draw(Assets.bulletTexture, bullet.getPosition().x * ppuX, bullet.getPosition().y * ppuY, Bullet.WIDTH * ppuX, Bullet.HEIGHT * ppuY);
        }
    }

    private void drawInteractiveImages() {
        InteractiveImage leftArrow = gameScreen.leftArrow;
        InteractiveImage rightArrow = gameScreen.rightArrow;
        InteractiveImage jumpIcon = gameScreen.jumpIcon;
        InteractiveImage fireIcon = gameScreen.fireIcon;

        spriteBatch.draw(leftArrow.getTexture(), leftArrow.getPosition().x * ppuX, leftArrow.getPosition().y * ppuY, InteractiveImage.SIZE * ppuX, InteractiveImage.SIZE * ppuY);
        spriteBatch.draw(rightArrow.getTexture(), rightArrow.getPosition().x * ppuX, rightArrow.getPosition().y * ppuY, InteractiveImage.SIZE * ppuX, InteractiveImage.SIZE * ppuY);
        spriteBatch.draw(jumpIcon.getTexture(), jumpIcon.getPosition().x * ppuX, jumpIcon.getPosition().y * ppuY, InteractiveImage.SIZE * ppuX, InteractiveImage.SIZE * ppuY);
        spriteBatch.draw(fireIcon.getTexture(), fireIcon.getPosition().x * ppuX, fireIcon.getPosition().y * ppuY, InteractiveImage.SIZE * ppuX, InteractiveImage.SIZE * ppuY);
    }

    private void drawHearts() {
        // Get the players health.
        Player player = world.getPlayer();
        int health = player.health;
        // Draw the full hearts.
        for (int i = 0; i < health; i++) {
            spriteBatch.draw(Assets.heartTextures.get("full"), (i * 15f) * ppuX, (GameScreen.CAMERA_HEIGHT - 18) * ppuY, 15f * ppuX, 15f * ppuY);
        }
        // Now draw empty hearts.
        for (int i = health; i < 3; i++) {
            spriteBatch.draw(Assets.heartTextures.get("empty"), (i * 15f) * ppuX, (GameScreen.CAMERA_HEIGHT - 18) * ppuY, 15f * ppuX, 15f * ppuY);
        }
    }
}
