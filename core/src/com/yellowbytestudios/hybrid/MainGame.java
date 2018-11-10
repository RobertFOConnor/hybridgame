package com.yellowbytestudios.hybrid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yellowbytestudios.hybrid.media.Assets;
import com.yellowbytestudios.hybrid.media.Sounds;
import com.yellowbytestudios.hybrid.screens.GameScreen;
import com.yellowbytestudios.hybrid.screens.ScreenManager;
import com.yellowbytestudios.hybrid.utils.DeveloperTools;

public class MainGame extends ApplicationAdapter {

    public static String DEVICE;
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    private boolean loaded = false;

    //Renderers
    private SpriteBatch sb;
    private ShapeRenderer sr;


    public MainGame(String device) {
        DEVICE = device;
    }

    @Override
    public void create() {
        sb = new SpriteBatch();
        sr = new ShapeRenderer();
        Assets.load();
    }

    @Override
    public void render() {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glClearColor(0, 0, 0, 0);

        if (Assets.update() && !loaded) {
            loaded = true;
            ScreenManager.setScreen(new GameScreen());
        }

        if (loaded) {
            ScreenManager.getCurrentScreen().update(Gdx.graphics.getDeltaTime());
            ScreenManager.getCurrentScreen().render(sb, sr);
        }
    }

    @Override
    public void resize(int w, int h) {
        if (ScreenManager.getCurrentScreen() != null) {
            ScreenManager.getCurrentScreen().resize(w, h);
        }
    }

    @Override
    public void dispose() {
        sb.dispose();
        Assets.dispose();
    }
}
