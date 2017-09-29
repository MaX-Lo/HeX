package de.maxlo.hex.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Assets {

    public AssetManager manager;

    public BitmapFont font;
    public BitmapFont fontMenu;
    public BitmapFont fontUnits;

    public GlyphLayout layout;

    public static Texture blackHex, blueHex, yellowHex, greenHex, purpleHex, redHex;
    public static Texture selectionHex;

    public Assets() {
        manager = new AssetManager();

        loadTextures();
        loadFonts();
    }

    private void loadTextures() {
        manager.load("textures/hexagon_black.png", Texture.class);
        manager.load("textures/hexagon_blue.png", Texture.class);
        manager.load("textures/hexagon_yellow.png", Texture.class);
        manager.load("textures/hexagon_green.png", Texture.class);
        manager.load("textures/hexagon_purple.png", Texture.class);
        manager.load("textures/hexagon_red.png", Texture.class);

        manager.load("textures/hexagon_selection.png", Texture.class);
    }

    private void loadFonts() {
        // init font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/open_sans_regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        font = generator.generateFont(parameter);

        FreeTypeFontGenerator.FreeTypeFontParameter parameterMenu = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterMenu.size = 35;
        fontMenu = generator.generateFont(parameterMenu);

        FreeTypeFontGenerator.FreeTypeFontParameter unitsParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        unitsParameter.size = 60;
        fontUnits = generator.generateFont(unitsParameter);

        layout = new GlyphLayout(); // Layout needed for text measurements
    }

    /**
     * should be called after it's sure that all textures are loaded into the assetmanager
     */
    public void done() {
        blackHex = manager.get("textures/hexagon_black.png");
        blueHex = manager.get("textures/hexagon_blue.png");
        yellowHex = manager.get("textures/hexagon_yellow.png");
        greenHex = manager.get("textures/hexagon_green.png");
        purpleHex = manager.get("textures/hexagon_purple.png");
        redHex = manager.get("textures/hexagon_red.png");

        selectionHex = manager.get("textures/hexagon_selection.png");
    }

    public void dispose() {
        manager.dispose();
    }
}
