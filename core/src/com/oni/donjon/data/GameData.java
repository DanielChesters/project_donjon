package com.oni.donjon.data;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.oni.donjon.component.DirectionComponent;
import com.oni.donjon.component.PositionComponent;
import com.oni.donjon.map.Map;
import com.oni.donjon.map.Tile;
import com.oni.donjon.map.TileType;

/**
 * @author Daniel Chesters (on 01/06/14).
 */
public enum GameData {
    INSTANCE;

    private Map map;
    private Entity player;
    private World world;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Entity getPlayer() {
        return player;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    public Vector2 getPlayerPosition() {
        return ComponentMapper.getFor(PositionComponent.class).get(player).position;
    }

    public void setPlayerDirection(DirectionComponent.Direction direction) {
        ComponentMapper.getFor(DirectionComponent.class).get(player).direction = direction;
    }

    public GameSave toGameSave() {
        GameSave.SavedTile[][] savedTiles = new GameSave.SavedTile[21][21];

        for (Tile tile: map.getTiles()) {
            GameSave.SavedTile savedTile = new GameSave.SavedTile(tile.getType(), tile.isVisible());
            savedTiles[(int)tile.getRectangle().x][(int)tile.getRectangle().y] = savedTile;
        }

        return new GameSave(savedTiles, getPlayerPosition());
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
