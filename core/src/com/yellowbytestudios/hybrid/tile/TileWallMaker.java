package com.yellowbytestudios.hybrid.tile;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class TileWallMaker {

    private ArrayList<Vector2[]> lines;
    private boolean shouldDrawVector = false;
    private boolean lineStarted = false;
    private int lineCount = 0;
    private float tileSize = 80;
    private float lineMargin = 10;
    private int startCol;
    private int roomWidth;

    public TileWallMaker(int startCol, int roomWidth) {
        this.startCol = startCol;
        this.roomWidth = roomWidth;
    }


    private Vector2 getTop_R(int row, int col) {
        return new Vector2(col * tileSize + tileSize, row * tileSize + tileSize);
    }

    private Vector2 getTop_L(int row, int col) {
        return new Vector2(col * tileSize, row * tileSize + tileSize);
    }

    private Vector2 getBot_L(int row, int col) {
        return new Vector2(col * tileSize, row * tileSize);
    }

    public ArrayList<Vector2[]> createWalls(TiledMapTileLayer layer) {
        lines = new ArrayList<Vector2[]>();
        tileSize = (int) layer.getTileWidth();

        Vector2 start = new Vector2();
        Vector2 finish = new Vector2();

        int width = roomWidth > 0 ? startCol + roomWidth : layer.getWidth();

        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = startCol; col < width; col++) {

                //Get cell at (row, col) position.
                Cell cell = layer.getCell(col, row);
                //Get cell above current cell.
                Cell above_cell = layer.getCell(col, row + 1);
                //Set the main cell to our current cell.
                Cell main_cell = cell;

                shouldDrawVector = false;

                //Check if cell is a tile.
                if (cell != null) {
                    //If cell is a tile and there is nothing above it, we should draw.

                    if (above_cell == null) {
                        if (!lineStarted) {
                            start = getTop_L(row, col);
                            finish = getTop_R(row, col);
                        } else {
                            finish = getTop_R(row, col);
                        }
                        lineStarted = true;

                    } else {
                        checkDrawVector();
                    }

                } else {

                    //If cell is empty but there is a tile above it, we should draw.
                    if (above_cell != null) {
                        if (!lineStarted) {
                            start = getTop_L(row, col);
                            finish = getTop_R(row, col);
                        } else {
                            finish = getTop_R(row, col);
                        }
                        lineStarted = true;
                        main_cell = above_cell;

                    } else {
                        checkDrawVector();
                    }
                }

                if (shouldDrawVector) {
                    drawVector(start.sub(-lineMargin, 0), finish.sub(lineMargin, 0));
                }
            }
        }
        lineStarted = false;


        for (int col = startCol; col < width; col++) {
            for (int row = 0; row < layer.getHeight(); row++) {

                Cell cell = layer.getCell(col, row);
                Cell left_cell = layer.getCell(col - 1, row);
                Cell main_cell = cell;
                shouldDrawVector = false;


                if (cell != null) {
                    if (left_cell == null && col != 0) {
                        if (!lineStarted) {
                            start = getTop_L(row, col);
                            finish = getBot_L(row, col);
                        } else {
                            start = getTop_L(row, col);
                        }
                        lineStarted = true;

                    } else {
                        checkDrawVector();
                    }
                } else {
                    //If cell is empty but there is a tile above it, we should draw.
                    if (left_cell != null) {
                        if (!lineStarted) {
                            start = getTop_L(row, col);
                            finish = getBot_L(row, col);
                        } else {
                            start = getTop_L(row, col);
                        }
                        lineStarted = true;
                        main_cell = left_cell;


                    } else {
                        checkDrawVector();
                    }
                }

                if (shouldDrawVector) {
                    drawVector(start.sub(0, lineMargin), finish.sub(0, -lineMargin));
                }
            }
        }
        System.out.println("Wall count: " + lineCount);
        return lines;
    }

    private void checkDrawVector() {
        if (lineStarted) {
            shouldDrawVector = true;
            lineStarted = false;
        }
    }

    private void drawVector(Vector2 start, Vector2 finish) {
        Vector2[] v;
        v = new Vector2[2];
        v[0] = start;
        v[1] = finish;
        lines.add(v);
        lineCount++;
    }
}
