package model;

import lib.Sound;

import com.badlogic.gdx.math.Vector2;

public class Player extends BoundObject {

    private World world;

    public int health = 3;
    public float hitFrequency = 2; // Determines how quickly they can be hit.
    public float timeSinceHit = hitFrequency; // Set this to the hit frequency so they don't spawn flashing.
    public float timeSinceLastShot = 0;
    public float shotFrequency = 1; // How often they are allowed to shoot.

    public boolean isSpawned = false;

    public Player(Vector2 position, World world) {
        super(position, 15f, 25f);
        // Override the gravity variable set.
        this.gravity = 70f;
        this.world = world;
    }

    public State getState() {
        return state;
    }

    public void update(float delta) {
        // Only update this if he is not idle.
        if (this.state != State.IDLE) {
            movingStateTime += delta;
        }
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

