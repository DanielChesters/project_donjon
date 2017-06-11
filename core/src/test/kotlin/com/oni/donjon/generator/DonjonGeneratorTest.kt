package com.oni.donjon.generator

import com.oni.donjon.map.TileType
import com.oni.donjon.map.TileType.*
import org.junit.Assert
import org.junit.Test

/**
 * @author Daniel Chesters (on 27/04/2017).
 */
class DonjonGeneratorTest {
    @Test
    fun testGeneratorNominal() {
        val mapGenerator = DonjonGenerator()
        mapGenerator.generate()
        val mapHeight = mapGenerator.mapHeight
        val mapWidth = mapGenerator.mapWidth
        Assert.assertEquals(50, mapHeight.toLong())
        Assert.assertEquals(50, mapWidth.toLong())
        val tileTypeList = ArrayList<TileType>()
        for (x in 0..mapWidth - 1) {
            (0..mapWidth - 1).mapTo(tileTypeList) {
                mapGenerator.tileTypes[x][it]!!
            }
        }

        val tileCount = tileTypeList.count()

        Assert.assertEquals((mapHeight * mapWidth), tileCount)

        val countDoor = tileTypeList.filter { tileType -> tileType == DOOR_CLOSE }.count()

        Assert.assertTrue(countDoor <= 20)
        Assert.assertEquals(1,
                tileTypeList.filter { tileType -> tileType == STAIR_UP }.count())
        Assert.assertEquals(1,
                tileTypeList.filter { tileType -> tileType == STAIR_DOWN }.count())
    }
}
