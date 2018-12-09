package com.yellowbytestudios.hybrid.tile;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.BIT_ENEMY;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.BIT_PLAYER;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.BIT_WALL;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.PPM;

public class TilePhysicsWallMaker {

    private FixtureDef fdef;
    private boolean shouldDrawVector = false;
    private boolean lineStarted = false;
    int wallCount = 0;

    int lineCountX = 0, lineCountY = 0;
    float tileSize = TileManager.getTileSize();
    float tileSizePPM = tileSize / PPM;

    float leftSide = (-tileSize / 2) / (PPM);
    float rightSide = (tileSize / 2) / (PPM);
    //BOTTOM CORNERS
    final Vector2 bot_L = new Vector2(leftSide, leftSide);
    final Vector2 bot_R = new Vector2(rightSide, leftSide);

    //TOP CORNERS
    final Vector2 top_L = new Vector2(leftSide, rightSide);
    final Vector2 top_R = new Vector2(rightSide, rightSide);


    public ArrayList<Body> createWalls(World world, TiledMap tileMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get(MapProps.WALL_LAYER);

        Vector2 start = new Vector2();
        Vector2 finish = new Vector2();

        BodyDef bdef = new BodyDef();
        fdef = new FixtureDef();

        ArrayList<Body> walls = new ArrayList<Body>();

        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                //Get cell at (row, col) position.
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                //Get cell above current cell.
                TiledMapTileLayer.Cell above_cell = layer.getCell(col, row + 1);
                //Set the main cell to our current cell.
                TiledMapTileLayer.Cell main_cell = cell;


                shouldDrawVector = false;

                //Check if cell is a tile.
                if (cell != null) {
                    //If cell is a tile and there is nothing above it, we should draw.

                    if (above_cell == null) {
                        if (!lineStarted) {
                            start = top_L.cpy();
                            finish = top_R.cpy();
                        } else {
                            finish = top_R.cpy().add(lineCountX * tileSizePPM, 0);
                        }
                        lineStarted = true;
                        lineCountX++;

                    } else {
                        checkDrawVector();
                    }

                } else {

                    //If cell is empty but there is a tile above it, we should draw.
                    if (above_cell != null) {
                        if (!lineStarted) {
                            start = top_L.cpy();
                            finish = top_R.cpy();
                        } else {
                            finish = top_R.cpy().add(lineCountX * tileSizePPM, 0);
                        }
                        lineStarted = true;
                        lineCountX++;
                        main_cell = above_cell;
                    } else {
                        checkDrawVector();
                    }
                }


                if (shouldDrawVector) {
                    walls.add(drawVector(world, start, finish, row, col));
                    lineCountX = 0;
                }
            }
        }
        lineStarted = false;
        lineCountX = 0;


        for (int col = 0; col < layer.getWidth(); col++) {
            for (int row = 0; row < layer.getHeight(); row++) {

                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                TiledMapTileLayer.Cell left_cell = layer.getCell(col - 1, row);
                TiledMapTileLayer.Cell main_cell = cell;
                shouldDrawVector = false;


                if (cell != null) {
                    if (left_cell == null && col != 0) {
                        if (!lineStarted) {
                            start = top_L.cpy();
                            finish = bot_L.cpy();
                        } else {
                            start = top_L.cpy().add(0, lineCountY * tileSizePPM);
                        }
                        lineStarted = true;
                        lineCountY++;
                    } else {
                        checkDrawVector();
                    }
                } else {
                    //If cell is empty but there is a tile above it, we should draw.
                    if (left_cell != null) {
                        if (!lineStarted) {
                            start = top_L.cpy();
                            finish = bot_L.cpy();
                        } else {
                            start = top_L.cpy().add(0, lineCountY * tileSizePPM);
                        }
                        lineStarted = true;
                        lineCountY++;
                        main_cell = left_cell;

                    } else {
                        checkDrawVector();
                    }
                }

                if (shouldDrawVector) {
                    walls.add(drawVector(world, start, finish, row, col));
                    lineCountY = 0;
                }
            }
        }

        return walls;
    }

    private void checkDrawVector() {
        if (lineStarted) {
            shouldDrawVector = true;
            lineStarted = false;
        }
    }

    private Body drawVector(World world, Vector2 start, Vector2 finish, int row, int col) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(((col * tileSizePPM - lineCountX * tileSizePPM)) + tileSizePPM / 2, ((row * tileSizePPM - lineCountY * tileSizePPM)) + tileSizePPM / 2);

        ChainShape chainShape = new ChainShape();

        Vector2[] v;
        v = new Vector2[2];
        v[0] = start;
        v[1] = finish;


        chainShape.createChain(v);
        fdef.density = 1f;
        fdef.shape = chainShape;
        fdef.filter.categoryBits = BIT_WALL;
        fdef.filter.maskBits = BIT_PLAYER | BIT_ENEMY;

        Body wallsBody = world.createBody(bdef);
        wallsBody.createFixture(fdef).setUserData("ground");
        chainShape.dispose();

        wallCount++;

        return wallsBody;
    }
}
