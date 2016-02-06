package com.oni.donjon;

import com.badlogic.gdx.Game;
import com.oni.donjon.screen.MainScreen;
import com.oni.donjon.sound.Sounds;

public class DonjonGame extends Game {

    @Override
    public void create() {
        setScreen(new MainScreen(this));
    }

    @Override public void dispose() {
        super.dispose();
        Sounds.disposeAll();
    }
}
