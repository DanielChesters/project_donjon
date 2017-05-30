package com.oni.donjon.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.utils.Array
import com.oni.donjon.DonjonGame
import com.oni.donjon.Resources
import com.oni.donjon.screen.GameScreen

/**
 * @author Daniel Chesters (on 06/06/14).
 */
class LoadWindow(title: String, skin: Skin, game: DonjonGame) {
    val window: Window = Window(title, skin)
    private var saveList: List<String>? = null
    private var loadButton: TextButton? = null
    private var cancelButton: TextButton? = null

    init {
        createSaveList(skin)
        createLoadButton(game, skin, saveList!!)
        createCancelButton(skin)
        createWindow(skin)
    }

    private fun createWindow(skin: Skin) {
        val saveScrollPane = ScrollPane(saveList, skin)
        saveScrollPane.setFlickScroll(false)
        window.defaults().spaceBottom(10f)
        window.row()
        window.add<ScrollPane>(saveScrollPane).fill().colspan(2).maxHeight(150f)
        window.row().fill().expandX()
        window.add<TextButton>(loadButton).colspan(1)
        window.add<TextButton>(cancelButton).colspan(1)
        window.isModal = true
        window.isVisible = false
    }

    private fun createCancelButton(skin: Skin) {
        cancelButton = TextButton(Resources.BUNDLE.get("main.screen.load.cancel"), skin)
        cancelButton!!.pack()
        cancelButton!!.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                                   button: Int): Boolean {
                this@LoadWindow.window.isVisible = false
                return true
            }
        })
    }

    private fun createLoadButton(game: DonjonGame, skin: Skin,
                                 saveList: List<String>) {
        loadButton = TextButton(Resources.BUNDLE.get("main.screen.load.ok"), skin)
        loadButton!!.pack()
        loadButton!!.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                                   button: Int): Boolean {
                val save = saveList.selected
                game.screen = GameScreen(game, String.format(".config/donjon/save/%s", save))
                return true
            }
        })
    }

    private fun createSaveList(skin: Skin) {
        saveList = List<String>(skin)
        saveList!!.selection.required = true
        saveList!!.selection.multiple = false
    }

    fun show() {
        val saveFolder = Gdx.files.external(".config/donjon/save")
        val saveArray = Array<String>()
        for (file in saveFolder.list { _, name -> name.endsWith(".json") }) {
            saveArray.add(file.name())
        }
        saveList!!.setItems(saveArray)
        window.pack()
        window.setPosition(Gdx.graphics.width / 2f - window.width / 2f,
                Gdx.graphics.height / 2f - window.height / 2f)
        window.isVisible = true
    }
}
