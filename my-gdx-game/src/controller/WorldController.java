package controller;

import java.util.HashMap;
import java.util.Map;

import screens.GameScreen;

import lib.Sound;
import model.Player;
import model.Player.State;
import model.Flamer;
import model.World;
import model.BoundObject;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class WorldController {

    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    private World world;
    private final Player player;
    public static final float WORLD_SIZE = 10f;

    // The variables representing the direction the character went off screen.
    public final int DOWN = 1;
    public final int UP = 2;
    public final int LEFT = 3;
    public final int RIGHT = 4;

    // Store the time from the last frame.
    public float delta;
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
        // Set the time from the last frame to a variable that can be accessed by other functions.
        this.delta = delta;
        // Begin the gaming process.
        processInput();
        updatePlayer();
        updateFlamers();
        updateBullets();
    }

    private void processInput() {
        if (player.isDead() == false) {
            if (keys.get(Keys.LEFT)) {
                player.facingLeft = true;
                if (!player.isJumping()) {
                    player.setState(State.WALKING);
                }
                player.getVelocity().x = -Player.SPEED;
            } else if (keys.get(Keys.RIGHT)) {
                player.facingLeft = false;
                if (!player.isJumping()) {
                    player.setState(State.WALKING);
                }
                player.getVelocity().x = Player.SPEED;
            } else {
                player.setState(State.IDLE);
                player.getVelocity().x = 0;
            }
            if (keys.get(Keys.JUMP)) {
                if (!player.isJumping()) {
                    Sound.playerJump.play();
                    player.setState(State.JUMPING);
                    player.getVelocity().y = Player.JUMP_VELOCITY;
                }
            }
            if (keys.get(Keys.FIRE)) {
                player.shootBullet();
            }
        } else {
            player.getVelocity().x = 0;
            player.getVelocity().y = 0;
        }
    }

    private void updatePlayer() {
        gravityDetection();
        // Before we change the X velocity store it as we want to restore if we hit a flamer.
        float velocityX = player.getVelocity().x;
        float velocityY = player.getVelocity().y;
        if (collisionDetection(player, world.getFlamers(), false)) {
            // The player should be allowed to move through the flamer, so restore velocity.
            player.getVelocity().x = velocityX;
            player.getVelocity().y = velocityY;
            player.hit();
        }
        collisionDetection(player, world.getBlocks(), false);
        player.update(delta);
        movingOffScreen();
    }

    private void updateFlamers() {
        // Loop through flamers and remove them if they are dead.
        Array<BoundObject> arrFlamers = world.getFlamers();
        for (int i = 0; i < arrFlamers.size; i++) {
            BoundObject flamer = arrFlamers.get(i);
            if (!flamer.facingLeft) {
                flamer.getVelocity().x = Flamer.SPEED;
            } else {
                flamer.getVelocity().x = -Flamer.SPEED;
            }
            if (collisionDetection(flamer, world.getBlocks(), false)) {
                flamer.facingLeft = !flamer.facingLeft;
            } else {
                flamer.update(delta);
            }
            if (collisionDetection(flamer, world.getBullets(), true)) {
                flamer.hit();
                if (flamer.isDead()) {
                    arrFlamers.removeIndex(i);
                }
            }
        }
    }

    private void updateBullets() {
        // Loop through bullets and remove them if they hit a block.
        Array<BoundObject> arrBullets = world.getBullets();
        for (int i = 0; i < arrBullets.size; i++) {
            if (collisionDetection(arrBullets.get(i), world.getBlocks(), false)) {
                arrBullets.removeIndex(i);
            } else {
                arrBullets.get(i).update(delta);
            }
        }
    }

    private void gravityDetection() {
        // Assume we are falling for now.
        falling = true;
        // Create a temporary rectangle used to detect collision.
        Rectangle tempRect = player.getBounds();
        tempRect.y -= Player.GRAVITY * delta;
        for (BoundObject block : world.getBlocks()) {
            if (tempRect.overlaps(block.getBounds())) {
                falling = false;
                break;
            }
        }
        if (falling) {
            player.getVelocity().y -= Player.GRAVITY * delta;
        } else {
            player.setState(State.WALKING);
        }
    }

    private boolean collisionDetection(BoundObject object, Array<BoundObject> items,
            Boolean removeHitItem) {
        // Boolean to return if there was contact.
        boolean contact = false;
        // Create a temporary rectangle used to detect collision.
        Rectangle tempRect = object.getBounds();
        // Alter the temporary rectangle's bounds in the X axis.
        tempRect.x += object.getVelocity().x * delta;
        for (int i = 0; i < items.size; i++) {
            BoundObject bo = items.get(i);
            if (tempRect.overlaps(bo.getBounds())) {
                if (removeHitItem) {
                    items.removeIndex(i);
                }
                contact = true;
                object.getVelocity().x = 0;
                break;
            }
        }
        // Set the X axis back to the object's position.
        tempRect.x = object.getBounds().x;
        // Alter the temporary rectangle's bounds in the Y axis.
        tempRect.y += object.getVelocity().y * delta;
        for (int i = 0; i < items.size; i++) {
            BoundObject bo = items.get(i);
            if (tempRect.overlaps(bo.getBounds())) {
                if (removeHitItem) {
                    items.removeIndex(i);
                }
                contact = true;
                object.getVelocity().y = 0;
                break;
            }
        }

        return contact;
    }

    public void movingOffScreen() {
        // Check here if we need to start transition between screens.
        float x = player.getPosition().x;
        float y = player.getPosition().y;

        // Check if the player has moved off screen and that their velocity is
        // heading in that direction so they do not loop between two screens.
        if ((y < 5) && (player.getVelocity().y < 0)) {
            this.transition(DOWN);
        } else if ((y > GameScreen.CAMERA_HEIGHT - (player.height + 5))
                && (player.getVelocity().y > 0)) {
            this.transition(UP);
        } else if ((x < 5) && (player.getVelocity().x < 0)) {
            this.transition(LEFT);
        } else if ((x > GameScreen.CAMERA_WIDTH - (player.width + 5))
                && (player.getVelocity().x > 0)) {
            this.transition(RIGHT);
        }
    }

    public void transition(int direction) {
        int xo = 0;
        int yo = 0;

        switch (direction) {
            case UP :
                yo = -1;
                player.getPosition().y = player.height;
                break;
            case DOWN :
                yo = 1;
                player.getPosition().y = GameScreen.CAMERA_HEIGHT - player.height;
                break;
            case LEFT :
                xo = -1;
                player.getPosition().x = GameScreen.CAMERA_WIDTH - player.width;
                break;
            case RIGHT :
                xo = 1;
                player.getPosition().x = player.width;
        }

        // Update the location of where we are now on levels.png.
        world.topLeftPixelX = world.topLeftPixelX + (World.widthInPixels * xo);
        world.topLeftPixelY = world.topLeftPixelY + (World.heightInPixels * yo);
        world.update();
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
