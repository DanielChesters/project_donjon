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
            messageLabel.setText("I cannot close me");
        } else {
            switch (tile.getType()) {
                case DOOR_CLOSE:
                    messageLabel.setText("Already closed");
                    break;
                case DOOR_OPEN:
                    if (isNearCharacter(tile)) {
                        tile.setType(TileType.DOOR_CLOSE);
                        messageLabel.setText("The door is closed");
                    } else {
                        messageLabel.setText("The door is too far to be close");
                    }
                    break;
                default:
                    messageLabel.setText("Nothing to close here");
                    break;
            }
        }
    }

    private void open(Tile tile) {
        if (characterSamePositionAsTile(tile)) {
            messageLabel.setText("I cannot open me");
        } else {
            switch (tile.getType()) {
                case DOOR_OPEN:
                    messageLabel.setText("Already opened");
                    break;
                case DOOR_CLOSE:
                    if (isNearCharacter(tile)) {
                        tile.setType(TileType.DOOR_OPEN);
                        messageLabel.setText("The door is opened");
                    } else {
                        messageLabel.setText("The door is too far to be open");
                    }
                    break;
                default:
                    messageLabel.setText("Nothing to open here");
                    break;
            }
        }
    }

    private void look(Tile tile) {
        if (characterSamePositionAsTile(tile)) {
            messageLabel.setText("It is me...");
        } else {
            switch (tile.getType()) {
                case GROUND:
                    messageLabel.setText("Nothing");
                    break;
                case WALL:
                    messageLabel.setText("A wall");
                    break;
                case DOOR_OPEN:
                    messageLabel.setText("A opened door");
                    break;
                case DOOR_CLOSE:
                    messageLabel.setText("A closed door");
                    break;
                case STAIR_UP:
                    messageLabel.setText("A stair to up stage");
                    break;
                case STAIR_DOWN:
                    messageLabel.setText("A stair to down stage");
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
