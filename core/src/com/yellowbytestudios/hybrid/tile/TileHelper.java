package com.yellowbytestudios.hybrid.tile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.yellowbytestudios.hybrid.media.Assets;

public class TileHelper {

    public static TiledMapTileLayer.Cell getCellByName(int tileName) {
        switch (tileName) {
            case TileNames.WALL_TILE:
                return getCell(0, 0, tileName, (int) TileManager.getTileSize());
        }
        return null;
    }

    /*
     * Builds a tile cell.
     */
    private static TiledMapTileLayer.Cell getCell(int regX, int regY, int ID, int tileSize) {
        Texture tileSheet = Assets.getTexture("tilemap/tilesheet.png");
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        StaticTiledMapTile t = new StaticTiledMapTile(new TextureRegion(tileSheet, regX, regY, tileSize, tileSize));
        t.setId(ID);
        cell.setTile(t);
        return cell;
    }
}
