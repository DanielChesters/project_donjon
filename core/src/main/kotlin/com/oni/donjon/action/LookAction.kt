package com.oni.donjon.action

import com.oni.donjon.Resources
import com.oni.donjon.map.Tile
import com.oni.donjon.map.TileType
import com.oni.donjon.stage.UIStage

/**
 * @author Daniel Chesters (on 02/06/14).
 */
class LookAction : AbstractAction() {
    override fun doAction(tile: Tile, stage: UIStage) {
        val messageLabel = stage.messageLabel
        if (isPlayerSamePositionAsTile(tile)) {
            messageLabel.setText(Resources.BUNDLE["look.me"])
        } else {
            when (tile.type) {
                TileType.GROUND -> messageLabel.setText(Resources.BUNDLE["look.ground"])
                TileType.WALL -> messageLabel.setText(Resources.BUNDLE["look.wall"])
                TileType.DOOR_OPEN -> messageLabel.setText(Resources.BUNDLE["look.door.open"])
                TileType.DOOR_CLOSE -> messageLabel.setText(Resources.BUNDLE["look.door.close"])
                TileType.STAIR_UP -> messageLabel.setText(Resources.BUNDLE["look.stair.up"])
                TileType.STAIR_DOWN -> messageLabel.setText(Resources.BUNDLE["look.stair.down"])
                else -> {
                }
            }
        }
    }
}
