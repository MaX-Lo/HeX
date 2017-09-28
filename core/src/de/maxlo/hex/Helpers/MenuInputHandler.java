package de.maxlo.hex.Helpers;

import com.badlogic.gdx.InputProcessor;

import de.maxlo.hex.Screens.MenuScreen;

/**
 * Created by max on 21.09.17.
 */

public class MenuInputHandler implements InputProcessor {

    MenuScreen menuScreen;

    public MenuInputHandler(MenuScreen menuScreen) {
        this.menuScreen = menuScreen;
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        menuScreen.startGame();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
