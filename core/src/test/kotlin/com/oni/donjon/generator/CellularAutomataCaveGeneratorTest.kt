package com.oni.donjon.generator

import com.oni.donjon.map.TileType
import com.oni.donjon.map.TileType.STAIR_DOWN
import com.oni.donjon.map.TileType.STAIR_UP
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

/**
 * @author Daniel Chesters (on 21/04/2017).
 */
class CellularAutomataCaveGeneratorTest {

    @Test
    fun `generate a map with default parameters`() {
        val mapGenerator = CellularAutomataCaveGenerator()
        mapGenerator.generate()
        val mapHeight = mapGenerator.mapHeight
        val mapWidth = mapGenerator.mapWidth

        val tileTypeList = ArrayList<TileType>()
        for (x in 0..mapWidth - 1) {
            (0..mapHeight - 1).mapTo(tileTypeList) { mapGenerator.tileTypes[x][it]!! }
        }

        val tileCount = tileTypeList.count()

        assertAll(
                Executable { assertEquals(50, mapHeight.toLong()) },
                Executable { assertEquals(50, mapWidth.toLong()) },
                Executable { assertEquals((mapHeight * mapWidth), tileCount) },
                Executable { assertEquals(1,
                        tileTypeList.filter { tileType -> tileType == STAIR_UP }.count()) },
                Executable { assertEquals(1,
                        tileTypeList.filter { tileType -> tileType == STAIR_DOWN }.count()) }
        )
    }
}
