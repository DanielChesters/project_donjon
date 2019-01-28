@file:JvmName("DesktopLauncher")

package com.oni.donjon.desktop

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.oni.donjon.DonjonGame

fun main() {
    Lwjgl3Application(DonjonGame(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Project donjon")
        setWindowedMode(800, 600)
        setResizable(false)
    })
    Gdx.app.logLevel = Application.LOG_DEBUG
}
