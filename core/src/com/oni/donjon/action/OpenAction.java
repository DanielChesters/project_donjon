package com.oni.donjon.action;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.oni.donjon.Resources;
import com.oni.donjon.data.GameData;
import com.oni.donjon.map.Tile;
import com.oni.donjon.map.TileType;
import com.oni.donjon.sound.Sounds;
import com.oni.donjon.stage.UIStage;

/**
 * @author Daniel Chesters (on 02/06/14).
 */
public class OpenAction extends AbstractAction {
    @Override public void doAction(Tile tile, GameData data, UIStage stage) {
        Label messageLabel = stage.getMessageLabel();
        if (isPlayerSamePositionAsTile(tile, data)) {
            messageLabel.setText(Resources.BUNDLE.get("open.me"));
        } else {
            switch (tile.getType()) {
                case DOOR_OPEN:
                    messageLabel.setText(Resources.BUNDLE.get("open.door.already.open"));
                    break;
                case DOOR_CLOSE:
                    openClosedDoor(tile, stage, data);
                    break;
                default:
                    messageLabel.setText(Resources.BUNDLE.get("open.nothing"));
                    break;
            }
        }
    }

    private void openClosedDoor(Tile tile, UIStage stage, GameData data) {
        Label messageLabel = stage.getMessageLabel();
        if (isNearPlayer(tile, data)) {
            tile.setType(TileType.DOOR_OPEN);
            messageLabel.setText(Resources.BUNDLE.get("open.door"));
        } else {
            messageLabel.setText(Resources.BUNDLE.get("open.door.too.far"));
        }
        Sounds.OPEN_DOOR.play();
    }
}
