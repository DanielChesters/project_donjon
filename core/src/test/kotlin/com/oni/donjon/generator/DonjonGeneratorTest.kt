package com.oni.donjon.generator

import com.oni.donjon.map.TileType
import com.oni.donjon.map.TileType.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.provider.CsvSource

/**
 * @author Daniel Chesters (on 27/04/2017).
 */
class DonjonGeneratorTest {
    @Test
    fun `generate a map with default parameters`() {
        val mapGenerator = DonjonGenerator()
        mapGenerator.generate()
        val mapHeight = mapGenerator.mapHeight
        val mapWidth = mapGenerator.mapWidth

        val tileTypeList = ArrayList<TileType>()
        for (x in 0 until mapWidth) {
            (0 until mapHeight).mapTo(tileTypeList) {
                mapGenerator.tileTypes[x][it]!!
            }
        }

        val tileCount = tileTypeList.count()

        val countDoor = tileTypeList.filter { tileType -> tileType == DOOR_CLOSE }.count()

        assertAll(
                Executable { assertEquals(50, mapHeight.toLong()) },
                Executable { assertEquals(50, mapWidth.toLong()) },
                Executable { assertEquals((mapHeight * mapWidth), tileCount) },
                Executable { assertTrue(countDoor <= 20) },
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

    @ParameterizedTest
    @CsvSource("15, 30, 30","20, 40, 40", "50, 100, 100")
    fun `generate a map with parameters`(nbRoom: Int, mapHeight: Int, mapWidth: Int) {
        val mapGenerator = DonjonGenerator(nbRoom, 10, 6, mapHeight, mapWidth)
        mapGenerator.generate()

        val tileTypeList = ArrayList<TileType>()
        for (x in 0 until mapGenerator.mapWidth) {
            (0 until mapGenerator.mapHeight).mapTo(tileTypeList) {
                mapGenerator.tileTypes[x][it]!!
            }
        }

        val tileCount = tileTypeList.count()

        val countDoor = tileTypeList.filter { tileType -> tileType == DOOR_CLOSE }.count()

        assertAll(
                Executable { assertEquals(mapHeight, mapGenerator.mapHeight) },
                Executable { assertEquals(mapWidth, mapGenerator.mapWidth) },
                Executable { assertEquals((mapHeight * mapWidth), tileCount) },
                Executable { assertTrue(countDoor <= nbRoom * 2) },
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
}
