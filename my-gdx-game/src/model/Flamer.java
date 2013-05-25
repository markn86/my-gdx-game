package model;

import com.badlogic.gdx.math.Vector2;

public class Flamer extends BoundObject {

    public int health = 3;
    public float timeSinceHit = 0;
    public float hitFrequency = 2; // How often they can be hit.

    public boolean falling = false;

    public Flamer(Vector2 position) {
        super(position, 15f, 25f);
    }

    public void update(float delta) {
        super.update(delta);
        this.speed = 20f;
        timeSinceHit += delta;
    }

    public void hit() {
        if (timeSinceHit > hitFrequency) {
            if (health >= 0){
                health--;
            }
            // Reset the counter.
            timeSinceHit = 0;
        }
    }

    public boolean isDead() {
        if (health <= 0) {
            return true;
        }

        return false;
    }
}
