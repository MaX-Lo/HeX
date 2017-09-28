package de.maxlo.hex.Helpers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

import de.maxlo.hex.Screens.GameScreen;

/**
 * Created by max on 21.09.17.
 */

public class GameInputHandler implements InputProcessor {

    private GameScreen gs;
    private boolean wasDragged = false;
    private int oldX, oldY;

    public GameInputHandler(GameScreen gs) {
        this.gs = gs;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }



    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        oldX = 0;
        oldY = 0;

        Vector3 pos = new Vector3(screenX, screenY, 0);

//        if (!gs.uitouch(pos) && !wasDragged) { // check overlay button touch
//            if (!gs.touch(pos)) { // check cell touch only if puzzle wasn't dragged
//                gs.changeMode(); // change click mode
//            }
//        }
        wasDragged = false;
        return true;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float mvX = oldX - screenX;
        float mvY = oldY - screenY;
        oldX = screenX;
        oldY = screenY;

        if (mvX != -screenX && mvY != -screenY) {
            Vector3 delta = new Vector3(mvX, -mvY, 0);
            gs.move(gs.convertScreenPixelToGamePixel(delta));

            if (delta.x>0.1f || delta.y>0.1f) {
                wasDragged = true;
            }
        }
        return true;
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
        return false;
    }
}
