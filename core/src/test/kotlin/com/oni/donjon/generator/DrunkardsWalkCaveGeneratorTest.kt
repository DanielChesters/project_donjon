package com.oni.donjon.generator

import com.oni.donjon.map.TileType
import com.oni.donjon.map.TileType.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.util.Arrays
import kotlin.collections.ArrayList

/**
 * @author Daniel Chesters (on 21/04/2017).
 */
class DrunkardsWalkCaveGeneratorTest {

    @Test
    fun `generate a map with default parameters`() {
        val mapGenerator = DrunkardsWalkCaveGenerator()
        mapGenerator.generate()
        val mapHeight = mapGenerator.mapHeight
        val mapWidth = mapGenerator.mapWidth
        val tileTypeList = ArrayList<TileType>()
        for (x in 0..mapWidth - 1) {
            (0..mapHeight - 1).mapTo(tileTypeList) { mapGenerator.tileTypes[x][it]!! }
        }

        val tileCount = tileTypeList.count()

        val groundTileCount = tileTypeList.filter { tileType ->
            Arrays.asList(GROUND, STAIR_DOWN, STAIR_UP)
                    .contains(tileType)
        }.count()

        assertAll(
                Executable { assertEquals(50, mapHeight.toLong()) },
                Executable { assertEquals(50, mapWidth.toLong()) },
                Executable { assertEquals((mapHeight * mapWidth), tileCount) },
                Executable { assertEquals(1000, groundTileCount) },
                Executable {
                    assertEquals((mapHeight * mapWidth - 1000),
                            tileTypeList.filter { tileType -> tileType == WALL }.count())
                },
                Executable {
                    assertEquals(1,
                            tileTypeList.filter { tileType -> tileType == STAIR_UP }.count())
                },
                Executable {
                    assertEquals(1,
                            tileTypeList.filter { tileType -> tileType == STAIR_DOWN }.count())
                }
        )
    }

    @Test
    fun `should raise IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            DrunkardsWalkCaveGenerator(10, 10, 1000)
        }
    }
}
