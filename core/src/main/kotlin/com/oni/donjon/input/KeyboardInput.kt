package com.oni.donjon.input

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.oni.donjon.component.DirectionComponent
import com.oni.donjon.data.GameData

/**
 * @author Daniel Chesters (on 20/05/14).
 */
class KeyboardInput : InputAdapter() {
    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.D, Input.Keys.RIGHT -> GameData.setPlayerDirection(DirectionComponent.Direction.RIGHT)
            Input.Keys.Q, Input.Keys.LEFT -> GameData.setPlayerDirection(DirectionComponent.Direction.LEFT)
            Input.Keys.Z, Input.Keys.UP -> GameData.setPlayerDirection(DirectionComponent.Direction.UP)
            Input.Keys.S, Input.Keys.DOWN -> GameData.setPlayerDirection(DirectionComponent.Direction.DOWN)
            else -> {
            }
        }
        if (Gdx.app.logLevel == Application.LOG_DEBUG) {
            debugMessage(keycode)
        }
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.D, Input.Keys.RIGHT, Input.Keys.Q, Input.Keys.LEFT, Input.Keys.Z, Input.Keys.UP, Input.Keys.S, Input.Keys.DOWN -> GameData.setPlayerDirection(DirectionComponent.Direction.NONE)
            else -> {
            }
        }

        return true
    }

    private fun debugMessage(keycode: Int) {
        Gdx.app.debug("Keyboard", Input.Keys.toString(keycode))
    }
}
