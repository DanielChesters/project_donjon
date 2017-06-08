package com.oni.donjon.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.oni.donjon.DonjonGame
import com.oni.donjon.Resources
import com.oni.donjon.screen.GameScreen
import com.oni.donjon.screen.MainScreen

/**
 * @author Daniel Chesters (on 08/06/14).
 */
class MenuGameWindow(skin: Skin, saveWindow: SaveWindow, game: DonjonGame, screen: GameScreen) {
    val window: Window = Window(Resources.BUNDLE.get("game_menu.title"), skin, "dialog")
    private var saveButton: TextButton? = null
    private var exitButton: TextButton? = null
    private var closeButton: TextButton? = null

    init {
        createSaveButton(skin, saveWindow)
        createExitButton(skin, game)
        createCloseButton(skin, screen)
        createWindow()
    }

    private fun createWindow() {
        window.row()
        window.add<TextButton>(saveButton).center()
        window.row()
        window.add<TextButton>(exitButton).center()
        window.row()
        window.add<TextButton>(closeButton).center()
        window.pack()
        window.setPosition(Gdx.graphics.width / 2f - window.width / 2f,
                Gdx.graphics.height / 2f - window.height / 2f)
        window.isModal = true
        window.isMovable = false
        window.isVisible = false
    }

    private fun createCloseButton(skin: Skin, screen: GameScreen) {
        closeButton = TextButton(Resources.BUNDLE.get("game_menu.action.close"), skin)
        closeButton!!.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                                   button: Int): Boolean {
                window.isVisible = false
                screen.setState(GameScreen.GameState.RUNNING)
                return true
            }
        })
        closeButton!!.pack()
    }

    private fun createExitButton(skin: Skin, game: DonjonGame) {
        exitButton = TextButton(Resources.BUNDLE.get("game_menu.action.exit"), skin)
        exitButton!!.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                                   button: Int): Boolean {
                game.removeScreen<MainScreen>()
                game.addScreen(MainScreen(game.context.inject(), game, game.context.inject()))
                game.setScreen<MainScreen>()
                return true
            }
        })
        exitButton!!.pack()
    }

    private fun createSaveButton(skin: Skin, saveWindow: SaveWindow) {
        saveButton = TextButton(Resources.BUNDLE.get("game_menu.action.save"), skin)
        saveButton!!.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                                   button: Int): Boolean {
                saveWindow.show()
                window.isVisible = false
                return true
            }
        })
        saveButton!!.pack()
        saveWindow.menuGameWindow = this
    }

    fun setVisible(visible: Boolean) {
        window.isVisible = visible
    }
}
