package de.maxlo.hex.Screens;


import com.badlogic.gdx.*;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.maxlo.hex.GameObjects.Hexagon;
import de.maxlo.hex.GameObjects.NormalHexagon;
import de.maxlo.hex.GameObjects.Player;
import de.maxlo.hex.Helpers.Assets;
import de.maxlo.hex.Helpers.GameInputHandler;
import de.maxlo.hex.Hex;

/**
 * Created by max on 21.09.17.
 */

public class GameScreen implements Screen {

    private static final int GAME_WIDTH = 1920;
    private static final int GAME_HEIGHT = 1080;

    private Hex game;
    private Assets assets;

    private Stage uiStage;
    private InputMultiplexer multiplexer;
    private OrthographicCamera camera;

    Hexagon hexTest;

    GameScreen(Hex game) {
        this.game = game;
        assets = game.getAssets();


        uiStage = new Stage(new ScreenViewport());
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage);
        multiplexer.addProcessor(new GameInputHandler(this));
        Gdx.input.setInputProcessor(multiplexer);

        initUI();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
        camera.translate(-50, -25);

        hexTest = new NormalHexagon(new Player(), assets.hexagon);
    }

    private void initUI() {
        Skin mySkin = new Skin(Gdx.files.internal("skins/shade/skin/uiskin.json"));

        float ui_border = Gdx.graphics.getHeight() / 25;

        // init Menu Button
        Button menuBtn = new TextButton("Menu", mySkin, "round");
        menuBtn.setPosition(Gdx.graphics.getWidth() / 25, Gdx.graphics.getHeight() - ui_border - menuBtn.getHeight());
        menuBtn.setSize(menuBtn.getWidth() + 65, menuBtn.getHeight() + 15);
        menuBtn.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MenuScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        uiStage.addActor(menuBtn);

        // init remaining Label
        Label remainingLabel = new Label("remaining: xx", mySkin, "title");
        remainingLabel.setSize(remainingLabel.getWidth(), remainingLabel.getHeight());
        remainingLabel.setPosition(Gdx.graphics.getWidth() - remainingLabel.getWidth() - ui_border, Gdx.graphics.getHeight() - remainingLabel.getHeight() - ui_border);
        uiStage.addActor(remainingLabel);

    }

    public Vector3 convertScreenPixelToGamePixel(Vector3 coordinates) {
        // Screen coordinates differ from game coordinates and have to be transformed
        coordinates.x = coordinates.x * (GAME_WIDTH / (Gdx.graphics.getWidth() * 1.0f));
        coordinates.y = coordinates.y * (GAME_HEIGHT / (Gdx.graphics.getHeight() * 1.0f));
        return coordinates;
    }

    public void move(Vector3 delta) {
        camera.translate(delta.x, delta.y);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        assets.layout.setText(assets.font, "HeX");
        assets.font.draw(game.batch, "HeX", (0.5f*Gdx.graphics.getWidth())-(0.5f*assets.layout.width), (0.5f*Gdx.graphics.getHeight())+(0.5f*assets.layout.height));

        game.batch.end();

        uiStage.act();

        game.batch.begin();
        game.batch.draw(hexTest.getTexture(), 0, 0);
        game.batch.end();

        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
