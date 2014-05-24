package com.oni.donjon.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.oni.donjon.entity.Character;
import com.oni.donjon.map.Map;
import com.oni.donjon.map.Tile;

import java.util.Optional;

/**
 * @author Daniel Chesters (on 20/05/14).
 */
public class KeyboardInput implements InputProcessor {
    Character character;
    Map map;

    public KeyboardInput(Character character, Map map) {
        this.character = character;
        this.map = map;
    }

    @Override
    public boolean keyDown(int keycode) {
        float val;
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            val = 5;
        } else {
            val = 0.5f;
        }
        switch (keycode) {
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                Gdx.app.debug("Key", MathUtils.round(character.getPosition().x + val) + "," + MathUtils.round(character.getPosition().y));
                Optional<Tile> tileRight = map.getTile(MathUtils.round(character.getPosition().x + val), MathUtils.round(character.getPosition().y));
                if (tileRight.isPresent() && !tileRight.get().getType().isBlock()) {
                    character.addX(val);
                }
                break;
            case Input.Keys.Q:
            case Input.Keys.LEFT:
                Gdx.app.debug("Key", MathUtils.round(character.getPosition().x - val) + "," + MathUtils.round(character.getPosition().y));
                Optional<Tile> tileLeft = map.getTile(MathUtils.round(character.getPosition().x - val), MathUtils.round(character.getPosition().y));
                if (tileLeft.isPresent() && !tileLeft.get().getType().isBlock()) {
                    character.addX(-val);
                }
                break;
            case Input.Keys.Z:
            case Input.Keys.UP:
                Gdx.app.debug("Key", MathUtils.round(character.getPosition().x) + "," + MathUtils.round(character.getPosition().y + val));
                Optional<Tile> tileUp = map.getTile(MathUtils.round(character.getPosition().x), MathUtils.round(character.getPosition().y + val));
                if (tileUp.isPresent() && !tileUp.get().getType().isBlock()) {
                    character.addY(val);
                }
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                Gdx.app.debug("Key", MathUtils.round(character.getPosition().x) + "," + MathUtils.round(character.getPosition().y - val));
                Optional<Tile> tileDown = map.getTile(MathUtils.round(character.getPosition().x), MathUtils.round(character.getPosition().y - val));
                if (tileDown.isPresent() && !tileDown.get().getType().isBlock()) {
                    character.addY(-val);
                }
                break;
            default:
                break;
        }
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            debugMessage(keycode);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private void debugMessage(int keycode) {
        Gdx.app.debug("Keyboard", Integer.toString(keycode));
    }
}
