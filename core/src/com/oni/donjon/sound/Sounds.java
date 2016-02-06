package com.oni.donjon.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * @author Daniel Chesters (on 06/02/16).
 */
public enum Sounds {

    OPEN_DOOR("doorOpen.ogg"),CLOSE_DOOR("doorClose.ogg");

    Sounds(String filename) {
        sound = Gdx.audio.newSound(Gdx.files.internal(String.format("sound/%s", filename)));
    }

    private Sound sound;

    public void play() {
        sound.play();
    }

    public void dispose() {
        sound.dispose();
    }

    public static void disposeAll() {
        for (Sounds sound : Sounds.values()) {
            sound.dispose();
        }
    }
}
