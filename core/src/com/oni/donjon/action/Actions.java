package com.oni.donjon.action;

import com.oni.donjon.Resources;
import com.oni.donjon.map.Tile;
import com.oni.donjon.stage.UIStage;

/**
 * @author Daniel Chesters (on 26/05/14).
 */
public enum Actions {
    LOOK(Resources.BUNDLE.get("action.look"), new LookAction()),
    OPEN(Resources.BUNDLE.get("action.open"), new OpenAction()),
    CLOSE(Resources.BUNDLE.get("action.close"), new CloseAction());

    private String label;
    private Action action;

    Actions(String label, Action action) {
        this.label = label;
        this.action = action;
    }

    @Override
    public String toString() {
        return label;
    }

    public void doAction(Tile tile, UIStage stage) {
        action.doAction(tile, stage);
    }
}
