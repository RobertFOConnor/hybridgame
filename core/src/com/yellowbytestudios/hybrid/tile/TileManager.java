package com.yellowbytestudios.hybrid.tile;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.yellowbytestudios.hybrid.physics.PhysicsManager;

import java.util.ArrayList;

import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.PPM;
import static com.yellowbytestudios.hybrid.tile.MapProperties.HEIGHT;
import static com.yellowbytestudios.hybrid.tile.MapProperties.TILE_SIZE;
import static com.yellowbytestudios.hybrid.tile.MapProperties.WIDTH;


public class TileManager {

    private OrthographicCamera camera;
    private static final boolean ARCADE_MODE = false;
    private static final String MAP_FILE_NAME = "tilemap/test.tmx";
    private static int tileSize = 80;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    private int mapWidth, mapHeight;

    private ArrayList<Body> walls;
    private ShapeRenderer sr;

    int count = 0;

    public TileManager(PhysicsManager physicsManager, OrthographicCamera camera) {
        this.camera = camera;
        tiledMap = ARCADE_MODE ? generateTiledMap() : loadTiledMap(MAP_FILE_NAME);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        sr = new ShapeRenderer();
        makeWalls(physicsManager.getWorld());
        setupMapWidthHeight();

        //physicsManager.getPlayer(0).setPos(mapWidth * tileSize / 2 / PPM, mapHeight * tileSize / 2 / PPM);
    }

    public void makeWalls(World world) {
        TilePhysicsWallMaker tc = new TilePhysicsWallMaker();
        walls = tc.createWalls(world, tiledMap);
    }

    public void destroyWalls(World world) {
        for (Body body : walls) {
            world.destroyBody(body);
        }
    }

    private TiledMap loadTiledMap(String fileName) {
        return new TmxMapLoader().load(fileName);
    }

    private TiledMap generateTiledMap() {
        return new TileMapGenerator().getGeneratedMap();
    }

    public void render() {
        float oldX = camera.position.x;
        camera.position.set(20 * 80 * count + camera.position.x, camera.position.y, camera.position.z);
        camera.update();
        tiledMapRenderer.setView(camera);
        //camera.position.set(oldX, camera.position.y, camera.position.z);
        camera.update();
        tiledMapRenderer.render();
    }

    private void setupMapWidthHeight() {
        mapWidth = getMapProperty(WIDTH);
        mapHeight = getMapProperty(HEIGHT);
        tileSize = getMapProperty(TILE_SIZE);
    }

    private Integer getMapProperty(String property) {
        return tiledMap.getProperties().get(property, Integer.class);
    }

    public float getMapWidth() {
        return mapWidth * tileSize;
    }

    public float getMapHeight() {
        return mapHeight * tileSize;
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

    public void destroyTile(int tileX, int tileY, World world) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        if (layer.getCell(tileX, tileY) != null) {
            layer.setCell(tileX, tileY, null);
            rebuildWalls(world);
        }
    }

    public void placeTile(int tileX, int tileY, World world) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        if (layer.getCell(tileX, tileY) == null) {
            layer.setCell(tileX, tileY, TileHelper.getCellByName(TileNames.WALL_TILE));
            rebuildWalls(world);
        }
    }

    public void generateMoreMap(PhysicsManager physicsManager) {
        tiledMap = new TileMapGenerator().addToMap(tiledMap);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        rebuildWalls(physicsManager.getWorld());
        count++;
    }

    private void rebuildWalls(World world) {
        destroyWalls(world);
        makeWalls(world);
    }
}
