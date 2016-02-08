package com.oni.donjon.data;

import com.badlogic.gdx.math.Vector2;
import com.oni.donjon.map.TileType;

/**
 * @author Daniel Chesters (on 07/02/16).
 */
public class GameSave {
    private SavedTile[][] map;
    private Vector2 playerPosition;

    public static class SavedTile {
        public TileType type;
        public boolean visible;

        public SavedTile(TileType type, boolean visible) {
            this.type = type;
            this.visible = visible;
        }

        public SavedTile() {
        }
    }

    public GameSave() {
        this.playerPosition = Vector2.Zero;
    }

    public GameSave(SavedTile[][] map, Vector2 playerPosition) {
        this.map = map;
        this.playerPosition = playerPosition;
    }

    public Vector2 getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Vector2 playerPosition) {
        this.playerPosition = playerPosition;
    }

    public SavedTile[][] getMap() {
        return map;
    }

    public void setMap(SavedTile[][] map) {
        this.map = map;
    }
}
