package com.oni.donjon.action;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.oni.donjon.Resources;
import com.oni.donjon.map.Tile;
import com.oni.donjon.stage.UIStage;

/**
 * @author Daniel Chesters (on 02/06/14).
 */
public class LookAction extends AbstractAction {
    @Override
    public void doAction(Tile tile, UIStage stage) {
        Label messageLabel = stage.getMessageLabel();
        if (isPlayerSamePositionAsTile(tile)) {
            messageLabel.setText(Resources.BUNDLE.get("look.me"));
        } else {
            switch (tile.getType()) {
                case GROUND:
                    messageLabel.setText(Resources.BUNDLE.get("look.ground"));
                    break;
                case WALL:
                    messageLabel.setText(Resources.BUNDLE.get("look.wall"));
                    break;
                case DOOR_OPEN:
                    messageLabel.setText(Resources.BUNDLE.get("look.door.open"));
                    break;
                case DOOR_CLOSE:
                    messageLabel.setText(Resources.BUNDLE.get("look.door.close"));
                    break;
                case STAIR_UP:
                    messageLabel.setText(Resources.BUNDLE.get("look.stair.up"));
                    break;
                case STAIR_DOWN:
                    messageLabel.setText(Resources.BUNDLE.get("look.stair.down"));
                    break;
                default:
                    break;
            }
        }
    }
}
