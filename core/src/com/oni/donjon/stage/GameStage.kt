package com.oni.donjon.stage

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.oni.donjon.data.GameData
import com.oni.donjon.map.Tile

/**
 * @author Daniel Chesters (on 01/06/14).
 */
class GameStage : Stage() {
    private var playerLabel: Label? = null

    fun setPlayerLabel(playerLabel: Label) {
        this.playerLabel = playerLabel
    }

    fun updatePlayer() {
        val position = GameData.getPlayerPosition()
        playerLabel!!.setPosition(position.x * Tile.SIZE, position.y * Tile.SIZE)
    }
}
