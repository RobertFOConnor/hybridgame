package com.yellowbytestudios.hybrid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yellowbytestudios.hybrid.media.Assets;
import com.yellowbytestudios.hybrid.media.Fonts;
import com.yellowbytestudios.hybrid.screens.game.GameScreen;
import com.yellowbytestudios.hybrid.screens.ScreenManager;

public class MainGame extends ApplicationAdapter {

    public static String DEVICE;
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    private boolean loaded = false;
    private boolean fullscreen = true;

    //Renderers
    private SpriteBatch sb;
    private ShapeRenderer sr;

    String vertexShader;
    String fragmentShader;
    ShaderProgram shaderProgram;


    public MainGame(String device) {
        DEVICE = device;
    }

    @Override
    public void create() {
        sb = new SpriteBatch();
        sr = new ShapeRenderer();
        Assets.load();
        Fonts.load();

        vertexShader = Gdx.files.internal("shaders/vertex.glsl").readString();
        fragmentShader = Gdx.files.internal("shaders/fragment.glsl").readString();
        shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
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
            //sb.setShader(shaderProgram);
            ScreenManager.getCurrentScreen().render(sb, sr);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {

                if (!fullscreen) {
                    Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
                    Gdx.graphics.setFullscreenMode(mode);
                } else {
                    Gdx.graphics.setWindowedMode(1280, 720);
                }
                fullscreen = !fullscreen;
            }
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
