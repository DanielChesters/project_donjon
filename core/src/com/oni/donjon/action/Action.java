package com.oni.donjon.action;

import com.oni.donjon.Resources;

/**
 * @author Daniel Chesters (on 26/05/14).
 */
public enum Action {
    LOOK(Resources.bundle.get("action.look")),
    OPEN(Resources.bundle.get("action.open")),
    CLOSE(Resources.bundle.get("action.close"));

    private String label;

    Action(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
