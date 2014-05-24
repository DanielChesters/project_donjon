package com.oni.donjon.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.oni.donjon.map.Map;
import com.oni.donjon.map.Tile;
import com.oni.donjon.map.TileType;

import java.util.stream.IntStream;

/**
 * @author Daniel Chesters (on 25/05/14).
 */
public class MapActor extends Actor {
    private Map map;

    public MapActor() {
        this.map = new Map();
        IntStream.rangeClosed(0, 20).forEach(x -> IntStream.rangeClosed(0, 20).forEach(y -> {
            if ((x > 1 && x < 19) && (y > 1 && y < 19)) {
                map.getTiles().add(new Tile(x, y, TileType.GROUND));
            } else {
                map.getTiles().add(new Tile(x, y, TileType.WALL));
            }
        }));
    }

    public Map getMap() {
        return map;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        map.getTiles().stream().forEach(t -> batch.draw(t.getType().getTexture(), t.getRectangle().getX() * Tile.SIZE, t.getRectangle().getY() * Tile.SIZE));
    }
}
