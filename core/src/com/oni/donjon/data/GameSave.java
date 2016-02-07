package com.oni.donjon.data;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Daniel Chesters (on 07/02/16).
 */
public class GameSave {
    private Vector2 playerPosition;

    public GameSave() {
        this.playerPosition = Vector2.Zero;
    }

    public GameSave(Vector2 playerPosition) {
        this.playerPosition = playerPosition;
    }

    public Vector2 getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Vector2 playerPosition) {
        this.playerPosition = playerPosition;
    }
}
