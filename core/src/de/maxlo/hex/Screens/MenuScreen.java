package de.maxlo.hex.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.maxlo.hex.Helpers.Assets;
import de.maxlo.hex.Hex;

/**
 * Created by max on 21.09.17.
 */

public class MenuScreen implements Screen {

    private Hex game;
    private Assets assets;

    private Stage stage;

    public MenuScreen(Hex game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Button startBtn = createStartButton();
        Button optionBtn = createOptionButton();
        Button highscoreBtn = creatHighscoreButton();

        stage.addActor(startBtn);
        stage.addActor(optionBtn);
        stage.addActor(highscoreBtn);
    }

    private Button creatHighscoreButton() {
        Skin highscoreBtnSkin = new Skin(Gdx.files.internal("skins/shade/skin/uiskin.json"));
        Button highscoreBtn = new TextButton("Highscores", highscoreBtnSkin);
        highscoreBtn.setPosition(Gdx.graphics.getWidth()/2 - highscoreBtn.getWidth()/2, Gdx.graphics.getHeight()/2 - highscoreBtn.getHeight());
        highscoreBtn.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button){
                startGame();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
        });
        return highscoreBtn;
    }

    private Button createOptionButton() {
        Skin optionBtnSkin = new Skin(Gdx.files.internal("skins/shade/skin/uiskin.json"));
        Button optionBtn = new TextButton("Option", optionBtnSkin);
        optionBtn.setPosition(Gdx.graphics.getWidth()/2 - optionBtn.getWidth()/2, Gdx.graphics.getHeight()/2);
        optionBtn.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                startGame();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        return optionBtn;
    }

    private Button createStartButton() {
        Skin startBtnSkin = new Skin(Gdx.files.internal("skins/shade/skin/uiskin.json"));
        Button startBtn = new TextButton("Start", startBtnSkin);
        startBtn.setPosition(Gdx.graphics.getWidth()/2-startBtn.getWidth()/2, Gdx.graphics.getHeight()/2 + startBtn.getHeight());
        startBtn.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                startGame();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        return startBtn;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
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

    public void startGame() {
        game.setScreen(new GameScreen(game));
    }
}