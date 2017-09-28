package de.maxlo.hex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.maxlo.hex.Helpers.Assets;
import de.maxlo.hex.Screens.MenuScreen;

public class Hex extends Game {

    private Assets assets;

    public SpriteBatch batch;

	@Override
	public void create () {

        assets = new Assets();
        assets.manager.finishLoading();
        assets.done();

        batch = new SpriteBatch();

        setScreen(new MenuScreen(this));
	}

	@Override
    public void render() {
        super.render();
    }
	
	@Override
	public void dispose () {
		super.dispose();
        assets.dispose();
        batch.dispose();
	}

    public Assets getAssets() {
        return assets;
    }
}
