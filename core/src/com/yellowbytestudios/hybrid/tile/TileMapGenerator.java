package com.yellowbytestudios.hybrid.tile;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.ArrayList;

/**
 * Created by Robert on 05/03/16.
 */
public class TileMapGenerator {


    public static int SECTION_COUNT = 3;
    public static int SECTION_WIDTH = 20;
    private static int SECTION_HEIGHT = 20;
    private static int WIDTH = SECTION_WIDTH * SECTION_COUNT;
    private static int HEIGHT = SECTION_HEIGHT * SECTION_COUNT;

    private ArrayList<String> sections;

    public TiledMap getGeneratedMap() {

        TiledMap tiledMap = new TiledMap();
        MapLayers layers = tiledMap.getLayers();

        int tileSize = (int) TileManager.getTileSize();
        int layerCount = 1;

        for (int i = 0; i < layerCount; i++) {
            layers.add(new TiledMapTileLayer(WIDTH, HEIGHT, tileSize, tileSize));
        }

        sections = new ArrayList<String>();
        sections.add("tilemap/dirt_1.tmx");
        sections.add("tilemap/dirt_2.tmx");

        for (int x = 0; x < SECTION_COUNT; x++) {
            for (int y = 0; y < SECTION_COUNT; y++) {
                TiledMap sectionTiledMap = new TmxMapLoader().load(sections.get((int) (Math.random() * sections.size())));
                addSection(tiledMap, sectionTiledMap, y * SECTION_HEIGHT, x * SECTION_WIDTH);
            }
        }

        setMapProperties(tiledMap);

        return tiledMap;
    }

    public TiledMap addToMap(TiledMap map) {
        TiledMapTileLayer oldLayer = (TiledMapTileLayer) map.getLayers().get(0);


        System.out.println("making more map");
        TiledMap tiledMap = new TiledMap();
        MapLayers layers = tiledMap.getLayers();

        int tileSize = (int) TileManager.getTileSize();
        int layerCount = 1;

        for (int i = 0; i < layerCount; i++) {
            layers.add(new TiledMapTileLayer(oldLayer.getWidth() + SECTION_WIDTH, HEIGHT, tileSize, tileSize));
        }

        sections = new ArrayList<String>();
        sections.add("tilemap/dirt_1.tmx");
        sections.add("tilemap/dirt_2.tmx");

        for (int x = 0; x < 1; x++) {
            for (int y = 0; y < SECTION_COUNT; y++) {
                TiledMap sectionTiledMap = new TmxMapLoader().load(sections.get((int) (Math.random() * sections.size())));
                addSection(tiledMap, sectionTiledMap, y * SECTION_HEIGHT, x * SECTION_WIDTH);
            }
        }

        TiledMapTileLayer newLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);


        // TODO add old map sections back in
        int offsetX = SECTION_WIDTH;

        for (int x = offsetX; x < oldLayer.getWidth() + offsetX; x++) {
            for (int y = 0; y < oldLayer.getHeight(); y++) {
                newLayer.setCell(x, y, oldLayer.getCell(x - offsetX, y));
            }
        }

        setMapProperties(tiledMap);
        return tiledMap;
    }

    public void setMapProperties(TiledMap map) {
        map.getProperties().put(MapProperties.WIDTH, SECTION_COUNT * SECTION_WIDTH);
        map.getProperties().put(MapProperties.HEIGHT, SECTION_COUNT * SECTION_HEIGHT);
        map.getProperties().put(MapProperties.TILE_SIZE, (int) TileManager.getTileSize());
    }

    /*
     * Populates the borders of the tile map layer with the given cell type.
     */
    private void populateLayer(TiledMapTileLayer layer, TiledMapTileLayer.Cell cell) {
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                if (row == 0 || row == layer.getHeight() - 1 || col == 0 || col == layer.getWidth() - 1) {
                    layer.setCell(col, row, cell);
                }
            }
        }
    }

    /*
     * Adds a smaller tile map to part of a larger one.
     */
    private void addSection(TiledMap map, TiledMap section, int startRow, int startCol) {
        for (int layerIndex = 0; layerIndex < map.getLayers().size(); layerIndex++) {
            TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerIndex);
            TiledMapTileLayer sectionLayer = (TiledMapTileLayer) section.getLayers().get(layerIndex);
            for (int row = 0; row < sectionLayer.getHeight(); row++) {
                for (int col = 0; col < sectionLayer.getWidth(); col++) {
                    layer.setCell(startCol + col, startRow + row, sectionLayer.getCell(col, row));
                }
            }
        }
    }
}