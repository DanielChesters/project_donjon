package com.oni.donjon.action;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.oni.donjon.data.GameData;
import com.oni.donjon.map.Tile;

/**
 * @author Daniel Chesters (on 02/06/14).
 */
public abstract class AbstractAction implements Action {
    protected boolean isPlayerSamePositionAsTile(Tile tile) {
        return testPositionBetweenTileAndPlayer(tile, 1);
    }

    protected boolean isNearPlayer(Tile tile) {
        return testPositionBetweenTileAndPlayer(tile, 1.5f);
    }

    private boolean testPositionBetweenTileAndPlayer(Tile tile, float distance) {
        Rectangle tileRectangle = tile.getRectangle();
        Vector2 playerPosition = GameData.INSTANCE.getPlayerPosition();
        return Math.abs(tileRectangle.getX() - (int) playerPosition.x) < distance
            && Math.abs(tileRectangle.getY() - (int) playerPosition.y) < distance;
    }
}
