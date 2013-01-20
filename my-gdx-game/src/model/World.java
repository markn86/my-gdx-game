package model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class World {

    // Store the blocks making up the world.
    Array<Block> blocks = new Array<Block>();

    // The interactive images.
    InteractiveImage leftArrow;
    InteractiveImage rightArrow;
    InteractiveImage jumpIcon;
    InteractiveImage fireIcon;

    // The character.
    Character character;

    // The flamer enemies.
    Array<Flamer> flamers = new Array<Flamer>();

    // The bullets.
    Array<Bullet> bullets = new Array<Bullet>();

    public World() {
        createFirstWorld();
    }

    public void createFirstWorld() {
        character = new Character(new Vector2(7, 2), this);
        flamers.add(new Flamer(new Vector2(1, 1)));

        for (int i = 0; i < 10; i++) {
            blocks.add(new Block(new Vector2(i, 0)));
            blocks.add(new Block(new Vector2(i, 6)));
            if (i > 2) {
                blocks.add(new Block(new Vector2(i, 1)));
            }
        }

        blocks.add(new Block(new Vector2(9, 2)));
        blocks.add(new Block(new Vector2(9, 3)));
        blocks.add(new Block(new Vector2(9, 4)));
        blocks.add(new Block(new Vector2(9, 5)));

        blocks.add(new Block(new Vector2(6, 3)));
        blocks.add(new Block(new Vector2(6, 4)));
        blocks.add(new Block(new Vector2(6, 5)));

        addInteractiveImages();
    }

    public Array<Block> getBlocks() {
        return this.blocks;
    }

    public Character getCharacter() {
        return this.character;
    }

    public Array<Flamer> getFlamers() {
        return this.flamers;
    }

    public Array<Bullet> getBullets() {
        return this.bullets;
    }

    public InteractiveImage getInteractiveImage(String name) {
        if (name == "leftArrow") {
            return leftArrow;
        } else if (name == "rightArrow") {
            return rightArrow;
        } else if (name == "jumpIcon") {
            return jumpIcon;
        } else if (name == "fireIcon") {
            return fireIcon;
        }

        // Should never get here.
        return rightArrow;
    }

    private void addInteractiveImages() {
        leftArrow = new InteractiveImage(new Texture(Gdx.files.internal("images/left_arrow.png")), new Vector2(0, 0));
        rightArrow = new InteractiveImage(new Texture(Gdx.files.internal("images/right_arrow.png")), new Vector2(1, 0));
        jumpIcon = new InteractiveImage(new Texture(Gdx.files.internal("images/jump.png")), new Vector2(8, 0));
        fireIcon = new InteractiveImage(new Texture(Gdx.files.internal("images/fire_button.png")), new Vector2(9, 0));
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }
}
