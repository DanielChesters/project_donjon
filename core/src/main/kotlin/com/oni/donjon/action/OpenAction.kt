package com.oni.donjon.action

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.oni.donjon.Resources
import com.oni.donjon.map.Tile
import com.oni.donjon.map.TileType
import com.oni.donjon.sound.Sounds
import com.oni.donjon.stage.UIStage

/**
 * @author Daniel Chesters (on 02/06/14).
 */
class OpenAction : AbstractAction() {
    override fun doAction(tile: Tile, stage: UIStage) {
        val messageLabel = stage.messageLabel
        if (isPlayerSamePositionAsTile(tile)) {
            messageLabel.setText(Resources.BUNDLE.get("open.me"))
        } else {
            when (tile.type) {
                TileType.DOOR_OPEN -> messageLabel.setText(Resources.BUNDLE.get("open.door.already.open"))
                TileType.DOOR_CLOSE -> openClosedDoor(tile, stage)
                else -> messageLabel.setText(Resources.BUNDLE.get("open.nothing"))
            }
        }
    }

    private fun openClosedDoor(tile: Tile, stage: UIStage) {
        val messageLabel = stage.messageLabel
        if (isNearPlayer(tile)) {
            val bodyDoor = tile.body
            bodyDoor.world.destroyBody(bodyDoor)
            tile.type = TileType.DOOR_OPEN
            messageLabel.setText(Resources.BUNDLE.get("open.door"))
            Sounds.OPEN_DOOR.play()
        } else {
            messageLabel.setText(Resources.BUNDLE.get("open.door.too.far"))
        }
    }
}
