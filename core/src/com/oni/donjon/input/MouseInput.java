package com.oni.donjon.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.oni.donjon.entity.Character;
import com.oni.donjon.map.Map;
import com.oni.donjon.map.Tile;

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
        Gdx.app.debug("Mouse", String.format("Down : %d,%d : %d%n", screenX, screenY, button));
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        camera.unproject(worldCoordinates);
        Vector2 mouseLocation = new Vector2(worldCoordinates.x, worldCoordinates.y);
        Gdx.app.debug("Mouse", String.format("%f,%f%n", mouseLocation.x, mouseLocation.y));
        Gdx.app.debug("Tile", String.format("%d,%d%n", (int) (mouseLocation.x / Tile.SIZE), (int) (mouseLocation.y / Tile.SIZE)));

        switch (button) {
            case Input.Buttons.LEFT:
                Optional<Tile> tile = map.getTile((int) (mouseLocation.x / Tile.SIZE), (int) (mouseLocation.y / Tile.SIZE));
                if (tile.isPresent()) {
                    Gdx.app.debug("Tile", tile.get().toString());
                    if (tile.get().getRectangle().getX() == character.getPosition().x && tile.get().getRectangle().getY() == character.getPosition().y) {
                        messageLabel.setText("It is me...");
                    } else {
                        switch (tile.get().getType()) {
                            case GROUND:
                                Gdx.app.log("Look", "Nothing");
                                messageLabel.setText("Nothing");
                                break;
                            case WALL:
                                Gdx.app.log("Look", "A wall");
                                messageLabel.setText("A wall");
                                break;
                        }
                    }

                }
                break;
            default:
                break;
        }
        return true;
    }
}
