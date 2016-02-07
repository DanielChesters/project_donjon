package com.oni.donjon.stage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.oni.donjon.data.GameData;
import com.oni.donjon.map.Tile;

/**
 * @author Daniel Chesters (on 01/06/14).
 */
public class GameStage {
    private Stage stage;
    private Label playerLabel;
    private GameData data;

    public GameStage() {
        stage = new Stage();
    }

    public Stage getStage() {
        return stage;
    }

    public void setPlayerLabel(Label playerLabel) {
        this.playerLabel = playerLabel;
    }

    public void updatePlayer() {
        Vector2 position = data.getPlayerPosition();
        playerLabel.setPosition(position.x * 16f, position.y * 16f);
    }

    public void setData(GameData data) {
        this.data = data;
    }
}
