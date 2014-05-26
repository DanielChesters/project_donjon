package com.oni.donjon.action;

import com.oni.donjon.screen.GameScreen;

/**
 * @author Daniel Chesters (on 26/05/14).
 */
public enum Action {
    LOOK(GameScreen.bundle.get("action.look")),
    OPEN(GameScreen.bundle.get("action.open")),
    CLOSE(GameScreen.bundle.get("action.close"));

    private String label;

    Action(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
