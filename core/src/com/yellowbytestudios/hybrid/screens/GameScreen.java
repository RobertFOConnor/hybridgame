package com.yellowbytestudios.hybrid.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yellowbytestudios.hybrid.physics.PhysicsManager;
import com.yellowbytestudios.hybrid.tile.TileManager;

import static com.yellowbytestudios.hybrid.MainGame.HEIGHT;
import static com.yellowbytestudios.hybrid.MainGame.WIDTH;

public class GameScreen extends Screen {

    private Color skyColor;
    private PhysicsManager physicsManager;
    private TileManager tileManager;

    @Override
    public void create() {
        super.create();
        skyColor = new Color(39 / 255f, 51 / 255f, 83 / 255f, 0);
        physicsManager = new PhysicsManager(camera);
        tileManager = new TileManager(physicsManager, camera);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        physicsManager.update(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.R) || physicsManager.isGameOver()) {
            ScreenManager.setScreen(new GameScreen());
        }
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {

        //draw sky
        //sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(skyColor);
        sr.rect(0, 0, WIDTH, HEIGHT);
        sr.end();

        sb.setProjectionMatrix(camera.combined);
        tileManager.render();
        physicsManager.render(sb);
    }

    @Override
    public void dispose() {
        super.dispose();
        tileManager.dispose();
        physicsManager.dispose();
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
