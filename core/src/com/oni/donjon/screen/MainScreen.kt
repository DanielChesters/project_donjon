package com.oni.donjon.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.oni.donjon.DonjonGame
import com.oni.donjon.Resources
import com.oni.donjon.actor.LoadWindow

/**
 * @author Daniel Chesters (on 01/06/14).
 */
class MainScreen(game: DonjonGame) : ScreenAdapter() {
    private val stage: Stage = Stage()


    init {
        val skin = Skin(Gdx.files.internal("skin/uiskin.json"))

        val mainTable = Table(skin)

        val loadWindow = LoadWindow(Resources.BUNDLE.get("main.screen.load.title"), skin, game)

        val newGameButton = createNewGameButton(game, skin)

        val loadGameButton = createLoadGameButton(skin, loadWindow)

        val exitGameButton = createExitGameButton(skin, loadGameButton)

        mainTable.defaults().space(5f)
        mainTable.row()
        mainTable.add(newGameButton).center()
        mainTable.row()
        mainTable.add(loadGameButton).center()
        mainTable.row()
        mainTable.add(exitGameButton).center()
        mainTable.pack()
        mainTable.setPosition(Gdx.graphics.width / 2f - mainTable.width / 2f,
                Gdx.graphics.height / 2f - mainTable.height / 2f)

        stage.addActor(mainTable)
        stage.addActor(loadWindow.window)
        Gdx.input.inputProcessor = stage
    }

    private fun createExitGameButton(skin: Skin, loadGameButton: TextButton): TextButton {
        val exitGameButton = TextButton(Resources.BUNDLE.get("main.screen.exit.title"), skin)
        exitGameButton.pack()
        exitGameButton.setPosition(Gdx.graphics.width / 2f - exitGameButton.width / 2f,
                Gdx.graphics.height / 2f - (exitGameButton.height + loadGameButton.height
                        + 30f))
        exitGameButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                                   button: Int): Boolean {
                Gdx.app.exit()
                return true
            }
        })
        return exitGameButton
    }

    private fun createLoadGameButton(skin: Skin, loadWindow: LoadWindow): TextButton {
        val loadGameButton = TextButton(Resources.BUNDLE.get("main.screen.load_game.title"), skin)
        loadGameButton.pack()
        loadGameButton.setPosition(Gdx.graphics.width / 2f - loadGameButton.width / 2f,
                Gdx.graphics.height / 2f - (loadGameButton.height + 20))
        loadGameButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                loadWindow.show()
                return true
            }
        })
        return loadGameButton
    }

    private fun createNewGameButton(game: DonjonGame, skin: Skin): TextButton {
        val newGameButton = TextButton(Resources.BUNDLE.get("main.screen.new_game.title"), skin)
        newGameButton.pack()
        newGameButton.setPosition(Gdx.graphics.width / 2f - newGameButton.width / 2f,
                Gdx.graphics.height / 2f - newGameButton.height / 2f)
        newGameButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                game.screen = GameScreen(game)
                return true
            }
        })
        return newGameButton
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.draw()
    }
}
