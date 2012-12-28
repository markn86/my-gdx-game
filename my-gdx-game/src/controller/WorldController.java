package controller;

import java.util.HashMap;
import java.util.Map;

import lib.OverlapTester;
import model.Block;
import model.Character;
import model.Character.State;
import model.World;

import com.badlogic.gdx.math.Rectangle;

public class WorldController {

    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    private final World world;
    private final Character character;
    public static final float WORLD_SIZE = 10f;

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
        processInput();
        gravityDetection();
        collisionDetectionBlocks();
        character.update(delta);
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
                // Acceleration is 0 on the x
                character.getAcceleration().x = 0;
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
                character.getBounds().width,
                character.getBounds().height);
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

    private void collisionDetectionBlocks() {
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
        Rectangle tempRect = new Rectangle(x, y, character.getBounds().width, character.getBounds().height);
        // Boolean to store if the user can move.
        boolean canMove = true;
        // Loop through the blocks.
        for (Block block : world.getBlocks()) {
            if (OverlapTester.overlapRectangles(block.getBounds(), tempRect)) {
                canMove = false;
                break;
            }
        }
        // If they can move, update.
        if (canMove) {
            if (keys.get(Keys.LEFT)) {
                character.getVelocity().x = -Character.SPEED;
            }
            if (keys.get(Keys.RIGHT)) {
                character.getVelocity().x = Character.SPEED;
            }
            if (keys.get(Keys.JUMP)) {
                character.getVelocity().y = Character.JUMP_VELOCITY;
            }
        } else {
            character.getVelocity().x = 0;
            character.getVelocity().y = 0;
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
