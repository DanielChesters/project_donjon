package com.oni.donjon.desktop

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.oni.donjon.DonjonGame

object DesktopLauncher {
    @JvmStatic fun main(arg: Array<String>) {
        val config = Lwjgl3ApplicationConfiguration()
        config.setWindowedMode(800, 600)
        Lwjgl3Application(DonjonGame(), config)
        Gdx.app.logLevel = Application.LOG_DEBUG
    }
}
