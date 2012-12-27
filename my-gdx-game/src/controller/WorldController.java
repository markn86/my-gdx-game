package controller;

import java.util.HashMap;
import java.util.Map;

import lib.OverlapTester;
import model.Block;
import model.Character;
import model.Character.State;
import model.World;

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
        gravityDetection(delta);
        collisionDetectionBlocks(delta);
        character.update(delta);
    }

    private void gravityDetection(float delta) {
        // Assume we are falling for now.
        character.falling = true;
        // Use this as a temp character.
        System.out.println("Char 1" + character.getPosition());
        Character tempChar = (Character) character.clone();
        System.out.println("tempChar 1" + tempChar.getPosition());
        tempChar.getVelocity().y = -Character.FALL_VELOCITY;
        tempChar.update(delta);
        System.out.println("Char 2" + character.getPosition());
        System.out.println("tempChar 1" + tempChar.getPosition());
        for (Block block: world.getBlocks()){
            if (OverlapTester.overlapRectangles(block.getBounds(), tempChar.getBounds())) {
                character.falling = false;
                break;
            }
        }
        if (keys.get(Keys.JUMP)){
            if (character.isdead) {
                character.getVelocity().y = Character.JUMP_VELOCITY;
            }
        } else {
            if (character.falling) {
                character.getVelocity().y = -Character.FALL_VELOCITY;
            } else {
                character.getVelocity().y = 0;
            }
        }
    }

    private void collisionDetectionBlocks(float delta) {
        // Use this as a temp character.
        Character tempChar = new Character(character.getPosition());
        // Horizontal-left
        if (keys.get(Keys.LEFT)) {
            tempChar.getVelocity().x = -Character.SPEED;
            tempChar.update(delta);
        }
        // Horizontal-right
        if (keys.get(Keys.RIGHT)) {
            tempChar.getVelocity().x = Character.SPEED;
            tempChar.update(delta);
        }
        // Vertical-ceiling
        if (keys.get(Keys.JUMP)) {
            tempChar.getVelocity().y = Character.JUMP_VELOCITY;
            tempChar.update(delta);
        }
        // Boolean to store if the user can move.
        boolean canMove = true;
        // Loop through the blocks.
        for (Block block: world.getBlocks()) {
            if (OverlapTester.overlapRectangles(block.getBounds(), tempChar.getBounds())) {
                canMove = false;
                break;
            }
        }
        // If they can move, update.
        if (canMove) {
            character.setPosition(tempChar.getPosition());
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
