package com.oni.donjon.stage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.oni.donjon.actor.MapActor;
import com.oni.donjon.data.GameData;
import com.oni.donjon.map.Tile;

/**
 * @author Daniel Chesters (on 01/06/14).
 */
public class GameStage extends Stage {
    private Label playerLabel;

    public GameStage() {
        super();
    }

    public void setPlayerLabel(Label playerLabel) {
        this.playerLabel = playerLabel;
    }

    public void updatePlayer() {
        Vector2 position = GameData.INSTANCE.getPlayerPosition();
        playerLabel.setPosition(position.x * Tile.SIZE, position.y * Tile.SIZE);
    }
}
