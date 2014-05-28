package com.oni.donjon.action;

import com.oni.donjon.Resources;

/**
 * @author Daniel Chesters (on 26/05/14).
 */
public enum Action {
    LOOK(Resources.BUNDLE.get("action.look")),
    OPEN(Resources.BUNDLE.get("action.open")),
    CLOSE(Resources.BUNDLE.get("action.close"));

    private String label;

    Action(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
