package com.oni.donjon.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.oni.donjon.entity.Character;
import com.oni.donjon.map.Map;
import com.oni.donjon.map.Tile;

import java.util.Optional;

/**
 * @author Daniel Chesters (on 20/05/14).
 */
public class KeyboardInput extends InputAdapter {
    private Character character;
    private Map map;

    public KeyboardInput(Character character, Map map) {
        this.character = character;
        this.map = map;
    }

    @Override
    public boolean keyDown(int keycode) {
        float speed = getSpeed();
        switch (keycode) {
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                goRight(speed);
                break;
            case Input.Keys.Q:
            case Input.Keys.LEFT:
                goLeft(speed);
                break;
            case Input.Keys.Z:
            case Input.Keys.UP:
                goUp(speed);
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                goDown(speed);
                break;
            default:
                break;
        }
        character.updateCharacter();
        map.updateVisibility();
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            debugMessage(keycode);
        }
        return true;
    }

    private float getSpeed() {
        float val;
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            val = 5;
        } else {
            val = 0.5f;
        }
        return val;
    }

    private void goDown(float speed) {
        Optional<Tile> tileDown = map.getTile((int) (character.getPosition().x), (int) (character.getPosition().y - speed));
        if (tileDown.isPresent() && !tileDown.get().getType().isBlock()) {
            character.addY(-speed);
        }
    }

    private void goUp(float val) {
        Optional<Tile> tileUp = map.getTile((int) (character.getPosition().x), (int) (character.getPosition().y + val));
        if (tileUp.isPresent() && !tileUp.get().getType().isBlock()) {
            character.addY(val);
        }
    }

    private void goLeft(float val) {
        Optional<Tile> tileLeft = map.getTile((int) (character.getPosition().x - val), (int) (character.getPosition().y));
        if (tileLeft.isPresent() && !tileLeft.get().getType().isBlock()) {
            character.addX(-val);
        }
    }

    private void goRight(float val) {
        Optional<Tile> tileRight = map.getTile((int) (character.getPosition().x + val), (int) (character.getPosition().y));
        if (tileRight.isPresent() && !tileRight.get().getType().isBlock()) {
            character.addX(val);
        }
    }

    private void debugMessage(int keycode) {
        Gdx.app.debug("Keyboard", Integer.toString(keycode));
        Gdx.app.debug("Character", character.toString());
    }
}
