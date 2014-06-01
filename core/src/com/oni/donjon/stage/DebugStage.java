package com.oni.donjon.stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.oni.donjon.data.GameData;
import com.oni.donjon.map.Tile;

/**
 * @author Daniel Chesters (on 01/06/14).
 */
public class DebugStage {
    private ShapeRenderer debugRenderer;
    private GameStage gameStage;
    private GameData data;

    public DebugStage() {
        debugRenderer = new ShapeRenderer();
    }

    public void setGameStage(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    public void setData(GameData data) {
        this.data = data;
    }

    public void drawDebug() {
        debugRenderer.setProjectionMatrix(gameStage.getStage().getCamera().combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        data.getMap().getTiles().stream().forEach(t -> {
            Rectangle rectangle = t.getRectangle();
            if (t.isVisible()) {
                debugRenderer.setColor(Color.RED);
            } else {
                debugRenderer.setColor(Color.BLUE);
            }
            debugRenderer
                .rect(rectangle.getX() * Tile.SIZE, rectangle.getY() * Tile.SIZE, Tile.SIZE,
                    Tile.SIZE);
        });
        debugRenderer.end();
    }

}
