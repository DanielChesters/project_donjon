package com.oni.donjon.data;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.oni.donjon.component.DirectionComponent;
import com.oni.donjon.component.PositionComponent;
import com.oni.donjon.map.Map;

/**
 * @author Daniel Chesters (on 01/06/14).
 */
public class GameData {
    private Map map;
    private Entity player;

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

}
