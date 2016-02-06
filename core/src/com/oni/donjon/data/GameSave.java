package com.oni.donjon.data;

import com.badlogic.gdx.math.Vector2;
import com.oni.donjon.map.Map;

/**
 * @author Daniel Chesters (on 07/02/16).
 */
public class GameSave {
    private Map map;
    private Vector2 playerPosition;

    public GameSave() {
        this.map = new Map();
        this.playerPosition = Vector2.Zero;
    }

    public GameSave(Map map, Vector2 playerPosition) {
        this.map = map;
        this.playerPosition = playerPosition;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Vector2 getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Vector2 playerPosition) {
        this.playerPosition = playerPosition;
    }
}
