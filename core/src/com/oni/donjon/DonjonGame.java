package com.oni.donjon;

import com.badlogic.gdx.Game;
import com.oni.donjon.screen.MainScreen;

public class DonjonGame extends Game {
    @Override
    public void create() {
        setScreen(new MainScreen(this));
    }
}
