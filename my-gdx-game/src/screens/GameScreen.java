package screens;

import lib.Assets;
import lib.OverlapTester;
import lib.Sound;
import model.InteractiveImage;
import model.World;
import view.WorldRenderer;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;

import controller.WorldController;

public class GameScreen implements Screen, InputProcessor {

    private World world;
    private WorldController controller;
    private WorldRenderer renderer;

    // The interactive images, arrow keys, jump, fire.
    public InteractiveImage leftArrow;
    public InteractiveImage rightArrow;
    public InteractiveImage jumpIcon;
    public InteractiveImage fireIcon;

    public static final float CAMERA_WIDTH = 320;
    public static final float CAMERA_HEIGHT = 240;

    private int width, height;

    public GameScreen() {
        Assets.load();
        Sound.load();

        // Set the interactive images.
        leftArrow = new InteractiveImage(Assets.interactiveTextures.get("left_arrow"), new Vector2(5, 0));
        rightArrow = new InteractiveImage(Assets.interactiveTextures.get("right_arrow"), new Vector2(30, 0));
        jumpIcon = new InteractiveImage(Assets.interactiveTextures.get("jump"), new Vector2(GameScreen.CAMERA_WIDTH - 60, 2));
        fireIcon = new InteractiveImage(Assets.interactiveTextures.get("fire"), new Vector2(GameScreen.CAMERA_WIDTH - 30, 2));

        world = new World();
        controller = new WorldController(world);
        renderer = new WorldRenderer(world, this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // Update the position of everything.
        controller.update(delta);

        // Render the world.
        renderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        renderer.setSize(width, height);

        this.width = width;
        this.height = height;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (!Gdx.app.getType().equals(ApplicationType.Android)) {
            return false;
        }

        float pointTouchedX = x / (width / CAMERA_WIDTH);
        float pointTouchedY = (height - y) / (height / CAMERA_HEIGHT);

        if (OverlapTester.pointInRectangle(leftArrow.getBounds(), pointTouchedX, pointTouchedY)) {
            WorldController.leftPressed();
            WorldController.rightReleased();
        }

        if (OverlapTester.pointInRectangle(rightArrow.getBounds(), pointTouchedX, pointTouchedY)) {
            WorldController.rightPressed();
            WorldController.leftReleased();
        }

        if (OverlapTester.pointInRectangle(jumpIcon.getBounds(), pointTouchedX, pointTouchedY)) {
            WorldController.jumpPressed();
        }

        if (OverlapTester.pointInRectangle(fireIcon.getBounds(), pointTouchedX, pointTouchedY)) {
            WorldController.firePressed();
        }

        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (!Gdx.app.getType().equals(ApplicationType.Android)) {
            return false;
        }

        float pointTouchedX = x / (width / CAMERA_WIDTH);
        float pointTouchedY = (height - y) / (height / CAMERA_HEIGHT);

        if (OverlapTester.pointInRectangle(leftArrow.getBounds(), pointTouchedX, pointTouchedY)) {
            WorldController.leftReleased();
        } else if (OverlapTester.pointInRectangle(rightArrow.getBounds(), pointTouchedX, pointTouchedY)) {
            WorldController.rightReleased();
        } else if (OverlapTester.pointInRectangle(jumpIcon.getBounds(), pointTouchedX, pointTouchedY)) {
            WorldController.jumpReleased();
        } else if (OverlapTester.pointInRectangle(fireIcon.getBounds(), pointTouchedX, pointTouchedY)) {
            WorldController.fireReleased();
        } else {
            // controller.leftReleased();
            // controller.rightReleased();
            // controller.jumpReleased();
        }

        return true;
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
                WorldController.jumpPressed();
                break;
            case Keys.A:
                WorldController.leftPressed();
                break;
            case Keys.D:
                WorldController.rightPressed();
                break;
            case Keys.SPACE:
                WorldController.firePressed();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
                WorldController.jumpReleased();
                break;
            case Keys.A:
                WorldController.leftReleased();
                break;
            case Keys.D:
                WorldController.rightReleased();
                break;
            case Keys.SPACE:
                WorldController.fireReleased();
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }
}
