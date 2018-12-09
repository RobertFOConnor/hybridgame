package com.yellowbytestudios.hybrid.tile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.yellowbytestudios.hybrid.physics.Coin;
import com.yellowbytestudios.hybrid.physics.Enemy;
import com.yellowbytestudios.hybrid.physics.Exit;
import com.yellowbytestudios.hybrid.physics.atoms.PhysicsObject;

import java.util.ArrayList;

public class TileObjects {

    private static int tileSize = (int) TileManager.getTileSize();

    public static ArrayList<PhysicsObject> createObject(TileManager tm, int layerIndex) {
        ArrayList<PhysicsObject> array = new ArrayList<PhysicsObject>();

        if (layerIndex < tm.getLayerCount()) {
            TiledMapTileLayer layer = (TiledMapTileLayer) tm.getLayer(layerIndex);

            for (int col = 0; col < layer.getWidth(); col++) {
                for (int row = 0; row < layer.getHeight(); row++) {
                    TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                    if (cell != null) {
                        switch (layerIndex) {
                            case MapProps.ITEM_LAYER:
                                Sprite sprite = new Sprite(new Texture("textures/items/coin.png"));
                                sprite.setPosition(col * tileSize + sprite.getWidth() / 2, row * tileSize + sprite.getHeight() / 2);
                                array.add(new Coin(sprite));
                                break;
                            case MapProps.ENEMY_LAYER:
                                sprite = new Sprite(new Texture("textures/player/p_idle.png"));
                                sprite.setPosition(col * tileSize + tileSize / 2, row * tileSize + tileSize / 2);
                                sprite.setSize(tileSize, tileSize);
                                array.add(new Enemy(sprite));
                                break;
                        }
                    }
                }
            }
        }
        return array;
    }

    public static ArrayList<PhysicsObject> createObjectFromObjectLayer(TileManager tm, int layerIndex) {
        ArrayList<PhysicsObject> array = new ArrayList<PhysicsObject>();

        if (layerIndex < tm.getLayerCount()) {
            MapLayer layer = tm.getLayer(layerIndex);
            MapObjects objects = layer.getObjects();

            for (MapObject door : objects) {
                MapProperties properties = door.getProperties();
                float x = (Float) properties.get("x");
                float y = (Float) properties.get("y");
                String linkedMap = (String) properties.get("link");

                float linkX = 0;
                float linkY = 0;

                if (properties.get("linkX") != null && properties.get("linkY") != null) {
                    linkX = (Integer) properties.get("linkX");
                    linkY = (Integer) properties.get("linkY");
                }

                Sprite sprite = new Sprite(new Texture("textures/player/p_idle.png"));
                sprite.setPosition(x + tileSize / 2, y + tileSize / 2);
                sprite.setSize(tileSize, tileSize);
                array.add(new Exit(sprite, linkedMap, linkX, linkY));
            }
        }
        return array;
    }
}
