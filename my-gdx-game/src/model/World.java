package model;

import lib.Assets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class World {

    // Store the blocks making up the world.
    Array<Block> blocks = new Array<Block>();

    // The player.
    Player player;

    // The flamer enemies.
    Array<Flamer> flamers = new Array<Flamer>();

    // The bullets.
    Array<Bullet> bullets = new Array<Bullet>();

    public World(int w, int h, int xo, int yo, int xSpawn, int ySpawn) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int col = (Assets.level.getPixel(x + xo * 31, y + yo * 23) & 0xffffff00) >>> 8;
                switch (col) {
                    case 0x914d2a: // Brown block.
                        blocks.add(new Block(new Vector2(x * 10, (h * 10) - (y * 10) - 10)));
                        break;
                    case 0x0000ff: // The player.
                        // Only change this if player is not currently being rendered.
                        if (xSpawn == 0 && ySpawn == 0) {
                            xSpawn = x * 10;
                            ySpawn = (h * 10) - (y * 10);
                        }
                        break;
                    case 0x5f5f5f: // The flamer.
                        flamers.add(new Flamer(new Vector2(x * 10, (h * 10) - (y * 10))));
                        break;
                }
            }
        }
        player = new Player(new Vector2(xSpawn, ySpawn), this);
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
