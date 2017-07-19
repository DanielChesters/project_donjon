package com.oni.donjon.generator

import com.oni.donjon.map.TileType
import com.oni.donjon.map.TileType.STAIR_DOWN
import com.oni.donjon.map.TileType.STAIR_UP
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Daniel Chesters (on 21/04/2017).
 */
class CellularAutomataCaveGeneratorTest {

    @Test
    fun testGeneratorNominal() {
        val mapGenerator = CellularAutomataCaveGenerator()
        mapGenerator.generate()
        val mapHeight = mapGenerator.mapHeight
        val mapWidth = mapGenerator.mapWidth
        assertEquals(50, mapHeight.toLong())
        assertEquals(50, mapWidth.toLong())
        val tileTypeList = ArrayList<TileType>()
        for (x in 0..mapWidth - 1) {
            (0..mapWidth - 1).mapTo(tileTypeList) { mapGenerator.tileTypes[x][it]!! }
        }

        val tileCount = tileTypeList.count()

        assertEquals((mapHeight * mapWidth), tileCount)

        assertEquals(1,
                tileTypeList.filter { tileType -> tileType == STAIR_UP }.count())
        assertEquals(1,
                tileTypeList.filter { tileType -> tileType == STAIR_DOWN }.count())
    }
}
