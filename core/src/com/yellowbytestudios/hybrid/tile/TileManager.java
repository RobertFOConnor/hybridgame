package com.yellowbytestudios.hybrid.tile;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.yellowbytestudios.hybrid.physics.Exit;
import com.yellowbytestudios.hybrid.physics.PhysicsManager;
import com.yellowbytestudios.hybrid.physics.atoms.PhysicsObject;

import java.util.ArrayList;

import static com.yellowbytestudios.hybrid.tile.MapProperties.HEIGHT;
import static com.yellowbytestudios.hybrid.tile.MapProperties.TILE_SIZE;
import static com.yellowbytestudios.hybrid.tile.MapProperties.WIDTH;

public class TileManager {

    private OrthographicCamera camera;
    private static final String MAP_FILE_NAME = "tilemap/test.tmx";
    private static int tileSize = 80;
    public static int mapWidth = 0;
    public static int mapHeight = 0;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private ShapeRenderer sr;

    public TileManager(PhysicsManager physicsManager, OrthographicCamera camera) {
        this.camera = camera;
        tiledMap = loadTiledMap(MAP_FILE_NAME);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        sr = new ShapeRenderer();
        makeWalls(physicsManager.getWorld());
        setupMapWidthHeight();

        Exit exit = (Exit) TileObjects.createObject(this, LayerNames.EVENT_LAYER).get(0);
        physicsManager.addObject(exit);

        ArrayList<PhysicsObject> enemies = TileObjects.createObject(this, LayerNames.ENEMY_LAYER);
        for (PhysicsObject enemy : enemies) {
            physicsManager.addObject(enemy);
        }
    }

    public void makeWalls(World world) {
        new TilePhysicsWallMaker().createWalls(world, tiledMap);
    }

    private TiledMap loadTiledMap(String fileName) {
        return new TmxMapLoader().load(fileName);
    }

    public void render() {
        camera.position.set(camera.position.x, camera.position.y, camera.position.z);
        camera.update();
        tiledMapRenderer.setView(camera);
        camera.update();
        tiledMapRenderer.render();
    }

    private void setupMapWidthHeight() {
        tileSize = getMapProperty(TILE_SIZE);
        mapWidth = getMapProperty(WIDTH);
        mapHeight = getMapProperty(HEIGHT);
    }

    private Integer getMapProperty(String property) {
        return tiledMap.getProperties().get(property, Integer.class);
    }

    public static float getTileSize() {
        return tileSize;
    }

    public int getLayerCount() {
        return tiledMap.getLayers().size();
    }

    public TiledMapTileLayer getLayer(int index) {
        return (TiledMapTileLayer) tiledMap.getLayers().get(index);
    }

    public void dispose() {
        tiledMap.dispose();
        sr.dispose();
        tiledMap = null;
        tiledMapRenderer = null;
        sr = null;
    }
}
