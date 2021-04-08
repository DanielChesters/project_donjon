package com.oni.donjon.action

import com.badlogic.gdx.utils.I18NBundle
import com.oni.donjon.DonjonGame
import com.oni.donjon.map.Tile
import com.oni.donjon.map.TileType
import com.oni.donjon.sound.Sounds
import com.oni.donjon.stage.UIStage

/**
 * @author Daniel Chesters (on 02/06/14).
 */
class OpenAction : AbstractAction() {
    override fun doAction(tile: Tile, stage: UIStage, game: DonjonGame) {
        val bundle: I18NBundle = game.context.inject()
        val messageLabel = stage.messageLabel
        if (isPlayerSamePositionAsTile(tile)) {
            messageLabel.setText(bundle["open.me"])
        } else {
            when (tile.type) {
                TileType.DOOR_OPEN -> messageLabel.setText(bundle["open.door.already.open"])
                TileType.DOOR_CLOSE -> openClosedDoor(tile, stage, bundle)
                else -> messageLabel.setText(bundle["open.nothing"])
            }
        }
    }

    private fun openClosedDoor(tile: Tile, stage: UIStage, bundle: I18NBundle) {
        val messageLabel = stage.messageLabel
        if (isNearPlayer(tile)) {
            val bodyDoor = tile.body
            bodyDoor.world.destroyBody(bodyDoor)
            tile.type = TileType.DOOR_OPEN
            messageLabel.setText(bundle["open.door"])
            Sounds.OPEN_DOOR.play()
        } else {
            messageLabel.setText(bundle["open.door.too.far"])
        }
    }
}
