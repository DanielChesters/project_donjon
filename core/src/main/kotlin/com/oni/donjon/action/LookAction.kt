package com.oni.donjon.action

import com.badlogic.gdx.utils.I18NBundle
import com.oni.donjon.map.Tile
import com.oni.donjon.map.TileType
import com.oni.donjon.stage.UIStage

/**
 * @author Daniel Chesters (on 02/06/14).
 */
class LookAction : AbstractAction() {
    override fun doAction(tile: Tile, stage: UIStage, bundle: I18NBundle) {
        val messageLabel = stage.messageLabel
        if (isPlayerSamePositionAsTile(tile)) {
            messageLabel.setText(bundle["look.me"])
        } else {
            when (tile.type) {
                TileType.GROUND -> messageLabel.setText(bundle["look.ground"])
                TileType.WALL -> messageLabel.setText(bundle["look.wall"])
                TileType.DOOR_OPEN -> messageLabel.setText(bundle["look.door.open"])
                TileType.DOOR_CLOSE -> messageLabel.setText(bundle["look.door.close"])
                TileType.STAIR_UP -> messageLabel.setText(bundle["look.stair.up"])
                TileType.STAIR_DOWN -> messageLabel.setText(bundle["look.stair.down"])
            }
        }
    }
}
