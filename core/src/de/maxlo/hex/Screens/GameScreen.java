package de.maxlo.hex.Screens;


import com.badlogic.gdx.*;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Map;

import de.maxlo.hex.GameObjects.GameMap;
import de.maxlo.hex.GameObjects.Hexagon;
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

    Label remainingLabel;
    Label newUnitsLabel;
    Slider slider;
    Table table;

    private Stage uiStage;
    private GameInputHandler gameInputHandler;
    private InputMultiplexer multiplexer;
    private OrthographicCamera camera;

    private GameMap map;

    GameScreen(Hex game) {
        this.game = game;
        assets = game.getAssets();

        uiStage = new Stage(new ScreenViewport());
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage);
        gameInputHandler = new GameInputHandler(this);
        multiplexer.addProcessor(gameInputHandler);
        Gdx.input.setInputProcessor(multiplexer);

        initUI();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

        map = new GameMap();
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
        remainingLabel = new Label("remaining: xx", mySkin, "title");
        remainingLabel.setPosition(Gdx.graphics.getWidth() - remainingLabel.getWidth() - ui_border, Gdx.graphics.getHeight() - remainingLabel.getHeight() - ui_border);
        uiStage.addActor(remainingLabel);


        // init Window with elements for unit movement

        Button cancelBtn = new TextButton("x", mySkin);
        cancelBtn.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                hideUnitMovement();
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        Button confirmBtn = new TextButton("_/", mySkin);
        confirmBtn.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                int units = (int)Math.floor(slider.getValue());
                map.getHexagon(map.getSelectedHexagon()).increase(units);
                map.getPlayer(1).decreaseRemainingUnits(units);
                hideUnitMovement();
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        Button addBtn = new TextButton("+", mySkin);
        addBtn.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                int units = Math.round(slider.getValue());
                if (units >= map.getPlayer(1).getRemainingUnits())
                    return;
                else {
                    slider.setValue(units + 1);
                    remainingLabel.setText(String.valueOf(Math.floor(units)));
                }
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        Button subBtn = new TextButton("-", mySkin);
        subBtn.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                int units = Math.round(slider.getValue());
                if (units >= map.getPlayer(1).getRemainingUnits())
                    return;
                else {
                    slider.setValue(units - 1);
                    remainingLabel.setText(String.valueOf(units));
                }
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });


        newUnitsLabel = new Label("0", mySkin);

        slider = new Slider(0, 100, 1, false, mySkin);

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (slider.getValue() > map.getPlayer(1).getRemainingUnits())
                    slider.setValue(map.getPlayer(1).getRemainingUnits());

                setNewUnitsLabel(Math.round(slider.getValue()));
            }
        });

        table = new Window("unit movements", mySkin);

        table.setSize(Gdx.graphics.getWidth()/3.5f, Gdx.graphics.getHeight()/6);

        table.add(cancelBtn).space(8).spaceRight(15);
        table.add(subBtn).space(8);
        table.add(newUnitsLabel).space(8).expandX();
        table.add(addBtn).space(8);
        table.add(confirmBtn).space(8).spaceRight(15);
        table.row();
        table.add(slider).colspan(5).expandX();
        table.setVisible(false);

        uiStage.addActor(table);
    }

    public void setNewUnitsLabel(int value) {
        newUnitsLabel.setText(String.valueOf(value));
    }

    /**
     * Screen coordinates differ from game coordinates and therefore have to be transformed
     *
     * @param coordinates screen coordinates that should be transformed
     * @return transformed coordinates
     */

    /**
     * Move the game in a given direction
     *
     * @param delta - amount the game should be moved
     */
    public void move(Vector3 delta) {
        camera.translate(delta.x, delta.y);
    }

    /**
     * Zoom in or out.
     * Number greater 0 -> zoom in.
     * Number smaller 0 -> zoom out.
     *
     * @param amount - set how fast to zoom
     */
    public void zoom(float amount) {
        if (camera.zoom+amount < 0.2f)
            camera.zoom = 0.2f;
        else if (camera.zoom+amount > 4)
            camera.zoom = 4;

        camera.zoom += amount;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // update game logic and objects at first...
        map.update(delta);
        gameInputHandler.update();

        // refresh remaining units label
        remainingLabel.setText("remaining: " + String.valueOf(Math.round(Math.floor(map.getPlayer(1).getRemainingUnits()))));

        // ...and graphics afterwards
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        assets.layout.setText(assets.font, "HeX");
        assets.font.draw(game.batch, "HeX", (0.5f*Gdx.graphics.getWidth())-(0.5f*assets.layout.width), (0.5f*Gdx.graphics.getHeight())+(0.5f*assets.layout.height));

        game.batch.end();

        uiStage.act();

        game.batch.begin();
        drawHexagons(game.batch, map.getHexagons());
        if (map.getSelectedHexagon() != null)
            drawHexagon(game.batch, map.getSelectedHexagon(), Assets.selectionHex);
        game.batch.end();

        uiStage.draw();
    }

    /**
     * Draws the Hexagons onto the given Spritebatch
     *
     * @param batch to draw on
     * @param hexagons map containing hexagons with coordinates as key
     */
    private void drawHexagons(SpriteBatch batch, Map<Vector3, Hexagon> hexagons) {
        for (Vector3 coordinate : hexagons.keySet()) {
            Hexagon hexagon = hexagons.get(coordinate);
            drawHexagon(batch, coordinate, hexagon.getTexture());
            drawUnitNumber(batch, coordinate, hexagon.getUnits());
        }
    }

    /**
     * Draws a single hexagon
     *
     * @param batch to draw on
     * @param hexPos - position from the hexagon on the map. 0,0 is the bottom left Hexagon. Increasing x means going diagonal
     *                    right down. Increasing y means going straight upwards.
     * @param texture of the hexagon that should be drawn
     */
    private void drawHexagon(SpriteBatch batch, Vector3 hexPos, Texture texture) {
        Vector3 screenPos = convertHexCoordinatesToGamePixel(hexPos);

        batch.draw(texture, screenPos.x, screenPos.y);
    }

    private void drawUnitNumber(SpriteBatch batch, Vector3 hexPos, int number) {
        Vector3 screenPos = convertHexCoordinatesToGamePixel(hexPos);
        float hexWidth = Assets.greenHex.getWidth();
        float hexHeight = Assets.greenHex.getHeight();

        int units = number;
        assets.layout.setText(assets.fontUnits, String.valueOf(units));
        assets.fontUnits.draw(batch, String.valueOf(units), screenPos.x+(0.5f*hexWidth)-(0.5f*assets.layout.width), screenPos.y + (0.5f*hexHeight) + (0.5f*assets.layout.height));
    }

    public void hideUnitMovement() {
        table.setVisible(false);
        slider.setValue(0);
        newUnitsLabel.setText("0");
    }

    public void showUnitMovement() {
        table.setVisible(true);
    }

    public void click(Vector3 pos) {
        pos = convertScreenPixelToHex(pos);

        if (map.getHexagon(pos) == null) {
            map.unselectHexagon();
            hideUnitMovement();
        } else if (map.getHexagon(pos).getOwner().equals(map.getPlayer(1))) {
            map.selectHexagon(pos);
            showUnitMovement();
        }
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
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

    private Vector3 convertHexCoordinatesToGamePixel(Vector3 hexPos) {
        // draw number of units in a hexagon on top of it
        float x = hexPos.x;
        float y = hexPos.y;

        float hexWidth = Assets.greenHex.getWidth();
        float hexHeight = Assets.greenHex.getHeight();
        float radius = hexWidth / 2.0f;

        // calculate x and y from the hexagon image
        float screenX = (float) (x * (radius + Math.sqrt((radius * radius) - ((hexHeight * hexHeight) / 4))));
        float screenY = (float) (-0.5 * hexHeight * x) + (hexHeight * y);

        return new Vector3(screenX, screenY, 0);
    }

    public Vector3 convertScreenPixelToGamePixel(Vector3 coordinates) {
        coordinates.x = coordinates.x * (GAME_WIDTH / (Gdx.graphics.getWidth() * 1.0f));
        coordinates.y = coordinates.y * (GAME_HEIGHT / (Gdx.graphics.getHeight() * 1.0f));
        return coordinates;
    }

    /**
     * Converts a given screen position to hexagon coordinate
     *
     * @param pos screen pos
     * @return hex pos
     */
    public Vector3 convertScreenPixelToHex(Vector3 pos) {
        // unproject coordinates, because in case screen is shifted or zoomed the coordinates would change otherwise
        pos = camera.unproject(pos);

        float gameX = pos.x;
        float gameY = -pos.y;

        float hexWidth = Assets.greenHex.getWidth();
        float hexHeight = Assets.greenHex.getHeight();
        float radius = hexWidth/2;

        float x = (float)(gameX/((0.5*hexWidth)+Math.sqrt((radius*radius)-((hexHeight*hexHeight)/4))));
        int xr = (int)(x-(0.3333));
        float y = (float)(gameY/hexHeight-(0.5*xr));
        int yr = (int)y;
        // negative numbers have to be rounded down
        if ((x-0.3333)<0) xr -= 1;
        yr = -yr;
        y = -y;

        // x and y relative to x0 and y0 of clicked hexagon
        // xc is between 0 and 1.3333 whereas 1-1.333 has to get checked
        // yc is between 0 and 1
        float xc = x-xr;
        float yc = y-yr;

        if (xc > 1) {
            if (yc > 0.5) {
                xc -= 1;
                float m = (-0.5f*hexHeight)/(0.25f*hexWidth); // Anstieg der Kante oben rechts
                if (m*xc+1 < yc)
                    xr += 1;
                yr += 1;
            } else {
                xc -= 1;
                float m = (0.5f*hexHeight)/(0.25f*hexWidth); // Anstieg der Kante unten rechts
                if (m*xc > yc) {
                    xr += 1;
                }
            }
        }

        return new Vector3(xr, yr, 0);
    }

}
