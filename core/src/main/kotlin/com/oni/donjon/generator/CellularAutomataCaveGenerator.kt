package com.oni.donjon.generator

import com.badlogic.gdx.math.MathUtils
import com.oni.donjon.map.TileType

/**
 * @author Daniel Chesters (on 09/05/2017).
 */
class CellularAutomataCaveGenerator(mapHeight: Int = 50, mapWidth: Int = 50) : MapGenerator(mapHeight, mapWidth) {

    init {
        this.tileTypes = Array(mapWidth) { arrayOfNulls(mapHeight) }
    }

    override fun generate() {
        initiateMap()
        iteration(DEFAULT_ITERATION)
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
                if (countWall >= LIMIT_COUNT_WALL) {
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
                countWall += addWallForCurrentTile(x, i, y, j)
            }
        }
        return countWall
    }

    private fun addWallForCurrentTile(x: Int, i: Int, y: Int, j: Int): Int {
        val outsideLimit = x + i < 0 || y + j < 0 || x + i > mapWidth - 1 || y + j > mapHeight - 1
        return if (outsideLimit || tileTypes[x + i][y + j] === TileType.WALL) {
            1
        } else {
            0
        }
    }

    private fun initiateMap() {
        for (x in 0 until mapWidth) {
            for (y in 0 until mapHeight) {
                initiateTile(x, y)
            }
        }
    }

    private fun initiateTile(x: Int, y: Int) {
        val outsideLimit = x == 0 || y == 0 || x == mapWidth - 1 || y == mapHeight - 1
        if (outsideLimit) {
            tileTypes[x][y] = TileType.WALL
        } else {
            if (MathUtils.randomBoolean(RATIO)) {
                tileTypes[x][y] = TileType.WALL
            } else {
                tileTypes[x][y] = TileType.GROUND
            }
        }
    }

    companion object {
        const val RATIO = 0.4f
        const val DEFAULT_ITERATION = 5
        const val LIMIT_COUNT_WALL = 5
    }
}
