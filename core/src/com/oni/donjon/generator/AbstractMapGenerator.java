package com.oni.donjon.generator;

import com.badlogic.gdx.math.MathUtils;
import com.oni.donjon.map.TileType;

/**
 * @author Daniel Chesters (on 10/05/2017).
 */
public abstract class AbstractMapGenerator implements MapGenerator {
    protected int mapHeight;
    protected int mapWidth;
    protected TileType[][] tileTypes;

    protected void placeSpecialTile(TileType tileType) {
        int x;
        int y;
        do {
            x = MathUtils.random(0, mapWidth - 1);
            y = MathUtils.random(0, mapWidth - 1);
        } while (!TileType.GROUND.equals(tileTypes[x][y]));
        tileTypes[x][y] = tileType;
    }

    @Override
    public TileType[][] getTileTypes() {
        return tileTypes;
    }

    @Override
    public int getMapHeight() {
        return mapHeight;
    }

    @Override
    public int getMapWidth() {
        return mapWidth;
    }
}
