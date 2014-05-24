package com.oni.donjon.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.oni.donjon.entity.Character;
import com.oni.donjon.map.Map;
import com.oni.donjon.map.Tile;

import java.util.Optional;

/**
 * Created by daniel on 24/05/14.
 */
public class MouseInput extends InputAdapter {

    private Character character;
    private Map map;
    private OrthographicCamera camera;

    public MouseInput(Character character, Map map, OrthographicCamera camera) {
        this.character = character;
        this.map = map;
        this.camera = camera;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.debug("Mouse", String.format("Down : %d,%d : %d%n", screenX, screenY, button));
        Vector2 centerPosition = character.getPosition();
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        camera.unproject(worldCoordinates);
        Vector2 mouseLocation = new Vector2(worldCoordinates.x, worldCoordinates.y);
        Gdx.app.debug("Mouse", String.format("%f,%f%n", mouseLocation.x, mouseLocation.y));

        switch (button) {
            case Input.Buttons.LEFT:
                Optional<Tile> tile = map.getTile((int)(mouseLocation.x / Tile.SIZE), (int)(mouseLocation.y / Tile.SIZE));
                if (tile.isPresent()) {
                    Gdx.app.debug("Tile", tile.get().toString());
                    switch (tile.get().getType()) {
                        case GROUND:
                            Gdx.app.log("Look", "Nothing…");
                            break;
                        case WALL:
                            Gdx.app.log("Look", "A wall…");
                            break;

                    }
                }
                break;
            default:
                break;
        }
        return true;
    }
}
