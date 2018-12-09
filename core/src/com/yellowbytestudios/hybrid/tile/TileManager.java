package com.yellowbytestudios.hybrid.tile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;

import static com.yellowbytestudios.hybrid.tile.MapProps.BACKGROUND_COLOR;
import static com.yellowbytestudios.hybrid.tile.MapProps.HEIGHT;
import static com.yellowbytestudios.hybrid.tile.MapProps.TILE_SIZE;
import static com.yellowbytestudios.hybrid.tile.MapProps.WIDTH;

public class TileManager {

    private static final String MAP_DIR = "tilemap/";
    private static int tileSize = 80;
    public static int mapWidth = 0;
    public static int mapHeight = 0;
    public static Color backgroundColor;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;

    public TileManager(OrthographicCamera camera, World world, String mapName) {
        this.camera = camera;
        tiledMap = loadTiledMap(MAP_DIR + mapName + ".tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        makeWalls(world);
        setupMapProperties();
    }

    public void makeWalls(World world) {
        new TilePhysicsWallMaker().createWalls(world, tiledMap);
    }

    private TiledMap loadTiledMap(String fileName) {
        return new TmxMapLoader().load(fileName);
    }

    public void render() {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    private void setupMapProperties() {
        tileSize = getMapProperty(TILE_SIZE);
        mapWidth = getMapProperty(WIDTH);
        mapHeight = getMapProperty(HEIGHT);
        backgroundColor = Color.valueOf(tiledMap.getProperties().get(BACKGROUND_COLOR, String.class));
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

    public MapLayer getLayer(int index) {
        return tiledMap.getLayers().get(index);
    }

    public void dispose() {
        tiledMap.dispose();
    }
}
