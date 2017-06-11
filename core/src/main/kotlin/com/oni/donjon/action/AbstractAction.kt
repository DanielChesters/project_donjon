package com.oni.donjon.action

import com.oni.donjon.data.GameData
import com.oni.donjon.map.Tile

/**
 * @author Daniel Chesters (on 02/06/14).
 */
abstract class AbstractAction : Action {
    protected fun isPlayerSamePositionAsTile(tile: Tile): Boolean {
        return testPositionBetweenTileAndPlayer(tile, 1f)
    }

    protected fun isNearPlayer(tile: Tile): Boolean {
        return testPositionBetweenTileAndPlayer(tile, 1.5f)
    }

    private fun testPositionBetweenTileAndPlayer(tile: Tile, distance: Float): Boolean {
        val tileRectangle = tile.rectangle
        val playerPosition = GameData.getPlayerPosition()
        return Math.abs(tileRectangle.getX() - playerPosition.x.toInt()) < distance && Math.abs(tileRectangle.getY() - playerPosition.y.toInt()) < distance
    }
}
