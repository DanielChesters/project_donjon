package com.oni.donjon.generator

import com.badlogic.gdx.math.MathUtils
import com.oni.donjon.map.TileType

/**
 * @author Daniel Chesters (on 24/11/16).
 */
class DrunkardsWalkCaveGenerator(
    mapHeight: Int = 50,
    mapWidth: Int = 50,
    private val nbFloorTiles: Int = 1000
) : MapGenerator(mapHeight, mapWidth) {

    init {

        if (nbFloorTiles >= mapHeight * mapWidth) {
            throw IllegalArgumentException("Too much floor tiles or map size too small")
        }

        this.tileTypes = Array(mapWidth) { arrayOfNulls(mapHeight) }
    }

    override fun generate() {
        placeWalls()

        placeGrounds()

        placeSpecialTile(TileType.STAIR_UP)
        placeSpecialTile(TileType.STAIR_DOWN)
    }

    private fun placeGrounds() {
        var x = MathUtils.random(1, mapWidth - 2)
        var y = MathUtils.random(1, mapHeight - 2)
        tileTypes[x][y] = TileType.GROUND

        (1 until nbFloorTiles).forEach { _ ->
            do {
                when (Direction.randomDirection()) {
                    Direction.NORTH -> y = MathUtils.clamp(y + 1, 1, mapHeight - 2)
                    Direction.SOUTH -> y = MathUtils.clamp(y - 1, 1, mapHeight - 2)
                    Direction.EAST -> x = MathUtils.clamp(x + 1, 1, mapHeight - 2)
                    Direction.WEST -> x = MathUtils.clamp(x - 1, 1, mapHeight - 2)
                }
            } while (TileType.WALL != tileTypes[x][y])
            tileTypes[x][y] = TileType.GROUND
        }
    }

    private fun placeWalls() {
        for (x in 0 until mapWidth) {
            for (y in 0 until mapHeight) {
                tileTypes[x][y] = TileType.WALL
            }
        }
    }

    internal enum class Direction {
        NORTH, EAST, SOUTH, WEST;

        companion object {

            private val VALUES = values().toList()
            private val SIZE = VALUES.size

            fun randomDirection(): Direction {
                return VALUES[MathUtils.random(SIZE - 1)]
            }
        }
    }
}
