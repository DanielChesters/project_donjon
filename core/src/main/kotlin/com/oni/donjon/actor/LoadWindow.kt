package com.oni.donjon.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.I18NBundle
import com.oni.donjon.DonjonGame
import com.oni.donjon.screen.GameScreen
import ktx.collections.gdxArrayOf
import ktx.scene2d.window

/**
 * @author Daniel Chesters (on 06/06/14).
 */
class LoadWindow(title: String, val game: DonjonGame) {
    val skin: Skin = game.context.inject()
    val bundle: I18NBundle = game.context.inject()

    val saveList = createSaveList()
    val loadButton = createLoadButton()
    val cancelButton = createCancelButton()

    val window = window(title = title, skin = skin) {
        val saveScrollPane = ScrollPane(saveList, skin)
        saveScrollPane.setFlickScroll(false)
        defaults().spaceBottom(10f)
        row()
        add(saveScrollPane).fill().colspan(2).maxHeight(150f)
        row().fill().expandX()
        add(loadButton).colspan(1)
        add(cancelButton).colspan(1)
        isModal = true
        isVisible = false
    }

    private fun createCancelButton(): TextButton {
        val cancelButton = TextButton(bundle["main.screen.load.cancel"], skin)
        cancelButton.pack()
        cancelButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                                   button: Int): Boolean {
                this@LoadWindow.window.isVisible = false
                return true
            }
        })
        return cancelButton
    }

    private fun createLoadButton(): TextButton {
        val loadButton = TextButton(bundle["main.screen.load.ok"], skin)
        loadButton.pack()
        loadButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                                   button: Int): Boolean {
                val save = saveList.selected
                if (game.containsScreen<GameScreen>()) {
                    game.removeScreen<GameScreen>()
                }
                game.addScreen(GameScreen(game, ".config/donjon/save/$save"))
                game.setScreen<GameScreen>()
                return true
            }
        })

        return loadButton
    }

    private fun createSaveList(): List<String> {
        val saveList = List<String>(skin)
        saveList.selection.required = true
        saveList.selection.multiple = false
        return saveList
    }

    fun show() {
        val saveFolder = Gdx.files.external(".config/donjon/save")
        val saveArray = gdxArrayOf<String>()
        for (file in saveFolder.list { _, name -> name.endsWith(".json") }) {
            saveArray.add(file.name())
        }
        saveList.setItems(saveArray)
        window.pack()
        window.setPosition(Gdx.graphics.width / 2f - window.width / 2f,
                Gdx.graphics.height / 2f - window.height / 2f)
        window.isVisible = true
    }
}
