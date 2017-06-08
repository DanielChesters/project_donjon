package com.oni.donjon

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.oni.donjon.screen.MainScreen
import ktx.app.KtxGame

class DonjonGame : KtxGame<Screen>() {
    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(MainScreen(this))
        setScreen<MainScreen>()
    }
}
