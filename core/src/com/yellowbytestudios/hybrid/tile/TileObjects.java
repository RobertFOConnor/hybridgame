package com.yellowbytestudios.hybrid.tile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.yellowbytestudios.hybrid.physics.Enemy;
import com.yellowbytestudios.hybrid.physics.Exit;
import com.yellowbytestudios.hybrid.physics.atoms.PhysicsObject;

import java.util.ArrayList;

public class TileObjects {

    private static int tileSize = (int) TileManager.getTileSize();

    public static ArrayList<PhysicsObject> createObject(TileManager tm, int layerIndex) {
        ArrayList<PhysicsObject> array = new ArrayList<PhysicsObject>();

        if (layerIndex < tm.getLayerCount()) {
            TiledMapTileLayer layer = tm.getLayer(layerIndex);

            for (int col = 0; col < layer.getWidth(); col++) {
                for (int row = 0; row < layer.getHeight(); row++) {
                    TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                    if (cell != null) {
                        switch (layerIndex) {
                            case LayerNames.EVENT_LAYER:
                                Sprite sprite = new Sprite(new Texture("textures/player/p_idle.png"));
                                sprite.setPosition(col * tileSize + tileSize / 2, row * tileSize + tileSize / 2);
                                sprite.setSize(tileSize, tileSize);
                                array.add(new Exit(sprite));
                            case LayerNames.ENEMY_LAYER:
                                sprite = new Sprite(new Texture("textures/player/p_idle.png"));
                                sprite.setPosition(col * tileSize + tileSize / 2, row * tileSize + tileSize / 2);
                                sprite.setSize(tileSize, tileSize);
                                array.add(new Enemy(sprite));
                        }
                    }
                }
            }
        }
        return array;
    }
}
