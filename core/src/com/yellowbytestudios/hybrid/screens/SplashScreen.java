package com.yellowbytestudios.hybrid.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.yellowbytestudios.hybrid.MainGame.HEIGHT;
import static com.yellowbytestudios.hybrid.MainGame.WIDTH;

public class SplashScreen extends Screen {

    private Texture logo;
    private int timer = 0;
    private int displaySeconds = 3;

    @Override
    public void create() {
        super.create();
        logo = new Texture("logo.png");
        logo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }


    @Override
    public void update(float delta) {
        super.update(delta);
        if (timer > displaySeconds * 60) {
            ScreenManager.setScreen(new TitleScreen());
        }
        timer++;
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(logo, WIDTH / 2 - logo.getWidth() / 2, HEIGHT / 2 - logo.getHeight() / 2, logo.getWidth(), logo.getHeight());
        sb.end();
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void goBack() {

    }
}
