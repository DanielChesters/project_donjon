package com.oni.donjon.generator;

import com.badlogic.gdx.math.MathUtils;
import com.oni.donjon.map.TileType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Chesters (on 24/11/16).
 */
public class DrunkardsWalkCaveGenerator extends AbstractMapGenerator {
    private int nbFloorTiles;

    public DrunkardsWalkCaveGenerator() {
        this(50, 50, 1000);
    }

    public DrunkardsWalkCaveGenerator(int mapHeight, int mapWidth, int nbFloorTiles) {

        if (nbFloorTiles >= mapHeight * mapWidth) {
            throw new IllegalArgumentException("Too much floor tiles or map size too small");
        }

        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.nbFloorTiles = nbFloorTiles;
        this.tileTypes = new TileType[mapWidth][mapHeight];
    }

    @Override public void generate() {
        placeWalls();

        placeGrounds();

        placeSpecialTile(TileType.STAIR_UP);
        placeSpecialTile(TileType.STAIR_DOWN);
    }

    private void placeGrounds() {
        int x = MathUtils.random(1, mapWidth - 2);
        int y = MathUtils.random(1, mapHeight - 2);
        tileTypes[x][y] = TileType.GROUND;

        for (int i = 1; i < nbFloorTiles; i++) {
            do {
                switch (Direction.randomDirection()) {
                    case NORTH:
                        y = MathUtils.clamp(y + 1, 1, mapHeight - 2);
                        break;
                    case SOUTH:
                        y = MathUtils.clamp(y - 1, 1, mapHeight - 2);
                        break;
                    case EAST:
                        x = MathUtils.clamp(x + 1, 1, mapHeight - 2);
                        break;
                    case WEST:
                        x = MathUtils.clamp(x - 1, 1, mapHeight - 2);
                        break;
                }
            } while (!TileType.WALL.equals(tileTypes[x][y]));
            tileTypes[x][y] = TileType.GROUND;
        }
    }

    private void placeWalls() {
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                tileTypes[x][y] = TileType.WALL;
            }
        }
    }

    enum Direction {
        NORTH, EAST, SOUTH, WEST;

        private static final List<Direction> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();

        public static Direction randomDirection() {
            return VALUES.get(MathUtils.random(SIZE - 1));
        }
    }
}
