package controller;

import java.util.HashMap;
import java.util.Map;

import lib.OverlapTester;
import lib.Sound;
import model.Block;
import model.Bullet;
import model.Player;
import model.Player.State;
import model.Flamer;
import model.World;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class WorldController {

    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    private final World world;
    private final Player player;
    public static final float WORLD_SIZE = 10f;

    // Store the time we are at.
    public static float currentDelta;
    boolean falling = true;

    static Map<Keys, Boolean> keys = new HashMap<WorldController.Keys, Boolean>();
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.JUMP, false);
        keys.put(Keys.FIRE, false);
    }

    public WorldController(World world) {
        this.world = world;
        this.player = world.getPlayer();
    }

    public void update(float delta) {
        // Set the current time to static variable that can be accessed by other classes.
        currentDelta = delta;
        // Begin the gaming process.
        processInput();
        gravityDetection();
        collisionCharBlock();
        collisionCharFlamer();
        player.update(delta);
        if (keys.get(Keys.FIRE)) {
            player.shootBullet();
        }
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
        if (player.isDead() == false) {
            if (keys.get(Keys.LEFT)) {
                player.setFacingLeft(true);
                player.setPlayerImage();
                if (!player.isJumping()) {
                    player.setState(State.WALKING);
                }
                player.getVelocity().x = -Player.SPEED;
            }
            if (keys.get(Keys.RIGHT)) {
                player.setFacingLeft(false);
                player.setPlayerImage();
                if (!player.isJumping()) {
                    player.setState(State.WALKING);
                }
                player.getVelocity().x = Player.SPEED;
            }
            if (keys.get(Keys.JUMP)) {
                if (!player.isJumping()) {
                    Sound.playerJump.play();
                    player.setState(State.JUMPING);
                    player.getVelocity().y = Player.JUMP_VELOCITY;
                    player.getPosition().y += player.getVelocity().y * currentDelta;
                }
            }
            if ((keys.get(Keys.LEFT) && keys.get(Keys.RIGHT)) ||
                    (!keys.get(Keys.LEFT) && !(keys.get(Keys.RIGHT)))) {
                if (!player.isJumping()) {
                    player.setState(State.IDLE);
                }
                // Horizontal speed is 0
                player.getVelocity().x = 0;
            }
        } else {
            player.getVelocity().x = 0;
            player.getVelocity().y = 0;
        }
    }

    private void gravityDetection() {
        // Assume we are falling for now.
        falling = true;
        // Use this as a temp rectangle.
        Rectangle tempRect = new Rectangle(player.getPosition().x,
                (float) (player.getPosition().y - 1),
                Player.WIDTH,
                Player.HEIGHT);
        for (Block block : world.getBlocks()){
            if (OverlapTester.overlapRectangles(block.getBounds(), tempRect)) {
                falling = false;
                break;
            }
        }
        if (falling) {
            player.getVelocity().y -= Player.GRAVITY * currentDelta;
        } else { // Set state back to walking as he has landed on the ground.
            player.setState(State.WALKING);
            player.getVelocity().y = 0;
        }
    }

    private void collisionCharBlock() {
        // Store the players x and y position.
        float x = player.getPosition().x;
        float y = player.getPosition().y;
        // Horizontal-left
        if (keys.get(Keys.LEFT)) {
            x = (float) (x - 1);
        }
        // Horizontal-right
        if (keys.get(Keys.RIGHT)) {
            x = (float) (x + 1);
        }
        // Vertical-ceiling
        if (player.isJumping()) {
            y = (float) (y + 1);
        }
        Rectangle tempRect = new Rectangle(x, y, Player.WIDTH, Player.HEIGHT);
        // Boolean to store if the user can move.
        boolean canMove = true;
        // Loop through the blocks.
        for (Block block : world.getBlocks()) {
            if (OverlapTester.overlapRectangles(block.getBounds(), tempRect)) {
                canMove = false;
                break;
            }
        }
        if (!canMove) { // Can't move, set x to 0 and y to 0, unless jumping, then decrease.
            player.getVelocity().x = 0;
            player.getVelocity().y = 0;
            if (player.isJumping()) {
                player.getVelocity().y = -Player.GRAVITY;
            }
        }
    }

    private void collisionCharFlamer() {
        // Store the players x and y position.
        float x = player.getPosition().x;
        float y = player.getPosition().y;
        // Horizontal-left
        if (keys.get(Keys.LEFT)) {
            x = (float) (x - 1);
        }
        // Horizontal-right
        if (keys.get(Keys.RIGHT)) {
            x = (float) (x + 1);
        }
        // Vertical-ceiling
        if (keys.get(Keys.JUMP)) {
            y = (float) (y + 1);
        }
        Rectangle tempRect = new Rectangle(x, y, Player.WIDTH, Player.HEIGHT);
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
            // If the player was hit more than 2 seconds ago, register it.
            if (player.timeSinceHit > 2) {
                player.hit();
            }
        }
    }

    public void collisionFlamerBlock(Flamer flamer) {
        // Store the flamers x and y position.
        float x = flamer.getPosition().x;
        float y = flamer.getPosition().y;
        // Horizontal-right
        if (flamer.facingLeft) {
            x = (float) (x - 1);
        } else {
            x = (float) (x + 1);
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
            x = (float) (x - 1);
        } else {
            x = (float) (x + 1);
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
            // If the flamer was hit more than 2 seconds ago, register it.
            if (flamer.timeSinceHit > 2) {
                flamer.hit();
            }
        }
    }

    public boolean collisionBulletBlock(Bullet bullet) {
        // Store the players x and y position.
        float x = bullet.getPosition().x;
        float y = bullet.getPosition().y;
        // Horizontal-right
        if (bullet.facingLeft) {
            x = (float) (x - 1);
        } else {
            x = (float) (x + 1);
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

    public static void leftPressed() {
        keys.put(Keys.LEFT, true);
    }

    public static void rightPressed() {
        keys.put(Keys.RIGHT, true);
    }

    public static void jumpPressed() {
        keys.put(Keys.JUMP, true);
    }

    public static void firePressed() {
        keys.put(Keys.FIRE, true);
    }

    public static void leftReleased() {
        keys.put(Keys.LEFT, false);
    }

    public static void rightReleased() {
        keys.put(Keys.RIGHT, false);
    }

    public static void jumpReleased() {
        keys.put(Keys.JUMP, false);
    }

    public static void fireReleased() {
        keys.put(Keys.FIRE, false);
    }
}
