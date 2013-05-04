package model;

import screens.GameScreen;
import lib.Assets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class World {

    public static final int widthInPixels = 32;
    public static final int heightInPixels = 24;

    // Store the blocks making up the world.
    Array<Block> blocks = new Array<Block>();

    // The player.
    Player player;

    // The flamer enemies.
    Array<Flamer> flamers;

    // The bullets.
    Array<Bullet> bullets = new Array<Bullet>();

    // Store the top left pixels of where we are on levels.png.
    public int topLeftPixelX = 0;
    public int topLeftPixelY = 0;

    public World() {
        player = new Player(new Vector2(0, 0), this);
        this.update();
    }

    public void update() {
        // Set these to empty variables to begin, so levels do no overlap.
        blocks = new Array<Block>();
        flamers = new Array<Flamer>();
        bullets = new Array<Bullet>();
        // The Y location we are going to generate items on the screen.
        int screenY = -1;
        // Start looping through from the current top position of levels.png.
        for (int y = topLeftPixelY; y < topLeftPixelY + heightInPixels; y++) {
            screenY++;
            // The X location we are going to generate items on the screen.
            int screenX = -1;
            // Now loop through the current left position of levels.png and assess the colour of pixel at that location.
            for (int x = topLeftPixelX; x < topLeftPixelX + widthInPixels; x++) {
                screenX++;
                int col = (Assets.level.getPixel(x, y) & 0xffffff00) >>> 8;
                switch (col) {
                    case 0x914d2a: // Brown block.
                        blocks.add(new Block(new Vector2(screenX * 10, GameScreen.CAMERA_HEIGHT - (screenY * 10) - 10)));
                        break;
                    case 0x0000ff: // The player.
                        // Only change this if player is not currently being rendered.
                        if (!player.isSpawned) {
                            player.getPosition().x = screenX * 10;
                            player.getPosition().y = GameScreen.CAMERA_HEIGHT - (screenY * 10);
                            player.isSpawned = true;
                        }
                        break;
                    case 0x5f5f5f: // The flamer.
                        flamers.add(new Flamer(new Vector2(screenX * 10, GameScreen.CAMERA_HEIGHT - (screenY * 10))));
                        break;
                }
            }
        }
    }

    public Array<Block> getBlocks() {
        return this.blocks;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Array<Flamer> getFlamers() {
        return this.flamers;
    }

    public Array<Bullet> getBullets() {
        return this.bullets;
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }
}
