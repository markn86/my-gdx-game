package controller;

import java.util.HashMap;
import java.util.Map;

import model.Block;
import model.World;
import model.Character;
import model.Character.State;

public class WorldController {

    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    private World world;
    private Character character;
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

    private void gravityDetection() {
        character.falling = true;
        for (Block block: world.getBlocks()){
            if (character.getPosition().y  >= block.getPosition().y && character.getPosition().y <= block.getPosition().y + 1 &&
                    character.getPosition().x + (Character.WIDTH / 2) >= block.getPosition().x && character.getPosition().x
                    + Character.WIDTH / 2 < block.getPosition().x + 1) {
                character.falling = false;
                break;
            }
        }
        if (character.falling == true) {
            character.getVelocity().y = -Character.FALL_VELOCITY;
        } else {
            if (keys.get(Keys.JUMP)){
                if (character.isdead == false) {
                    character.getVelocity().y = Character.JUMP_VELOCITY;
                }
            } else {
                character.getVelocity().y = 0;
            }
        }
    }

    private void collisionDetectionBlocks() {
        for (Block block: world.getBlocks()) {
            // Horizontal-right
            if (keys.get(Keys.LEFT)){
                if (character.getPosition().x + Character.WIDTH/2 >= block.getPosition().x + 0.20
                        && character.getPosition().x + Character.WIDTH / 2 <= (block.getPosition().x + 1.50)
                        && character.getPosition().y + Character.HEIGHT / 2 >= block.getPosition().y
                        && character.getPosition().y + Character.HEIGHT / 2 <= (block.getPosition().y + 1)) {
                    character.getVelocity().x = Character.SPEED;
                }
            }
            // Horizontal-left
            if (keys.get(Keys.RIGHT)){
                if (character.getPosition().x + Character.WIDTH / 2 >= (block.getPosition().x - 0.7)
                        && character.getPosition().x + Character.WIDTH / 2 <= (block.getPosition().x + 0.5)
                        && character.getPosition().y + Character.HEIGHT / 2 >= (block.getPosition().y)
                        && character.getPosition().y + Character.HEIGHT / 2 <= (block.getPosition().y + 1)) {
                    character.getVelocity().x = -Character.SPEED;
                }
            }
            // Vertical-ceiling
            if (keys.get(Keys.JUMP)){
                if (character.getPosition().y + 1 >= block.getPosition().y
                        && character.getPosition().y + 1 <= block.getPosition().y + 1
                        && character.getPosition().x + Character.WIDTH / 2 >= block.getPosition().x
                        && character.getPosition().x + Character.WIDTH / 2 < block.getPosition().x + 1) {
                    character.getVelocity().y = -Character.SPEED;
                }
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
