package com.oni.donjon.action;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.oni.donjon.data.GameData;
import com.oni.donjon.map.Tile;

/**
 * @author Daniel Chesters (on 02/06/14).
 */
public abstract class AbstractAction implements Action {
    protected boolean isPlayerSamePositionAsTile(Tile tile, GameData data) {
        return testPositionBetweenTileAndPlayer(tile, 1, data);
    }

    protected boolean isNearPlayer(Tile tile, GameData data) {
        return testPositionBetweenTileAndPlayer(tile, 1.5f, data);
    }

    private boolean testPositionBetweenTileAndPlayer(Tile tile, float distance, GameData data) {
        Rectangle tileRectangle = tile.getRectangle();
        Vector2 playerPosition = data.getPlayer().getPosition();
        return Math.abs(tileRectangle.getX() - (int) playerPosition.x) < distance
            && Math.abs(tileRectangle.getY() - (int) playerPosition.y) < distance;
    }
}
