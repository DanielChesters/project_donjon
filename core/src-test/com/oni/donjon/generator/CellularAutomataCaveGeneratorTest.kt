package com.oni.donjon.generator

import com.oni.donjon.map.TileType
import com.oni.donjon.map.TileType.STAIR_DOWN
import com.oni.donjon.map.TileType.STAIR_UP
import org.junit.Assert
import org.junit.Test

/**
 * @author Daniel Chesters (on 21/04/2017).
 */
class CellularAutomataCaveGeneratorTest {

    @Test
    fun testGeneratorNominal() {
        val mapGenerator = CellularAutomataCaveGenerator()
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

        Assert.assertEquals(1,
                tileTypeList.filter { tileType -> tileType == STAIR_UP }.count())
        Assert.assertEquals(1,
                tileTypeList.filter { tileType -> tileType == STAIR_DOWN }.count())
    }
}
