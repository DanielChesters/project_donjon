package com.oni.donjon.generator

import com.oni.donjon.map.TileType
import com.oni.donjon.map.TileType.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

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
        assertEquals(50, mapHeight.toLong())
        assertEquals(50, mapWidth.toLong())
        val tileTypeList = ArrayList<TileType>()
        for (x in 0..mapWidth - 1) {
            (0..mapWidth - 1).mapTo(tileTypeList) {
                mapGenerator.tileTypes[x][it]!!
            }
        }

        val tileCount = tileTypeList.count()

        assertEquals((mapHeight * mapWidth), tileCount)

        val countDoor = tileTypeList.filter { tileType -> tileType == DOOR_CLOSE }.count()

        assertTrue(countDoor <= 20)
        assertEquals(1,
                tileTypeList.filter { tileType -> tileType == STAIR_UP }.count())
        assertEquals(1,
                tileTypeList.filter { tileType -> tileType == STAIR_DOWN }.count())
    }
}
