package com.oni.donjon.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

    public MouseInput(Character character, Map map, Camera camera, Label messageLabel) {
        this.character = character;
        this.map = map;
        this.camera = camera;
        this.messageLabel = messageLabel;
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
        if (tile.isPresent()) {
            Tile realTile = tile.get();
            if (characterSamePositionAsTile(realTile)) {
                messageLabel.setText("It is me...");
            } else {
                switch (realTile.getType()) {
                    case GROUND:
                        messageLabel.setText("Nothing");
                        break;
                    case WALL:
                        messageLabel.setText("A wall");
                        break;
                    case DOOR_OPEN:
                        changeStateDoorOrLookDoor(realTile, "The door is closed", TileType.DOOR_CLOSE, "A opened door");
                        break;
                    case DOOR_CLOSE:
                        changeStateDoorOrLookDoor(realTile, "The door is opened", TileType.DOOR_OPEN, "A closed door");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private boolean characterSamePositionAsTile(Tile tile) {
        Rectangle tileRectangle = tile.getRectangle();
        Vector2 characterPosition = character.getPosition();
        return tileRectangle.getX() == characterPosition.x && tileRectangle.getY() == characterPosition.y;
    }

    private void changeStateDoorOrLookDoor(Tile realTile, String changeStateDoorText, TileType newTypeDoor, String lookText) {
        if (isNearCharacter(realTile)) {
            messageLabel.setText(changeStateDoorText);
            realTile.setType(newTypeDoor);
        } else {
            messageLabel.setText(lookText);
        }
    }

    private boolean isNearCharacter(Tile tile) {
        Rectangle tileRectangle = tile.getRectangle();
        Vector2 characterPosition = character.getPosition();
        return Math.abs(characterPosition.x - tileRectangle.getX()) < 2 || Math.abs(characterPosition.y - tileRectangle.getY()) < 2;
    }
}
