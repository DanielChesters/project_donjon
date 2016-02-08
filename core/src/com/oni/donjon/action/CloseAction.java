package com.oni.donjon.action;

import com.badlogic.gdx.physics.box2d.Body;
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
public class CloseAction extends AbstractAction {
    @Override public void doAction(Tile tile, GameData data, UIStage stage) {
        Label messageLabel = stage.getMessageLabel();
        if (isPlayerSamePositionAsTile(tile, data)) {
            messageLabel.setText(Resources.BUNDLE.get("close.me"));
        } else {
            switch (tile.getType()) {
                case DOOR_CLOSE:
                    messageLabel.setText(Resources.BUNDLE.get("close.door.already.close"));
                    break;
                case DOOR_OPEN:
                    closeOpenedDoor(tile, stage, data);
                    break;
                default:
                    messageLabel.setText(Resources.BUNDLE.get("close.nothing"));
                    break;
            }
        }
    }

    private void closeOpenedDoor(Tile tile, UIStage stage, GameData data) {
        Label messageLabel = stage.getMessageLabel();
        if (isNearPlayer(tile, data)) {
            tile.setType(TileType.DOOR_CLOSE);
            tile.setBody(tile.createBody(GameData.INSTANCE.getWorld()));
            messageLabel.setText(Resources.BUNDLE.get("close.door"));
            Sounds.CLOSE_DOOR.play();
        } else {
            messageLabel.setText(Resources.BUNDLE.get("close.door.too.far"));
        }
    }
}
