package com.oni.donjon.action;

/**
 * @author Daniel Chesters (on 26/05/14).
 */
public enum Action {
    LOOK("Look"),
    OPEN("Open"),
    CLOSE("Close");

    private String label;

    Action(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
