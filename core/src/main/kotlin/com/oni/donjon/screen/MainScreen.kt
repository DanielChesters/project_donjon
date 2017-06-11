package com.oni.donjon.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.oni.donjon.DonjonGame
import com.oni.donjon.Resources
import com.oni.donjon.actor.LoadWindow
import ktx.app.KtxScreen
import ktx.scene2d.KTextButton
import ktx.scene2d.defaultStyle
import ktx.scene2d.table

/**
 * @author Daniel Chesters (on 01/06/14).
 */
class MainScreen(val stage: Stage, val game: DonjonGame, val skin: Skin) : KtxScreen {
    val loadWindow = LoadWindow(Resources.BUNDLE.get("main.screen.load.title"), skin, game)
    val loadGameButton = createLoadGameButton()
    val view = table {
        defaults().space(5f)
        row()
        add(createNewGameButton()).center()
        row()
        add(loadGameButton).center()
        row()
        add(createExitGameButton()).center()
        pack()
        setPosition(Gdx.graphics.width / 2f - width / 2f,
                Gdx.graphics.height / 2f - height / 2f)
    }

    override fun show() {
        stage.addActor(view)
        stage.addActor(loadWindow.window)
        Gdx.input.inputProcessor = stage
    }

    private fun createExitGameButton(): KTextButton {
        val exitGameButton = KTextButton(Resources.BUNDLE.get("main.screen.exit.title"), skin, defaultStyle)
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

    private fun createLoadGameButton(): KTextButton {
        val loadGameButton = KTextButton(Resources.BUNDLE.get("main.screen.load_game.title"), skin, defaultStyle)
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

    private fun createNewGameButton(): KTextButton {
        val newGameButton = KTextButton(Resources.BUNDLE.get("main.screen.new_game.title"), skin, defaultStyle)
        newGameButton.pack()
        newGameButton.setPosition(Gdx.graphics.width / 2f - newGameButton.width / 2f,
                Gdx.graphics.height / 2f - newGameButton.height / 2f)
        newGameButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (game.containsScreen<GameScreen>()) {
                    game.removeScreen<GameScreen>()
                }
                game.addScreen(GameScreen(game, game.context.inject()))
                game.setScreen<GameScreen>()
                return true
            }
        })
        return newGameButton
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(delta)
        stage.draw()
    }

    override fun hide() {
        view.remove()
    }
}
