package com.oni.donjon.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.oni.donjon.Resources;
import com.oni.donjon.data.GameData;
import com.oni.donjon.map.Tile;
import com.oni.donjon.map.TileType;
import com.oni.donjon.stage.GameStage;
import com.oni.donjon.stage.UIStage;

import java.util.Optional;

/**
 * @author Daniel Chesters (on 24/05/14).
 */
public class MouseInput extends InputAdapter {
    private GameData data;
    private GameStage gameStage;
    private UIStage uiStage;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 mouseLocation = getMouseLocation(screenX, screenY, button);

        switch (button) {
            case Input.Buttons.LEFT:
                mainAction(mouseLocation);
                break;
            default:
                break;
        }
        return true;
    }

    private Vector2 getMouseLocation(int screenX, int screenY, int button) {
        Gdx.app.debug("Mouse", String.format("Down : %d,%d : %d%n", screenX, screenY, button));
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        gameStage.getStage().getCamera().unproject(worldCoordinates);
        Vector2 mouseLocation = new Vector2(worldCoordinates.x, worldCoordinates.y);
        Gdx.app.debug("Mouse", String.format("%f,%f%n", mouseLocation.x, mouseLocation.y));
        Gdx.app.debug("Tile", String.format("%d,%d%n", (int) (mouseLocation.x / Tile.SIZE),
            (int) (mouseLocation.y / Tile.SIZE)));
        return mouseLocation;
    }

    private void mainAction(Vector2 mouseLocation) {
        Optional<Tile> tile =
            data.getMap().getTile((int) (mouseLocation.x / Tile.SIZE),
                (int) (mouseLocation.y / Tile.SIZE));
        if (tile.isPresent() && tile.get().isVisible()) {
            Tile realTile = tile.get();
            switch (uiStage.getActionList().getSelected()) {
                case LOOK:
                    look(realTile);
                    break;
                case OPEN:
                    open(realTile);
                    break;
                case CLOSE:
                    close(realTile);
                    break;
                default:
                    break;
            }
        }
    }

    private void close(Tile tile) {
        Label messageLabel = uiStage.getMessageLabel();
        if (isPlayerSamePositionAsTile(tile)) {
            messageLabel.setText(Resources.BUNDLE.get("close.me"));
        } else {
            switch (tile.getType()) {
                case DOOR_CLOSE:
                    messageLabel.setText(Resources.BUNDLE.get("close.door.already.close"));
                    break;
                case DOOR_OPEN:
                    closeOpenedDoor(tile);
                    break;
                default:
                    messageLabel.setText(Resources.BUNDLE.get("close.nothing"));
                    break;
            }
        }
    }

    private void closeOpenedDoor(Tile tile) {
        Label messageLabel = uiStage.getMessageLabel();
        if (isNearPlayer(tile)) {
            tile.setType(TileType.DOOR_CLOSE);
            messageLabel.setText(Resources.BUNDLE.get("close.door"));
        } else {
            messageLabel.setText(Resources.BUNDLE.get("close.door.too.far"));
        }
    }

    private void open(Tile tile) {
        Label messageLabel = uiStage.getMessageLabel();
        if (isPlayerSamePositionAsTile(tile)) {
            messageLabel.setText(Resources.BUNDLE.get("open.me"));
        } else {
            switch (tile.getType()) {
                case DOOR_OPEN:
                    messageLabel.setText(Resources.BUNDLE.get("open.door.already.open"));
                    break;
                case DOOR_CLOSE:
                    openClosedDoor(tile);
                    break;
                default:
                    messageLabel.setText(Resources.BUNDLE.get("open.nothing"));
                    break;
            }
        }
    }

    private void openClosedDoor(Tile tile) {
        Label messageLabel = uiStage.getMessageLabel();
        if (isNearPlayer(tile)) {
            tile.setType(TileType.DOOR_OPEN);
            messageLabel.setText(Resources.BUNDLE.get("open.door"));
        } else {
            messageLabel.setText(Resources.BUNDLE.get("open.door.too.far"));
        }
    }

    private void look(Tile tile) {
        Label messageLabel = uiStage.getMessageLabel();
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


    private boolean isPlayerSamePositionAsTile(Tile tile) {
        return testPositionBetweenTileAndPlayer(tile, 1);
    }

    private boolean isNearPlayer(Tile tile) {
        return testPositionBetweenTileAndPlayer(tile, 1.5f);
    }

    private boolean testPositionBetweenTileAndPlayer(Tile tile, float distance) {
        Rectangle tileRectangle = tile.getRectangle();
        Vector2 playerPosition = data.getPlayer().getPosition();
        return Math.abs(tileRectangle.getX() - (int) playerPosition.x) < distance
            && Math.abs(tileRectangle.getY() - (int) playerPosition.y) < distance;
    }

    public void setData(GameData data) {
        this.data = data;
    }

    public void setGameStage(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    public void setUiStage(UIStage uiStage) {
        this.uiStage = uiStage;
    }
}
