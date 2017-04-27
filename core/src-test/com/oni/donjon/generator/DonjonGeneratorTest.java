package com.oni.donjon.generator;

import com.oni.donjon.map.TileType;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.oni.donjon.map.TileType.*;

/**
 * @author Daniel Chesters (on 27/04/2017).
 */
public class DonjonGeneratorTest {
    @Test
    public void testGeneratorNominal() {
        MapGenerator mapGenerator = new DonjonGenerator();
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

        final long countDoor =
            tileTypeList.stream().filter(tileType -> tileType.equals(DOOR_CLOSE)).count();

        Assert.assertTrue(countDoor <= 20);
        Assert.assertEquals(1,
            tileTypeList.stream().filter(tileType -> tileType.equals(STAIR_UP)).count());
        Assert.assertEquals(1,
            tileTypeList.stream().filter(tileType -> tileType.equals(STAIR_DOWN)).count());
    }
}
