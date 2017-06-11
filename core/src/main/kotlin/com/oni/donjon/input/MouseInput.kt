package com.oni.donjon.input

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.oni.donjon.data.GameData
import com.oni.donjon.map.Tile
import com.oni.donjon.stage.GameStage
import com.oni.donjon.stage.UIStage
import ktx.app.KtxInputAdapter
import ktx.log.logger
import ktx.math.vec2
import ktx.math.vec3

/**
 * @author Daniel Chesters (on 24/05/14).
 */
class MouseInput(val gameStage: GameStage, val uiStage: UIStage) : KtxInputAdapter {
    companion object {
        val log = logger<MouseInput>()
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val mouseLocation = getMouseLocation(screenX, screenY, button)

        if (button == Input.Buttons.LEFT) {
            mainAction(mouseLocation)
        }

        return true
    }

    private fun getMouseLocation(screenX: Int, screenY: Int, button: Int): Vector2 {
        log.debug { "Down : $screenX,$screenY : $button" }
        val worldCoordinates = vec3(screenX.toFloat(), screenY.toFloat(), 0f)
        gameStage.camera.unproject(worldCoordinates)
        val mouseLocation = vec2(worldCoordinates.x, worldCoordinates.y)
        log.debug { "${mouseLocation.x},${mouseLocation.y}" }
        log.debug {
            "${(mouseLocation.x / Tile.SIZE).toInt()},${(mouseLocation.y / Tile.SIZE).toInt()}"
        }
        return mouseLocation
    }

    private fun mainAction(mouseLocation: Vector2) {
        val tile = GameData.map.getTile((mouseLocation.x / Tile.SIZE).toInt().toFloat(),
                (mouseLocation.y / Tile.SIZE).toInt().toFloat())
        if (tile.isPresent && tile.get().isKnow) {
            val realTile = tile.get()
            val action = uiStage!!.actionList!!.selected
            action?.doAction(realTile, uiStage!!)
        }
    }
}
