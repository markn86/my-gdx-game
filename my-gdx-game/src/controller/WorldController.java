package controller;

import java.util.HashMap;
import java.util.Map;

import lib.OverlapTester;
import model.Block;
import model.Character;
import model.Character.State;
import model.Flamer;
import model.World;

import com.badlogic.gdx.math.Rectangle;

public class WorldController {

    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
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
        keys.put(Keys.FIRE, false);
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
        collisionDetection();
        character.update(delta);
        for (Flamer flamer : world.getFlamers()) {
            collisionDetectionFlamers(flamer);
            flamer.update(delta);
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

    private void collisionDetection() {
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
        // Boolean to store if the user has been hit.
        boolean isHit = false;
        // Loop through the blocks.
        for (Block block : world.getBlocks()) {
            if (OverlapTester.overlapRectangles(block.getBounds(), tempRect)) {
                canMove = false;
                break;
            }
        }
        // Loop through the flamers.
        for (Flamer block : world.getFlamers()) {
            if (OverlapTester.overlapRectangles(block.getBounds(), tempRect)) {
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
        } else if (canMove) { // If they can move, update.
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

    public void collisionDetectionFlamers(Flamer flamer) {
        // Store the characters x and y position.
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
        keys.put(Keys.FIRE, true);
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

    public void fireReleased() {
        keys.put(Keys.FIRE, false);
    }
}
