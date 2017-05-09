package com.oni.donjon.generator;

import com.badlogic.gdx.math.MathUtils;
import com.oni.donjon.map.TileType;

/**
 * @author Daniel Chesters (on 09/05/2017).
 */
public class CellularAutomataCaveGenerator implements MapGenerator {
    private int mapHeight;
    private int mapWidth;
    private TileType[][] tileTypes;

    public CellularAutomataCaveGenerator() {
        this(50, 50);
    }

    public CellularAutomataCaveGenerator(int mapHeight, int mapWidth) {
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.tileTypes = new TileType[mapWidth][mapHeight];
    }

    @Override public TileType[][] getTileTypes() {
        return tileTypes;
    }

    @Override public void generate() {
        initiateMap();
        iteration(5);
        placeSpecialTile(TileType.STAIR_UP);
        placeSpecialTile(TileType.STAIR_DOWN);
    }

    private void iteration(int count) {
        for (int i = 0; i < count; i++) {
            iteration();
        }
    }

    private void iteration() {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                int countWall = countWall(x, y);
                if (countWall >= 5) {
                    tileTypes[x][y] = TileType.WALL;
                } else {
                    tileTypes[x][y] = TileType.GROUND;
                }
            }
        }
    }

    private int countWall(int x, int y) {
        int countWall = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x + i < 0 || y + j < 0 || x + i > mapWidth - 1 || y + j > mapHeight - 1) {
                    countWall++;
                } else {
                    countWall += tileTypes[x + i][y + j] == TileType.WALL ? 1 : 0;
                }
            }
        }
        return countWall;
    }

    private void initiateMap() {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                if (x == 0 || y == 0 || x == mapWidth - 1 || y == mapHeight - 1) {
                    tileTypes[x][y] = TileType.WALL;
                } else {
                    if (MathUtils.randomBoolean(0.4f)) {
                        tileTypes[x][y] = TileType.WALL;
                    } else {
                        tileTypes[x][y] = TileType.GROUND;
                    }
                }
            }
        }
    }

    private void placeSpecialTile(TileType tileType) {
        int x;
        int y;
        do {
            x = MathUtils.random(0, mapWidth - 1);
            y = MathUtils.random(0, mapWidth - 1);
        } while (!TileType.GROUND.equals(tileTypes[x][y]));
        tileTypes[x][y] = tileType;
    }

    @Override public int getMapHeight() {
        return mapHeight;
    }

    @Override public int getMapWidth() {
        return mapWidth;
    }
}
