package model;

import lib.Sound;

import com.badlogic.gdx.math.Vector2;

public class Player extends BoundObject {

    public enum State {
        IDLE, WALKING, JUMPING, DYING
    }

    private World world;

    public static final float SPEED = 60f; // Units per second.
    public static final float JUMP_VELOCITY = 70f;
    public static final float GRAVITY = 70f;

    public int health = 3;
    public float hitFrequency = 2; // Determines how quickly they can be hit.
    public float timeSinceHit = hitFrequency; // Set this to the hit frequency so they don't spawn flashing.
    public float timeSinceLastShot = 0;
    public float shotFrequency = 1; // How often they are allowed to shoot.

    public State state = State.IDLE;
    public boolean isSpawned = false;

    // Keep track of which image we last used to display the player.
    String playerImage = "walking_left_1";

    public Player(Vector2 position, World world) {
        super(position, 15f, 25f);
        this.world = world;
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public void update(float delta) {
        super.update(delta);
        timeSinceHit += delta;
        timeSinceLastShot += delta;
    }

    public void hit() {
        if (timeSinceHit > hitFrequency) {
            if (health >= 0){
                health--;
            }
            // Reset the count.
            timeSinceHit = 0;
        }
    }

    public boolean isDead() {
        if (health <= 0) {
            return true;
        }

        return false;
    }

    public boolean isJumping() {
        return this.state == State.JUMPING;
    }

    public String getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage() {
        if (facingLeft) {
            if (playerImage == "walking_left_1") {
                playerImage = "walking_left_2";
            } else {
                playerImage = "walking_left_1";
            }
        } else {
            if (playerImage == "walking_right_1") {
                playerImage = "walking_right_2";
            } else {
                playerImage = "walking_right_1";
            }
        }
    }

    public void shootBullet() {
        // Check that they are allowed to shoot.
        if (timeSinceLastShot < shotFrequency) {
            return;
        }
        timeSinceLastShot = 0;
        Vector2 bulletPosition = new Vector2();
        if (facingLeft) {
            bulletPosition.x = this.position.x - 2f;
        } else {
            bulletPosition.x = this.position.x + 2f;
        }
        bulletPosition.y = this.position.y + 6f;
        Bullet bullet = new Bullet(bulletPosition, facingLeft);
        world.addBullet(bullet);
        Sound.playerBullet.play();
    }
}

