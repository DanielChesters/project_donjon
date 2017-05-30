package com.oni.donjon.sound

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

/**
 * @author Daniel Chesters (on 06/02/16).
 */
enum class Sounds(filename: String) {

    OPEN_DOOR("doorOpen.ogg"), CLOSE_DOOR("doorClose.ogg");

    private val sound: Sound = Gdx.audio.newSound(Gdx.files.internal(String.format("sound/%s", filename)))

    fun play() {
        sound.play()
    }

    fun dispose() {
        sound.dispose()
    }

    companion object {

        fun disposeAll() {
            for (sound in Sounds.values()) {
                sound.dispose()
            }
        }
    }
}
