package com.oni.donjon.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.I18NBundle
import com.oni.donjon.DonjonGame
import com.oni.donjon.Resources
import com.oni.donjon.screen.GameScreen
import com.oni.donjon.screen.MainScreen
import ktx.scene2d.scene2d
import ktx.scene2d.window

/**
 * @author Daniel Chesters (on 08/06/14).
 */
class MenuGameWindow(val saveWindow: SaveWindow, val game: DonjonGame, val screen: GameScreen) {
    private val skin: Skin = game.context.inject()
    val bundle: I18NBundle = game.context.inject()

    private val saveButton = createSaveButton()
    private val exitButton = createExitButton()
    private val closeButton = createCloseButton()

    val window = scene2d.window(bundle["game_menu.title"], "dialog", skin) {
        row()
        add(saveButton).center()
        row()
        add(exitButton).center()
        row()
        add(closeButton).center()
        pack()
        setPosition(
            Gdx.graphics.width / 2f - width / 2f,
            Gdx.graphics.height / 2f - height / 2f
        )
        isModal = true
        isMovable = false
        isVisible = false
    }

    private fun createCloseButton(): TextButton {
        val closeButton = TextButton(bundle["game_menu.action.close"], skin)
        closeButton.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                window.isVisible = false
                screen.state = GameScreen.GameState.RUNNING
                return true
            }
        })
        closeButton.pack()
        return closeButton
    }

    private fun createExitButton(): TextButton {
        val exitButton = TextButton(bundle["game_menu.action.exit"], skin)
        exitButton.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                game.removeScreen<MainScreen>()
                game.addScreen(MainScreen(game))
                game.setScreen<MainScreen>()
                return true
            }
        })
        exitButton.pack()
        return exitButton
    }

    private fun createSaveButton(): TextButton {
        val saveButton = TextButton(bundle["game_menu.action.save"], skin)
        saveButton.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                saveWindow.show()
                window.isVisible = false
                return true
            }
        })
        saveButton.pack()
        saveWindow.menuGameWindow = this
        return saveButton
    }

    fun setVisible(visible: Boolean) {
        window.isVisible = visible
    }
}
