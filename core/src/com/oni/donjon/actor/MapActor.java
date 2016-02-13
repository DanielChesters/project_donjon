package com.oni.donjon.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.oni.donjon.data.GameData;
import com.oni.donjon.map.Tile;

/**
 * @author Daniel Chesters (on 25/05/14).
 */
public class MapActor extends Actor {
    @Override
    public void draw(Batch batch, float alpha) {
        GameData.INSTANCE.getMap().getTiles().stream().filter(Tile::isKnow).forEach(t -> batch
            .draw(t.getType().getTexture(), t.getRectangle().getX() * Tile.SIZE,
                t.getRectangle().getY() * Tile.SIZE));
    }
}
