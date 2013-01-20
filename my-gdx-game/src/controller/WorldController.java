package controller;

import java.util.HashMap;
import java.util.Map;

import lib.OverlapTester;
import model.Block;
import model.Bullet;
import model.Character;
import model.Character.State;
import model.Flamer;
import model.World;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class WorldController {

    enum Keys {
        LEFT, RIGHT, JUMP
    }

    private final World world;
    private final Character character;
    public static final float WORLD_SIZE = 10f;

    // Store the time we are at.
    public static float currentDelta;

    static Map<Keys, Boolean> keys = new HashMap<WorldController.Keys, Boolean>();
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.JUMP, false);
    }

    public WorldController(World world) {
        this.world = world;
        this.character = world.getCharacter();
    }

    public void update(float delta) {
        // Set the current time to static variable that can be accessed by other classes.
        currentDelta = delta;
        // Begin the gaming process.
        processInput();
        gravityDetection();
        collisionCharBlock();
        collisionCharFlamer();
        character.update(delta);
        // Loop through flamers and remove them if they are dead.
        Array<Flamer> arrFlamers = world.getFlamers();
        for (int i = 0; i < arrFlamers.size; i++) {
            collisionFlamerBlock(arrFlamers.get(i));
            collisionFlamerBullet(arrFlamers.get(i));
            if (arrFlamers.get(i).isDead()) {
                arrFlamers.removeIndex(i);
            } else {
                arrFlamers.get(i).update(delta);
            }
        }
        // Loop through bullets and remove them if they hit a block.
        Array<Bullet> arrBullets = world.getBullets();
        for (int i = 0; i < arrBullets.size; i++) {
            if (collisionBulletBlock(arrBullets.get(i))) {
                arrBullets.removeIndex(i);
            } else {
                arrBullets.get(i).update(delta);
            }
        }
    }

    private void processInput() {
        if (character.isdead == false) {
            if (keys.get(Keys.LEFT)) {
                character.setFacingLeft(true);
                character.setCharacterImage();
                character.setState(State.WALKING);
                character.getVelocity().x = -Character.SPEED;
            }
            if (keys.get(Keys.RIGHT)) {
                character.setFacingLeft(false);
                character.setCharacterImage();
                character.setState(State.WALKING);
                character.getVelocity().x = Character.SPEED;
            }
            if (keys.get(Keys.JUMP)) {
                character.setState(State.JUMPING);
            }
            if ((keys.get(Keys.LEFT) && keys.get(Keys.RIGHT)) ||
                    (!keys.get(Keys.LEFT) && !(keys.get(Keys.RIGHT)))) {
                character.setState(State.IDLE);
                // Horizontal speed is 0
                character.getVelocity().x = 0;
            }
        } else {
            character.getVelocity().x = 0;
            character.getVelocity().y = 0;
        }
    }

    private void gravityDetection() {
        // Assume we are falling for now.
        character.falling = true;
        // Use this as a temp rectangle.
        Rectangle tempRect = new Rectangle(character.getPosition().x,
                (float) (character.getPosition().y  - 0.05),
                Character.WIDTH,
                Character.HEIGHT);
        for (Block block : world.getBlocks()){
            if (OverlapTester.overlapRectangles(block.getBounds(), tempRect)) {
                character.falling = false;
                break;
            }
        }
        if (character.falling) {
            character.getVelocity().y = -Character.FALL_VELOCITY;
        } else {
            character.getVelocity().y = 0;
        }
    }

    private void collisionCharBlock() {
        // Store the characters x and y position.
        float x = character.getPosition().x;
        float y = character.getPosition().y;
        // Horizontal-left
        if (keys.get(Keys.LEFT)) {
            x = (float) (x - 0.05);
        }
        // Horizontal-right
        if (keys.get(Keys.RIGHT)) {
            x = (float) (x + 0.05);
        }
        // Vertical-ceiling
        if (keys.get(Keys.JUMP)) {
            y = (float) (y + 0.05);
        }
        Rectangle tempRect = new Rectangle(x, y, Character.WIDTH, Character.HEIGHT);
        // Boolean to store if the user can move.
        boolean canMove = true;
        // Loop through the blocks.
        for (Block block : world.getBlocks()) {
            if (OverlapTester.overlapRectangles(block.getBounds(), tempRect)) {
                canMove = false;
                break;
            }
        }
        if (canMove) { // If they can move, update.
            if (keys.get(Keys.LEFT)) {
                character.getVelocity().x = -Character.SPEED;
            }
            if (keys.get(Keys.RIGHT)) {
                character.getVelocity().x = Character.SPEED;
            }
            if (keys.get(Keys.JUMP)) {
                character.getVelocity().y = Character.JUMP_VELOCITY;
            }
        } else { // If they can't move, they hit something and should bounce back.
            if (keys.get(Keys.LEFT)) {
                character.getVelocity().x = Character.SPEED;
            }
            if (keys.get(Keys.RIGHT)) {
                character.getVelocity().x = -Character.SPEED;
            }
            if (keys.get(Keys.JUMP)) {
                character.getVelocity().y = -Character.JUMP_VELOCITY;
            }
        }
    }

    private void collisionCharFlamer() {
        // Store the characters x and y position.
        float x = character.getPosition().x;
        float y = character.getPosition().y;
        // Horizontal-left
        if (keys.get(Keys.LEFT)) {
            x = (float) (x - 0.05);
        }
        // Horizontal-right
        if (keys.get(Keys.RIGHT)) {
            x = (float) (x + 0.05);
        }
        // Vertical-ceiling
        if (keys.get(Keys.JUMP)) {
            y = (float) (y + 0.05);
        }
        Rectangle tempRect = new Rectangle(x, y, Character.WIDTH, Character.HEIGHT);
        // Boolean to store if the user has been hit.
        boolean isHit = false;
        // Loop through the flamers.
        for (Flamer flamer : world.getFlamers()) {
            if (OverlapTester.overlapRectangles(flamer.getBounds(), tempRect)) {
                isHit = true;
                break;
            }
        }
        // If they are hit then adjust the users health.
        if (isHit) {
            // If the character was hit more than 2 seconds ago, register it.
            if (character.timeSinceHit > 2) {
                character.hit();
            }
        }
    }

    public void collisionFlamerBlock(Flamer flamer) {
        // Store the flamers x and y position.
        float x = flamer.getPosition().x;
        float y = flamer.getPosition().y;
        // Horizontal-right
        if (flamer.facingLeft) {
            x = (float) (x - 0.05);
        } else {
            x = (float) (x + 0.05);
        }
        Rectangle tempRect = new Rectangle(x, y, Flamer.WIDTH, Flamer.HEIGHT);
        // Boolean to store if the flamer can move.
        boolean canMove = true;
        // Loop through the blocks.
        for (Block block : world.getBlocks()) {
            if (OverlapTester.overlapRectangles(block.getBounds(), tempRect)) {
                canMove = false;
                break;
            }
        }
        // Check he does not hit the end of the screen horizontally.
        if (tempRect.x < 0) {
            canMove = false;
        }
        // If they can move, update.
        if (canMove) {
            if (flamer.facingLeft) {
                flamer.getVelocity().x = -Flamer.SPEED;
            } else {
                flamer.getVelocity().x = Flamer.SPEED;
            }
        } else {
            // Change the direction he is facing.
            if (flamer.facingLeft) {
                flamer.facingLeft = false;
            } else {
                flamer.facingLeft = true;
            }
            flamer.getVelocity().x = 0;
            flamer.getVelocity().y = 0;
        }
    }

    public void collisionFlamerBullet(Flamer flamer) {
        // Store the flamers x and y position.
        float x = flamer.getPosition().x;
        float y = flamer.getPosition().y;
        // Horizontal-right
        if (flamer.facingLeft) {
            x = (float) (x - 0.05);
        } else {
            x = (float) (x + 0.05);
        }
        Rectangle tempRect = new Rectangle(x, y, Flamer.WIDTH, Flamer.HEIGHT);
        // Store if the flamer is hit
        boolean isHit = false;
        // Loop through the bullets.
        Array<Bullet> arrBullets = world.getBullets();
        for (int i = 0; i < arrBullets.size; i++) {
            if (OverlapTester.overlapRectangles(arrBullets.get(i).getBounds(), tempRect)) {
                flamer.hit();
                isHit = true;
                // Remove the bullet
                arrBullets.removeIndex(i);
                break;
            }
        }
        // If they can get hit, update.
        if (isHit) {
            // If the character was hit more than 2 seconds ago, register it.
            if (flamer.timeSinceHit > 2) {
                flamer.hit();
            }
        }
    }

    public boolean collisionBulletBlock(Bullet bullet) {
        // Store the characters x and y position.
        float x = bullet.getPosition().x;
        float y = bullet.getPosition().y;
        // Horizontal-right
        if (bullet.facingLeft) {
            x = (float) (x - 0.05);
        } else {
            x = (float) (x + 0.05);
        }
        Rectangle tempRect = new Rectangle(x, y, Bullet.WIDTH, Bullet.HEIGHT);
        // Store if the flamer is hit
        boolean isHit = false;
        // Loop through the bullets.
        Array<Block> arrBlocks = world.getBlocks();
        for (int i = 0; i < arrBlocks.size; i++) {
            if (OverlapTester.overlapRectangles(arrBlocks.get(i).getBounds(), tempRect)) {
                isHit = true;
                break;
            }
        }

        return isHit;
    }

    public void leftPressed() {
        keys.put(Keys.LEFT, true);
    }

    public void rightPressed() {
        keys.put(Keys.RIGHT, true);
    }

    public void jumpPressed() {
        keys.put(Keys.JUMP, true);
    }

    public void firePressed() {
        character.shootBullet();
    }

    public void leftReleased() {
        keys.put(Keys.LEFT, false);
    }

    public void rightReleased() {
        keys.put(Keys.RIGHT, false);
    }

    public void jumpReleased() {
        keys.put(Keys.JUMP, false);
    }
}
