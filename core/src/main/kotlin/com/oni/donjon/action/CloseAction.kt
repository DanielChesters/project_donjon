package com.oni.donjon.action

import com.badlogic.gdx.utils.I18NBundle
import com.oni.donjon.data.GameData
import com.oni.donjon.map.Tile
import com.oni.donjon.map.TileType
import com.oni.donjon.sound.Sounds
import com.oni.donjon.stage.UIStage

/**
 * @author Daniel Chesters (on 02/06/14).
 */
class CloseAction : AbstractAction() {
    override fun doAction(tile: Tile, stage: UIStage, bundle: I18NBundle) {
        val messageLabel = stage.messageLabel
        if (isPlayerSamePositionAsTile(tile)) {
            messageLabel.setText(bundle["close.me"])
        } else {
            when (tile.type) {
                TileType.DOOR_CLOSE -> messageLabel.setText(bundle["close.door.already.close"])
                TileType.DOOR_OPEN -> closeOpenedDoor(tile, stage, bundle)
                else -> messageLabel.setText(bundle["close.nothing"])
            }
        }
    }

    private fun closeOpenedDoor(tile: Tile, stage: UIStage, bundle: I18NBundle) {
        val messageLabel = stage.messageLabel
        if (isNearPlayer(tile)) {
            tile.type = TileType.DOOR_CLOSE
            tile.body = tile.createBody(GameData.world)
            messageLabel.setText(bundle["close.door"])
            Sounds.CLOSE_DOOR.play()
        } else {
            messageLabel.setText(bundle["close.door.too.far"])
        }
    }
}
