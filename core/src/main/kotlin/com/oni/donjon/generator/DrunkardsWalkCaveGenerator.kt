package com.oni.donjon.generator

import com.badlogic.gdx.math.MathUtils
import com.oni.donjon.map.TileType
import java.util.*

/**
 * @author Daniel Chesters (on 24/11/16).
 */
class DrunkardsWalkCaveGenerator @JvmOverloads constructor(mapHeight: Int = 50, mapWidth: Int = 50, private val nbFloorTiles: Int = 1000) : MapGenerator() {

    init {

        if (nbFloorTiles >= mapHeight * mapWidth) {
            throw IllegalArgumentException("Too much floor tiles or map size too small")
        }

        this.mapHeight = mapHeight
        this.mapWidth = mapWidth
        this.tileTypes = Array(mapWidth) { arrayOfNulls<TileType>(mapHeight) }
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
        tileTypes!![x][y] = TileType.GROUND

        for (i in 1..nbFloorTiles - 1) {
            do {
                when (Direction.randomDirection()) {
                    DrunkardsWalkCaveGenerator.Direction.NORTH -> y = MathUtils.clamp(y + 1, 1, mapHeight - 2)
                    DrunkardsWalkCaveGenerator.Direction.SOUTH -> y = MathUtils.clamp(y - 1, 1, mapHeight - 2)
                    DrunkardsWalkCaveGenerator.Direction.EAST -> x = MathUtils.clamp(x + 1, 1, mapHeight - 2)
                    DrunkardsWalkCaveGenerator.Direction.WEST -> x = MathUtils.clamp(x - 1, 1, mapHeight - 2)
                }
            } while (TileType.WALL != tileTypes!![x][y])
            tileTypes!![x][y] = TileType.GROUND
        }
    }

    private fun placeWalls() {
        for (x in 0..mapWidth - 1) {
            for (y in 0..mapHeight - 1) {
                tileTypes!![x][y] = TileType.WALL
            }
        }
    }

    internal enum class Direction {
        NORTH, EAST, SOUTH, WEST;


        companion object {

            private val VALUES = Collections.unmodifiableList(Arrays.asList(*values()))
            private val SIZE = VALUES.size

            fun randomDirection(): Direction {
                return VALUES[MathUtils.random(SIZE - 1)]
            }
        }
    }
}
