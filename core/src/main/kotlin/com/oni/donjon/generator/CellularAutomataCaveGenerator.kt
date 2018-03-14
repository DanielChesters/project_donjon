package com.oni.donjon.generator

import com.badlogic.gdx.math.MathUtils
import com.oni.donjon.map.TileType

/**
 * @author Daniel Chesters (on 09/05/2017).
 */
class CellularAutomataCaveGenerator(mapHeight: Int = 50, mapWidth: Int = 50) : MapGenerator(mapHeight, mapWidth) {

    init {
        this.tileTypes = Array(mapWidth) { arrayOfNulls<TileType>(mapHeight) }
    }

    override fun generate() {
        initiateMap()
        iteration(5)
        placeSpecialTile(TileType.STAIR_UP)
        placeSpecialTile(TileType.STAIR_DOWN)
    }

    private fun iteration(count: Int) {
        for (i in 0 until count) {
            iteration()
        }
    }

    private fun iteration() {
        for (x in 0 until mapWidth) {
            for (y in 0 until mapHeight) {
                val countWall = countWall(x, y)
                if (countWall >= 5) {
                    tileTypes[x][y] = TileType.WALL
                } else {
                    tileTypes[x][y] = TileType.GROUND
                }
            }
        }
    }

    private fun countWall(x: Int, y: Int): Int {
        var countWall = 0
        for (i in -1..1) {
            for (j in -1..1) {
                if (x + i < 0 || y + j < 0 || x + i > mapWidth - 1 || y + j > mapHeight - 1) {
                    countWall++
                } else {
                    countWall += if (tileTypes[x + i][y + j] === TileType.WALL) 1 else 0
                }
            }
        }
        return countWall
    }

    private fun initiateMap() {
        for (x in 0 until mapWidth) {
            for (y in 0 until mapHeight) {
                if (x == 0 || y == 0 || x == mapWidth - 1 || y == mapHeight - 1) {
                    tileTypes[x][y] = TileType.WALL
                } else {
                    if (MathUtils.randomBoolean(0.4f)) {
                        tileTypes[x][y] = TileType.WALL
                    } else {
                        tileTypes[x][y] = TileType.GROUND
                    }
                }
            }
        }
    }
}
