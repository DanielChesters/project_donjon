package com.oni.donjon

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.oni.donjon.screen.MainScreen
import ktx.app.KtxGame
import ktx.inject.Context
import ktx.scene2d.Scene2DSkin

class DonjonGame : KtxGame<Screen>() {
    val context = Context()
    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        context.register {
            bindSingleton(Stage())
            bindSingleton(Skin(Gdx.files.internal("skin/uiskin.json")))
            Scene2DSkin.defaultSkin = inject()
            bindSingleton(this@DonjonGame)
        }

        addScreen(MainScreen(context.inject(), context.inject(), context.inject()))
        setScreen<MainScreen>()
    }

    override fun dispose() {
        context.dispose()
    }
}
