package com.oni.donjon

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.oni.donjon.screen.MainScreen

class DonjonGame : Game() {
    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        setScreen(MainScreen(this))
    }
}
