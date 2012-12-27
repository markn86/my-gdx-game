package screens;

import lib.OverlapTester;
import model.InteractiveImage;
import model.World;
import view.WorldRenderer;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;

import controller.WorldController;

public class GameScreen implements Screen, InputProcessor {

    private World world;
    private WorldRenderer renderer;
    private WorldController controller;

    private int width, height;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        controller.update(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.setSize(width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void show() {
        world = new World();
        renderer = new WorldRenderer(world);
        controller = new WorldController(world);
        Gdx.input.setInputProcessor(this);
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (!Gdx.app.getType().equals(ApplicationType.Android)) {
            return false;
        }
        InteractiveImage leftArrow = world.getInteractiveImage("leftArrow");
        InteractiveImage rightArrow = world.getInteractiveImage("rightArrow");
        InteractiveImage jumpIcon = world.getInteractiveImage("jumpIcon");
        float pointTouchedX = x / (width / WorldRenderer.CAMERA_WIDTH);
        float pointTouchedY = (height - y) / (height / WorldRenderer.CAMERA_HEIGHT);
        if (OverlapTester.pointInRectangle(leftArrow.getBounds(), pointTouchedX, pointTouchedY)) {
            controller.leftPressed();
        }
        if (OverlapTester.pointInRectangle(rightArrow.getBounds(), pointTouchedX, pointTouchedY)) {
            controller.rightPressed();
        }
        if (OverlapTester.pointInRectangle(jumpIcon.getBounds(), pointTouchedX, pointTouchedY)) {
            controller.jumpPressed();
        }
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (!Gdx.app.getType().equals(ApplicationType.Android)) {
            return false;
        }
        InteractiveImage leftArrow = world.getInteractiveImage("leftArrow");
        InteractiveImage rightArrow = world.getInteractiveImage("rightArrow");
        InteractiveImage jumpIcon = world.getInteractiveImage("jumpIcon");
        float pointTouchedX = x / (width / WorldRenderer.CAMERA_WIDTH);
        float pointTouchedY = (height - y) / (height / WorldRenderer.CAMERA_HEIGHT);
        if (OverlapTester.pointInRectangle(leftArrow.getBounds(), pointTouchedX, pointTouchedY)) {
            controller.leftReleased();
        }
        if (OverlapTester.pointInRectangle(rightArrow.getBounds(), pointTouchedX, pointTouchedY)) {
            controller.rightReleased();
        }
        if (OverlapTester.pointInRectangle(jumpIcon.getBounds(), pointTouchedX, pointTouchedY)) {
            controller.jumpReleased();
        }
        return true;
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
