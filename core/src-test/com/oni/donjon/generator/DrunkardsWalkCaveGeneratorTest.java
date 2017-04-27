package com.oni.donjon.generator;

import com.oni.donjon.map.TileType;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.oni.donjon.map.TileType.*;

/**
 * @author Daniel Chesters (on 21/04/2017).
 */
public class DrunkardsWalkCaveGeneratorTest {

    @Test
    public void testGeneratorNominal() {
        MapGenerator mapGenerator = new DrunkardsWalkCaveGenerator();
        mapGenerator.generate();
        final int mapHeight = mapGenerator.getMapHeight();
        final int mapWidth = mapGenerator.getMapWidth();
        Assert.assertEquals(50, mapHeight);
        Assert.assertEquals(50, mapWidth);
        List<TileType> tileTypeList = new ArrayList<>();
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapWidth; y++) {
                tileTypeList.add(mapGenerator.getTileTypes()[x][y]);
            }
        }

        final long tileCount = tileTypeList.stream().count();

        Assert.assertEquals(mapHeight * mapWidth, tileCount);

        final long groundTileCount = tileTypeList.stream().filter(
            tileType -> Arrays.asList(GROUND, STAIR_DOWN, STAIR_UP)
                .contains(tileType)).count();

        Assert.assertEquals(1000, groundTileCount);

        Assert.assertEquals(mapHeight * mapWidth - 1000,
            tileTypeList.stream().filter(tileType -> tileType.equals(WALL)).count());

        Assert.assertEquals(1,
            tileTypeList.stream().filter(tileType -> tileType.equals(STAIR_UP)).count());
        Assert.assertEquals(1,
            tileTypeList.stream().filter(tileType -> tileType.equals(STAIR_DOWN)).count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGeneratorError() {
        new DrunkardsWalkCaveGenerator(10, 10, 1000);
    }
}
