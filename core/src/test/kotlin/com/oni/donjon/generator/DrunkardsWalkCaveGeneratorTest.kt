package com.oni.donjon.generator

import com.oni.donjon.map.TileType
import com.oni.donjon.map.TileType.GROUND
import com.oni.donjon.map.TileType.STAIR_DOWN
import com.oni.donjon.map.TileType.STAIR_UP
import com.oni.donjon.map.TileType.WALL
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

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
        for (x in 0 until mapWidth) {
            (0 until mapHeight).mapTo(tileTypeList) { mapGenerator.tileTypes[x][it]!! }
        }

        val tileCount = tileTypeList.count()

        val groundTileCount = tileTypeList.filter { tileType ->
            listOf(GROUND, STAIR_DOWN, STAIR_UP)
                .contains(tileType)
        }.count()

        assertAll(
            Executable { assertEquals(50, mapHeight.toLong()) },
            Executable { assertEquals(50, mapWidth.toLong()) },
            Executable { assertEquals((mapHeight * mapWidth), tileCount) },
            Executable { assertEquals(1000, groundTileCount) },
            Executable {
                assertEquals(
                    (mapHeight * mapWidth - 1000),
                    tileTypeList.filter { tileType -> tileType == WALL }.count()
                )
            },
            Executable {
                assertEquals(
                    1,
                    tileTypeList.filter { tileType -> tileType == STAIR_UP }.count()
                )
            },
            Executable {
                assertEquals(
                    1,
                    tileTypeList.filter { tileType -> tileType == STAIR_DOWN }.count()
                )
            }
        )
    }

    @ParameterizedTest
    @CsvSource("20, 20, 100", "30, 30, 200")
    fun `generate a map with parameters`(mapHeight: Int, mapWidth: Int, nbFloorTiles: Int) {
        val mapGenerator = DrunkardsWalkCaveGenerator(mapHeight, mapWidth, nbFloorTiles)
        mapGenerator.generate()
        val tileTypeList = ArrayList<TileType>()
        for (x in 0 until mapGenerator.mapWidth) {
            (0 until mapGenerator.mapHeight).mapTo(tileTypeList) { mapGenerator.tileTypes[x][it]!! }
        }

        val tileCount = tileTypeList.count()

        val groundTileCount = tileTypeList.filter { tileType ->
            listOf(GROUND, STAIR_DOWN, STAIR_UP)
                .contains(tileType)
        }.count()

        assertAll(
            Executable { assertEquals(mapHeight, mapGenerator.mapHeight) },
            Executable { assertEquals(mapWidth, mapGenerator.mapWidth) },
            Executable { assertEquals((mapHeight * mapWidth), tileCount) },
            Executable { assertEquals(nbFloorTiles, groundTileCount) },
            Executable {
                assertEquals(
                    (mapHeight * mapWidth - nbFloorTiles),
                    tileTypeList.filter { tileType -> tileType == WALL }.count()
                )
            },
            Executable {
                assertEquals(
                    1,
                    tileTypeList.filter { tileType -> tileType == STAIR_UP }.count()
                )
            },
            Executable {
                assertEquals(
                    1,
                    tileTypeList.filter { tileType -> tileType == STAIR_DOWN }.count()
                )
            }
        )
    }

    @ParameterizedTest
    @CsvSource("10, 10, 1000", "5, 5, 200", "40, 40, 50000")
    fun `should raise IllegalArgumentException`(mapHeight: Int, mapWidth: Int, nbFloorTiles: Int) {
        assertThrows(IllegalArgumentException::class.java) {
            DrunkardsWalkCaveGenerator(mapHeight, mapWidth, nbFloorTiles)
        }
    }
}
