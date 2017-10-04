package de.maxlo.hex.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.maxlo.hex.Screens.GameScreen;

/**
 * Created by max on 21.09.17.
 */

public class GameInputHandler implements InputProcessor, GestureDetector.GestureListener {

    private GameScreen gs;

    private boolean keyPressed = false;
    private int keycode;

    public GameInputHandler(GameScreen gs) {
        this.gs = gs;
    }

    @Override
    public boolean keyDown(int keycode) {
        keyPressed = true;
        this.keycode = keycode;
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyPressed = false;
        return true;
    }

    /**
     * updates game depending on current keyboard input
     * should be called with every render cycle
     */
    public void update() {
        if (keyPressed) {
            switch (keycode) {
                // arrow keys
                case Input.Keys.LEFT:
                    gs.move(new Vector3(-25, 0, 0));
                    break;
                case Input.Keys.RIGHT:
                    gs.move(new Vector3(25, 0, 0));
                    break;
                case Input.Keys.UP:
                    gs.move(new Vector3(0, 25, 0));
                    break;
                case Input.Keys.DOWN:
                    gs.move(new Vector3(0, -25, 0));
                    break;
                // +/- zoom keys
                case Input.Keys.PLUS:
                    gs.zoom(-0.05f);
                    break;
                case Input.Keys.MINUS:
                    gs.zoom(0.05f);
            }
        }
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        gs.click(pos);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        gs.zoom(amount/2f);
        return true;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    private boolean zooming = false;
    private boolean dragging = false;
    private float oldAmount = 0;
    @Override
    public boolean zoom(float initialDistance, float distance) {
        float zoomReduction = (Gdx.graphics.getWidth()+Gdx.graphics.getHeight()) / 10;
        float amount = (distance - initialDistance) / zoomReduction;

        float zoomChange = oldAmount - amount;

        if (!dragging && (zoomChange > 0.025f || zoomChange < -0.025f)) // determine how fast zooming should get activated
            zooming = true;

        if (zooming && !dragging)
            gs.zoom(zoomChange);

        oldAmount = amount;
        return true;
    }

    private float oldDragX;
    private float oldDragY;
    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {

        if (!zooming) {

            float x = (pointer1.x + pointer2.x) / 2f;
            float y = (pointer1.y + pointer2.y) / 2f;

            if (initialPointer1.x == pointer1.x && initialPointer1.y == pointer1.y && initialPointer2.x == pointer2.x && initialPointer2.y == pointer2.y) {
                oldDragX = x;
                oldDragY = y;
            }

            Vector3 translation = new Vector3(0, 0, 0);
            translation.x = oldDragX - x;
            translation.y = y - oldDragY;

            oldDragX = x;
            oldDragY = y;

            float abs = Math.abs(translation.x)+Math.abs(translation.y);
            if (abs > 11) // determine how fast dragging should get activated
                dragging = true;

            gs.move(gs.convertScreenPixelToGamePixel(translation));
        }

        return true;
    }

    @Override
    public void pinchStop() {
        oldAmount = 0;
        zooming = false;
        dragging = false;
    }
}
