@file:JvmName("DesktopLauncher")

package com.oni.donjon.desktop

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.oni.donjon.DonjonGame

fun main() {
    Lwjgl3Application(
        DonjonGame(),
        Lwjgl3ApplicationConfiguration().apply {
            setTitle("Project donjon")
            setWindowedMode(Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT)
            setResizable(false)
        }
    )
    Gdx.app.logLevel = Application.LOG_DEBUG
}

object Constants {
    const val DEFAULT_WIDTH = 800
    const val DEFAULT_HEIGHT = 600
}
