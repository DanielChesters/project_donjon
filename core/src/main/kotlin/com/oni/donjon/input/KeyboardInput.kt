package com.oni.donjon.input

import com.badlogic.gdx.Input
import com.oni.donjon.component.DirectionComponent
import com.oni.donjon.data.GameData
import ktx.app.KtxInputAdapter
import ktx.log.logger

/**
 * @author Daniel Chesters (on 20/05/14).
 */
class KeyboardInput : KtxInputAdapter {
    companion object {
        val log = logger<KeyboardInput>()
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.D, Input.Keys.RIGHT -> GameData.setPlayerDirection(DirectionComponent.Direction.RIGHT)
            Input.Keys.Q, Input.Keys.LEFT -> GameData.setPlayerDirection(DirectionComponent.Direction.LEFT)
            Input.Keys.Z, Input.Keys.UP -> GameData.setPlayerDirection(DirectionComponent.Direction.UP)
            Input.Keys.S, Input.Keys.DOWN -> GameData.setPlayerDirection(DirectionComponent.Direction.DOWN)
        }
        debugMessage(keycode)
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.D, Input.Keys.RIGHT, Input.Keys.Q, Input.Keys.LEFT,
            Input.Keys.Z, Input.Keys.UP, Input.Keys.S, Input.Keys.DOWN ->
                GameData.setPlayerDirection(DirectionComponent.Direction.NONE)
        }

        return true
    }

    private fun debugMessage(keycode: Int) {
        log.debug { Input.Keys.toString(keycode) }
    }
}
