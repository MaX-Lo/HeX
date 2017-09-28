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

    public GlyphLayout layout;

    public Texture hexagon;
    public Texture img;

    public Assets() {
        manager = new AssetManager();

        loadTextures();
        loadFonts();
    }

    private void loadTextures() {
        manager.load("textures/badlogic.jpg", Texture.class);
        manager.load("textures/hexagon_blue.png", Texture.class);
    }

    private void loadFonts() {
        // init font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/open_sans_regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        FreeTypeFontGenerator.FreeTypeFontParameter parameterMenu = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterMenu.size = 35;
        font = generator.generateFont(parameter);
        fontMenu = generator.generateFont(parameterMenu);
        layout = new GlyphLayout(); // Layout needed for text measurements
    }

    public void done() {
        hexagon = manager.get("textures/hexagon_blue.png");
        img = manager.get("textures/badlogic.jpg");
    }

    public void dispose() {
        manager.dispose();
    }
}
