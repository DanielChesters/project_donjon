package com.oni.donjon.generator

import com.oni.donjon.map.TileType
import com.oni.donjon.map.TileType.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
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

        val groundTileCount = tileTypeList.filter { tileType ->
            Arrays.asList(GROUND, STAIR_DOWN, STAIR_UP)
                    .contains(tileType)
        }.count()

        assertEquals(1000, groundTileCount)

        assertEquals((mapHeight * mapWidth - 1000),
                tileTypeList.filter { tileType -> tileType == WALL }.count())

        assertEquals(1,
                tileTypeList.filter { tileType -> tileType == STAIR_UP }.count())
        assertEquals(1,
                tileTypeList.filter { tileType -> tileType == STAIR_DOWN }.count())
    }

    @Test
    fun testGeneratorError() {
        assertThrows(IllegalArgumentException::class.java) {
            DrunkardsWalkCaveGenerator(10, 10, 1000)
        }
    }
}
