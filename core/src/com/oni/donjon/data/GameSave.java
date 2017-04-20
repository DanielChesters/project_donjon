package com.oni.donjon.data;

import com.badlogic.gdx.math.Vector2;
import com.oni.donjon.map.TileType;
import lombok.Data;

/**
 * @author Daniel Chesters (on 07/02/16).
 */
public class GameSave {
    private int mapHeight;
    private int mapWidth;
    private SavedTile[][] map;
    private Vector2 playerPosition;


    public GameSave() {
        this.playerPosition = Vector2.Zero;
    }

    public GameSave(int mapHeight, int mapWidth, SavedTile[][] map,
        Vector2 playerPosition) {
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
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

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }


    @Data
    public static class SavedTile {
        private TileType type;
        private boolean know;

        public SavedTile(TileType type, boolean know) {
            this.type = type;
            this.know = know;
        }

        public SavedTile() {
            //Empty for JSON processor
        }
    }
}
