package com.oni.donjon.action

import com.oni.donjon.data.GameData
import com.oni.donjon.map.Tile
import kotlin.math.abs

/**
 * @author Daniel Chesters (on 02/06/14).
 */
abstract class AbstractAction : Action {
    protected fun isPlayerSamePositionAsTile(tile: Tile): Boolean {
        return testPositionBetweenTileAndPlayer(tile, 1f)
    }

    protected fun isNearPlayer(tile: Tile): Boolean {
        return testPositionBetweenTileAndPlayer(tile, distanceNearPlayer)
    }

    private fun testPositionBetweenTileAndPlayer(tile: Tile, distance: Float): Boolean {
        val tileRectangle = tile.rectangle
        val playerPosition = GameData.getPlayerPosition()
        return abs(tileRectangle.getX() - playerPosition.x.toInt()) < distance &&
            abs(tileRectangle.getY() - playerPosition.y.toInt()) < distance
    }

    companion object {
        const val distanceNearPlayer = 1.5f
    }
}
