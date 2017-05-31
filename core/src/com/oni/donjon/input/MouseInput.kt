package com.oni.donjon.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.oni.donjon.data.GameData
import com.oni.donjon.map.Tile
import com.oni.donjon.stage.GameStage
import com.oni.donjon.stage.UIStage

/**
 * @author Daniel Chesters (on 24/05/14).
 */
class MouseInput : InputAdapter() {
    private var gameStage: GameStage? = null
    private var uiStage: UIStage? = null

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val mouseLocation = getMouseLocation(screenX, screenY, button)

        if (button == Input.Buttons.LEFT) {
            mainAction(mouseLocation)
        }

        return true
    }

    private fun getMouseLocation(screenX: Int, screenY: Int, button: Int): Vector2 {
        Gdx.app.debug("Mouse", String.format("Down : %d,%d : %d%n", screenX, screenY, button))
        val worldCoordinates = Vector3(screenX.toFloat(), screenY.toFloat(), 0f)
        gameStage!!.camera.unproject(worldCoordinates)
        val mouseLocation = Vector2(worldCoordinates.x, worldCoordinates.y)
        Gdx.app.debug("Mouse", String.format("%f,%f%n", mouseLocation.x, mouseLocation.y))
        Gdx.app.debug("Tile", String.format("%d,%d%n", (mouseLocation.x / Tile
                .SIZE).toInt(),
                (mouseLocation.y / Tile.SIZE).toInt()))
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

    fun setGameStage(gameStage: GameStage) {
        this.gameStage = gameStage
    }

    fun setUiStage(uiStage: UIStage) {
        this.uiStage = uiStage
    }
}
