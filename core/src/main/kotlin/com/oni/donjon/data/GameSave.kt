package com.oni.donjon.data

import com.badlogic.gdx.math.Vector2
import com.oni.donjon.map.TileType
import java.util.*

/**
 * @author Daniel Chesters (on 07/02/16).
 */
data class GameSave(
        var mapHeight: Int? = null,
        var mapWidth: Int? = null,
        var map: Array<Array<SavedTile?>>? = null,
        var playerPosition: Vector2 = Vector2.Zero) {
    data class SavedTile(
            var type: TileType? = null,
            var know: Boolean? = false)

    override operator fun equals(other: Any?): Boolean {
        when (other) {
            is GameSave -> {
                for (x in 0..mapWidth!!) {
                    for (y in 0..mapHeight!!) {
                        if (map?.get(x)?.get(y) != other.map?.get(x)?.get(y)) {
                            return false
                        }
                    }
                }

                return other.mapHeight == mapHeight
                        && other.mapWidth == mapWidth
                        && other.playerPosition == playerPosition
            }
            else -> return false
        }
    }

    override fun hashCode(): Int {

        return Objects.hash(mapHeight, mapWidth, map, playerPosition)
    }
}
