package screens;

import lib.OverlapTester;
import model.InteractiveImage;
import model.Player;
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

    private World world = new World(32, 24, 0, 0, 0, 0);
    private WorldRenderer renderer;

    // The interactive images, arrow keys, jump, fire.
    InteractiveImage leftArrow;
    InteractiveImage rightArrow;
    InteractiveImage jumpIcon;
    InteractiveImage fireIcon;

    public static final float CAMERA_WIDTH = 320;
    public static final float CAMERA_HEIGHT = 240;

    private float ppuX; // Pixels per unit on the X axis.
    private float ppuY; // Pixels per unit on the Y axis.

    private int width, height;

    @Override
    public void render(float delta) {
    	// Update
    	
    	// Render    	
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        WorldRenderer.spriteBatch.begin();
        renderer.render(delta);
        drawInteractiveImages();
        drawHearts();
        WorldRenderer.spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        renderer.setSize(width, height);
        ppuX = width / CAMERA_WIDTH;
        ppuY = height / CAMERA_HEIGHT;
        this.width = width;
        this.height = height;
    }

    @Override
    public void show() {
        renderer = new WorldRenderer(world);
        loadInteractiveImages();
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

    private void loadInteractiveImages() {
        leftArrow = new InteractiveImage(WorldRenderer.interactiveTextures.get("left_arrow"), new Vector2(5, 0));
        rightArrow = new InteractiveImage(WorldRenderer.interactiveTextures.get("right_arrow"), new Vector2(30, 0));
        jumpIcon = new InteractiveImage(WorldRenderer.interactiveTextures.get("jump"), new Vector2(GameScreen.CAMERA_WIDTH - 60, 2));
        fireIcon = new InteractiveImage(WorldRenderer.interactiveTextures.get("fire"), new Vector2(GameScreen.CAMERA_WIDTH - 30, 2));
    }

    private void drawInteractiveImages() {
        WorldRenderer.spriteBatch.draw(leftArrow.getTexture(), leftArrow.getPosition().x * ppuX, leftArrow.getPosition().y * ppuY, InteractiveImage.SIZE * ppuX, InteractiveImage.SIZE * ppuY);
        WorldRenderer.spriteBatch.draw(rightArrow.getTexture(), rightArrow.getPosition().x * ppuX, rightArrow.getPosition().y * ppuY, InteractiveImage.SIZE * ppuX, InteractiveImage.SIZE * ppuY);
        WorldRenderer.spriteBatch.draw(jumpIcon.getTexture(), jumpIcon.getPosition().x * ppuX, jumpIcon.getPosition().y * ppuY, InteractiveImage.SIZE * ppuX, InteractiveImage.SIZE * ppuY);
        WorldRenderer.spriteBatch.draw(fireIcon.getTexture(), fireIcon.getPosition().x * ppuX, fireIcon.getPosition().y * ppuY, InteractiveImage.SIZE * ppuX, InteractiveImage.SIZE * ppuY);
    }

    private void drawHearts() {
        // Get the players health.
        Player player = world.getPlayer();
        int health = player.health;
        // Draw the full hearts.
        for (int i = 0; i < health; i++) {
            WorldRenderer.spriteBatch.draw(WorldRenderer.heartTextures.get("full"), (i * 15f) * ppuX, (GameScreen.CAMERA_HEIGHT - 18) * ppuY, 15f * ppuX, 15f * ppuY);
        }
        // Now draw empty hearts.
        for (int i = health; i < 3; i++) {
            WorldRenderer.spriteBatch.draw(WorldRenderer.heartTextures.get("empty"), (i * 15f) * ppuX, (GameScreen.CAMERA_HEIGHT - 18) * ppuY, 15f * ppuX, 15f * ppuY);
        }
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
