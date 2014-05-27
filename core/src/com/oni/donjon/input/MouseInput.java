package com.oni.donjon.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.oni.donjon.Resources;
import com.oni.donjon.action.Action;
import com.oni.donjon.entity.Character;
import com.oni.donjon.map.Map;
import com.oni.donjon.map.Tile;
import com.oni.donjon.map.TileType;

import java.util.Optional;

/**
 * @author Daniel Chesters (on 24/05/14).
 */
public class MouseInput extends InputAdapter {

    private Character character;
    private Map map;
    private Camera camera;
    private Label messageLabel;
    private List<Action> actionList;

    public void setCharacter(Character character) {
        this.character = character;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setMessageLabel(Label messageLabel) {
        this.messageLabel = messageLabel;
    }

    public void setActionList(List<Action> actionList) {
        this.actionList = actionList;
    }

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
        camera.unproject(worldCoordinates);
        Vector2 mouseLocation = new Vector2(worldCoordinates.x, worldCoordinates.y);
        Gdx.app.debug("Mouse", String.format("%f,%f%n", mouseLocation.x, mouseLocation.y));
        Gdx.app.debug("Tile", String.format("%d,%d%n", (int) (mouseLocation.x / Tile.SIZE), (int) (mouseLocation.y / Tile.SIZE)));
        return mouseLocation;
    }

    private void mainAction(Vector2 mouseLocation) {
        Optional<Tile> tile = map.getTile((int) (mouseLocation.x / Tile.SIZE), (int) (mouseLocation.y / Tile.SIZE));
        if (tile.isPresent() && tile.get().isVisible()) {
            Tile realTile = tile.get();
            switch (actionList.getSelected()) {
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
        if (characterSamePositionAsTile(tile)) {
            messageLabel.setText(Resources.bundle.get("close.me"));
        } else {
            switch (tile.getType()) {
                case DOOR_CLOSE:
                    messageLabel.setText(Resources.bundle.get("close.door.already.close"));
                    break;
                case DOOR_OPEN:
                    if (isNearCharacter(tile)) {
                        tile.setType(TileType.DOOR_CLOSE);
                        messageLabel.setText(Resources.bundle.get("close.door"));
                    } else {
                        messageLabel.setText(Resources.bundle.get("close.door.too.far"));
                    }
                    break;
                default:
                    messageLabel.setText(Resources.bundle.get("close.nothing"));
                    break;
            }
        }
    }

    private void open(Tile tile) {
        if (characterSamePositionAsTile(tile)) {
            messageLabel.setText(Resources.bundle.get("open.me"));
        } else {
            switch (tile.getType()) {
                case DOOR_OPEN:
                    messageLabel.setText(Resources.bundle.get("open.door.already.open"));
                    break;
                case DOOR_CLOSE:
                    if (isNearCharacter(tile)) {
                        tile.setType(TileType.DOOR_OPEN);
                        messageLabel.setText(Resources.bundle.get("open.door"));
                    } else {
                        messageLabel.setText(Resources.bundle.get("open.door.too.far"));
                    }
                    break;
                default:
                    messageLabel.setText(Resources.bundle.get("open.nothing"));
                    break;
            }
        }
    }

    private void look(Tile tile) {
        if (characterSamePositionAsTile(tile)) {
            messageLabel.setText(Resources.bundle.get("look.me"));
        } else {
            switch (tile.getType()) {
                case GROUND:
                    messageLabel.setText(Resources.bundle.get("look.ground"));
                    break;
                case WALL:
                    messageLabel.setText(Resources.bundle.get("look.wall"));
                    break;
                case DOOR_OPEN:
                    messageLabel.setText(Resources.bundle.get("look.door.open"));
                    break;
                case DOOR_CLOSE:
                    messageLabel.setText(Resources.bundle.get("look.door.close"));
                    break;
                case STAIR_UP:
                    messageLabel.setText(Resources.bundle.get("look.stair.up"));
                    break;
                case STAIR_DOWN:
                    messageLabel.setText(Resources.bundle.get("look.stair.down"));
                    break;
                default:
                    break;
            }
        }
    }


    private boolean characterSamePositionAsTile(Tile tile) {
        Rectangle tileRectangle = tile.getRectangle();
        Vector2 characterPosition = character.getPosition();
        return Math.abs(tileRectangle.getX() - (int) characterPosition.x) < 1 && Math.abs(tileRectangle.getY() - (int) characterPosition.y) < 1;
    }

    private boolean isNearCharacter(Tile tile) {
        Rectangle tileRectangle = tile.getRectangle();
        Vector2 characterPosition = character.getPosition();
        return Math.abs(tileRectangle.getX() - (int) characterPosition.x) < 1.5 && Math.abs(tileRectangle.getY() - (int) characterPosition.y) < 1.5;
    }
}
