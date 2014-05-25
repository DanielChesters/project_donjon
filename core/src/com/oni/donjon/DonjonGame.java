package com.oni.donjon;

import com.badlogic.gdx.Game;
import com.oni.donjon.screen.GameScreen;

public class DonjonGame extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen());
    }

    @Override
    public void render() {
        super.render();
    }
}
