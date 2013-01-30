package lib;

import com.badlogic.gdx.Gdx;

public class Sound {

    public static com.badlogic.gdx.audio.Sound playerBullet;
    public static com.badlogic.gdx.audio.Sound playerJump;

    public static void load () {
        playerBullet = load("sounds/player_bullet.mp3");
        playerJump = load("sounds/player_jump.wav");
    }

    private static com.badlogic.gdx.audio.Sound load(String name) {
        return Gdx.audio.newSound(Gdx.files.internal(name));
    }
}
