package com.oni.donjon.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.oni.donjon.entity.Character;

/**
 * @author Daniel Chesters (on 20/05/14).
 *
 */
public class KeyboardInput implements InputProcessor {
    Character character;

    public KeyboardInput(Character character) {
        this.character = character;
    }

    @Override
    public boolean keyDown(int keycode) {
        int val;
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            val = 10;
        } else {
            val = 1;
        }
        switch (keycode) {
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                character.addX(val);
                break;
            case Input.Keys.Q:
            case Input.Keys.LEFT:
                character.addX(-val);
                break;
            case Input.Keys.Z:
            case Input.Keys.UP:
                character.addY(val);
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                character.addY(-val);
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
