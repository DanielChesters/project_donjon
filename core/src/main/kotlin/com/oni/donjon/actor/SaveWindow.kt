package com.oni.donjon.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.Json
import com.oni.donjon.DonjonGame
import com.oni.donjon.Resources
import com.oni.donjon.data.GameData
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem
import de.tomgrill.gdxdialogs.core.dialogs.GDXTextPrompt
import de.tomgrill.gdxdialogs.core.listener.TextPromptListener
import ktx.scene2d.scene2d
import ktx.scene2d.window

/**
 * @author Daniel Chesters (on 06/06/14).
 */
class SaveWindow(title: String, private val skin: Skin, game: DonjonGame) {
    lateinit var menuGameWindow: MenuGameWindow
    private val bundle: I18NBundle = game.context.inject()

    val saveList = createSaveList()
    private val saveButton = createSaveButton()
    private val cancelButton = createCancelButton()
    private val newSaveButton = createNewSaveButton()

    val window: Window = scene2d.window(title = title, skin = skin) {
        val saveScrollPane = ScrollPane(saveList, skin)
        saveScrollPane.setFlickScroll(false)
        defaults().spaceBottom(SAVE_WINDOW_SPACE_BOTTOM)
        row()
        add(saveScrollPane).fill().colspan(SAVE_WINDOW_COLSPAN).maxHeight(SAVE_WINDOW_MAX_HEIGHT)
        row().fill().expandX()
        add(saveButton).colspan(1)
        add(newSaveButton).colspan(1)
        add(cancelButton).colspan(1)
        isModal = true
        isVisible = false
    }

    private fun createSaveList(): List<String> {
        val saveList = List<String>(skin)
        saveList.selection.required = false
        saveList.selection.multiple = false
        return saveList
    }

    private fun createNewSaveButton(): TextButton {
        val newSaveButton = TextButton(bundle["window.save.new"], skin)
        newSaveButton.pack()
        newSaveButton.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                val dialogs = GDXDialogsSystem.install()

                val textPrompt = dialogs.newDialog(GDXTextPrompt::class.java)

                textPrompt.setTitle(bundle["window.save.new.input"])
                textPrompt.setMessage(bundle["window.save.new.input"])

                textPrompt.setCancelButtonLabel(bundle["window.save.cancel"])
                textPrompt.setConfirmButtonLabel(bundle["window.save.ok"])

                textPrompt.setTextPromptListener(object : TextPromptListener {

                    override fun confirm(text: String) {
                        val json = Json()
                        val file = Gdx.files.external(".config/donjon/save/$text.json")
                        val save = json.prettyPrint(GameData)
                        file.writeString(save, false)
                        this@SaveWindow.window.isVisible = false
                        menuGameWindow.setVisible(true)
                    }

                    override fun cancel() {
                        this@SaveWindow.window.isVisible = false
                        menuGameWindow.setVisible(true)
                    }
                })

                textPrompt.build().show()
                return true
            }
        })
        return newSaveButton
    }

    private fun createCancelButton(): TextButton {
        val cancelButton = TextButton(bundle["window.save.cancel"], skin)
        cancelButton.pack()
        cancelButton.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                this@SaveWindow.window.isVisible = false
                menuGameWindow.setVisible(true)
                return true
            }
        })

        return cancelButton
    }

    private fun createSaveButton(): TextButton {
        val saveButton = TextButton(bundle["window.save.ok"], skin)
        saveButton.pack()
        saveButton.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                val saveName = saveList.selected
                val json = Json()
                val file = Gdx.files.external(".config/donjon/save/$saveName")
                val save = json.prettyPrint(GameData.toGameSave())
                file.writeString(save, false)
                this@SaveWindow.window.isVisible = false
                menuGameWindow.setVisible(true)
                return true
            }
        })
        return saveButton
    }

    fun show() {
        val saveFolder = Gdx.files.external(".config/donjon/save")
        val saveArray = Array<String>()
        for (file in saveFolder.list { _, name -> name.endsWith(".json") }) {
            saveArray.add(file.name())
        }
        saveList.setItems(saveArray)
        window.pack()
        window.setPosition(
            Gdx.graphics.width / 2f - window.width / 2f,
            Gdx.graphics.height / 2f - window.height / 2f
        )
        window.isVisible = true
    }

    companion object {
        const val SAVE_WINDOW_SPACE_BOTTOM = 10f
        const val SAVE_WINDOW_COLSPAN = 3
        const val SAVE_WINDOW_MAX_HEIGHT = 150f
    }
}
