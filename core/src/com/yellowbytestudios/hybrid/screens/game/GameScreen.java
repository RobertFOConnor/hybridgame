package com.yellowbytestudios.hybrid.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yellowbytestudios.hybrid.effects.FloaterManager;
import com.yellowbytestudios.hybrid.physics.PhysicsManager;
import com.yellowbytestudios.hybrid.physics.atoms.PhysicsObject;
import com.yellowbytestudios.hybrid.screens.Screen;
import com.yellowbytestudios.hybrid.screens.ScreenManager;
import com.yellowbytestudios.hybrid.tile.MapProps;
import com.yellowbytestudios.hybrid.tile.TileManager;
import com.yellowbytestudios.hybrid.tile.TileObjects;

import static com.yellowbytestudios.hybrid.MainGame.HEIGHT;
import static com.yellowbytestudios.hybrid.MainGame.WIDTH;
import static com.yellowbytestudios.hybrid.tile.TileManager.backgroundColor;

public class GameScreen extends Screen {

    private String mapName;
    private float startX;
    private float startY;

    private PhysicsManager physicsManager;
    private TileManager tileManager;
    //private FloaterManager floaterManager;


    public GameScreen() {
        this.mapName = "test";
        this.startX = 240;
        this.startY = 320;
    }

    public GameScreen(String mapName, float startX, float startY) {
        this.mapName = mapName;
        this.startX = startX;
        this.startY = startY;
    }

    @Override
    public void create() {
        super.create();
        physicsManager = new PhysicsManager(camera, startX, startY);
        tileManager = new TileManager(camera, physicsManager.getWorld(), mapName);
        //floaterManager = new FloaterManager();

        addGameObjects(MapProps.ENEMY_LAYER);
        addGameObjects(MapProps.ITEM_LAYER);

        for (PhysicsObject exit : TileObjects.createObjectFromObjectLayer(tileManager, MapProps.DOOR_LAYER)) {
            physicsManager.addObject(exit);
        }
    }

    private void addGameObjects(int layerId) {
        for (PhysicsObject object : TileObjects.createObject(tileManager, layerId)) {
            physicsManager.addObject(object);
        }
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

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(backgroundColor);
        sr.rect(0, 0, WIDTH, HEIGHT);
        sr.end();

        sb.setProjectionMatrix(camera.combined);
        tileManager.render();
        physicsManager.render(sb);
        //floaterManager.updateAndRender(sb);
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
