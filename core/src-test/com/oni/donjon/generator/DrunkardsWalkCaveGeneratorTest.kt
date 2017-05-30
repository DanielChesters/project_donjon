package com.oni.donjon.generator

import com.oni.donjon.map.TileType
import com.oni.donjon.map.TileType.*
import org.junit.Assert
import org.junit.Test
import java.util.Arrays
import kotlin.collections.ArrayList

/**
 * @author Daniel Chesters (on 21/04/2017).
 */
class DrunkardsWalkCaveGeneratorTest {

    @Test
    fun testGeneratorNominal() {
        val mapGenerator = DrunkardsWalkCaveGenerator()
        mapGenerator.generate()
        val mapHeight = mapGenerator.getMapHeight()
        val mapWidth = mapGenerator.getMapWidth()
        Assert.assertEquals(50, mapHeight.toLong())
        Assert.assertEquals(50, mapWidth.toLong())
        val tileTypeList = ArrayList<TileType>()
        for (x in 0..mapWidth - 1) {
            (0..mapWidth - 1).mapTo(tileTypeList) { mapGenerator.getTileTypes()[x][it] }
        }

        val tileCount = tileTypeList.count()

        Assert.assertEquals((mapHeight * mapWidth), tileCount)

        val groundTileCount = tileTypeList.filter { tileType ->
            Arrays.asList(GROUND, STAIR_DOWN, STAIR_UP)
                    .contains(tileType)
        }.count()

        Assert.assertEquals(1000, groundTileCount)

        Assert.assertEquals((mapHeight * mapWidth - 1000),
                tileTypeList.filter { tileType -> tileType == WALL }.count())

        Assert.assertEquals(1,
                tileTypeList.filter { tileType -> tileType == STAIR_UP }.count())
        Assert.assertEquals(1,
                tileTypeList.filter { tileType -> tileType == STAIR_DOWN }.count())
    }

    @Test(expected = IllegalArgumentException::class)
    fun testGeneratorError() {
        DrunkardsWalkCaveGenerator(10, 10, 1000)
    }
}
