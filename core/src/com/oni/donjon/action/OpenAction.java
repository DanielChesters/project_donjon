package com.oni.donjon.action;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.oni.donjon.Resources;
import com.oni.donjon.map.Tile;
import com.oni.donjon.map.TileType;
import com.oni.donjon.sound.Sounds;
import com.oni.donjon.stage.UIStage;

/**
 * @author Daniel Chesters (on 02/06/14).
 */
public class OpenAction extends AbstractAction {
    @Override
    public void doAction(Tile tile, UIStage stage) {
        Label messageLabel = stage.getMessageLabel();
        if (isPlayerSamePositionAsTile(tile)) {
            messageLabel.setText(Resources.BUNDLE.get("open.me"));
        } else {
            switch (tile.getType()) {
                case DOOR_OPEN:
                    messageLabel.setText(Resources.BUNDLE.get("open.door.already.open"));
                    break;
                case DOOR_CLOSE:
                    openClosedDoor(tile, stage);
                    break;
                default:
                    messageLabel.setText(Resources.BUNDLE.get("open.nothing"));
                    break;
            }
        }
    }

    private void openClosedDoor(Tile tile, UIStage stage) {
        Label messageLabel = stage.getMessageLabel();
        if (isNearPlayer(tile)) {
            Body bodyDoor = tile.getBody();
            bodyDoor.getWorld().destroyBody(bodyDoor);
            tile.setType(TileType.DOOR_OPEN);
            messageLabel.setText(Resources.BUNDLE.get("open.door"));
            Sounds.OPEN_DOOR.play();
        } else {
            messageLabel.setText(Resources.BUNDLE.get("open.door.too.far"));
        }
    }
}
