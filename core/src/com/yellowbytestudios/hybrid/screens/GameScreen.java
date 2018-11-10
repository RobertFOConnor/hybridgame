package com.yellowbytestudios.hybrid.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yellowbytestudios.hybrid.physics.PhysicsManager;
import com.yellowbytestudios.hybrid.tile.TileManager;

public class GameScreen extends Screen {

    private PhysicsManager physicsManager;
    private TileManager tileManager;

    @Override
    public void create() {
        super.create();
        physicsManager = new PhysicsManager(camera);
        tileManager = new TileManager(physicsManager, camera);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        physicsManager.update(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            ScreenManager.setScreen(new GameScreen());
        }
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        tileManager.render();
        physicsManager.render(sb);
        sb.end();
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
