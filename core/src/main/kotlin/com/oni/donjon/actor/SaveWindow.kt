package com.oni.donjon.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Json
import com.oni.donjon.Resources
import com.oni.donjon.data.GameData
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem
import de.tomgrill.gdxdialogs.core.dialogs.GDXTextPrompt
import de.tomgrill.gdxdialogs.core.listener.TextPromptListener

/**
 * @author Daniel Chesters (on 06/06/14).
 */
class SaveWindow(title: String, skin: Skin) {
    val window: Window = Window(title, skin)

    var menuGameWindow: MenuGameWindow? = null

    private var saveList: List<String>? = null
    private var saveButton: TextButton? = null
    private var cancelButton: TextButton? = null
    private var newSaveButton: TextButton? = null

    init {
        createSaveList(skin)
        createSaveButton(skin)
        createCancelButton(skin)
        createNewSaveButton(skin)
        createWindow(skin)
    }

    private fun createWindow(skin: Skin) {
        val saveScrollPane = ScrollPane(saveList, skin)
        saveScrollPane.setFlickScroll(false)
        window.defaults().spaceBottom(10f)
        window.row()
        window.add<ScrollPane>(saveScrollPane).fill().colspan(3).maxHeight(150f)
        window.row().fill().expandX()
        window.add<TextButton>(saveButton).colspan(1)
        window.add<TextButton>(newSaveButton).colspan(1)
        window.add<TextButton>(cancelButton).colspan(1)
        window.isModal = true
        window.isVisible = false
    }

    private fun createSaveList(skin: Skin) {
        saveList = List<String>(skin)
        saveList!!.selection.required = false
        saveList!!.selection.multiple = false
    }

    private fun createNewSaveButton(skin: Skin) {
        newSaveButton = TextButton(Resources.BUNDLE.get("window.save.new"), skin)
        newSaveButton!!.pack()
        newSaveButton!!.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                                   button: Int): Boolean {
                val dialogs = GDXDialogsSystem.install()

                val textPrompt = dialogs.newDialog(GDXTextPrompt::class.java)

                textPrompt.setTitle(Resources.BUNDLE.get("window.save.new.input"))
                textPrompt.setMessage(Resources.BUNDLE.get("window.save.new.input"))

                textPrompt.setCancelButtonLabel(Resources.BUNDLE.get("window.save.cancel"))
                textPrompt.setConfirmButtonLabel(Resources.BUNDLE.get("window.save.ok"))

                textPrompt.setTextPromptListener(object : TextPromptListener {

                    override fun confirm(text: String) {
                        val json = Json()
                        val file = Gdx.files.external(".config/donjon/save/$text.json")
                        val save = json.prettyPrint(GameData)
                        file.writeString(save, false)
                        this@SaveWindow.window.isVisible = false
                        menuGameWindow!!.setVisible(true)
                    }

                    override fun cancel() {
                        this@SaveWindow.window.isVisible = false
                        menuGameWindow!!.setVisible(true)
                    }
                })

                textPrompt.build().show()
                return true
            }
        })
    }

    private fun createCancelButton(skin: Skin) {
        cancelButton = TextButton(Resources.BUNDLE.get("window.save.cancel"), skin)
        cancelButton!!.pack()
        cancelButton!!.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                                   button: Int): Boolean {
                this@SaveWindow.window.isVisible = false
                menuGameWindow!!.setVisible(true)
                return true
            }
        })
    }

    private fun createSaveButton(skin: Skin) {
        saveButton = TextButton(Resources.BUNDLE.get("window.save.ok"), skin)
        saveButton!!.pack()
        saveButton!!.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                                   button: Int): Boolean {
                val saveName = saveList!!.selected
                val json = Json()
                val file = Gdx.files.external(".config/donjon/save/$saveName")
                val save = json.prettyPrint(GameData.toGameSave())
                file.writeString(save, false)
                this@SaveWindow.window.isVisible = false
                menuGameWindow!!.setVisible(true)
                return true
            }
        })
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
