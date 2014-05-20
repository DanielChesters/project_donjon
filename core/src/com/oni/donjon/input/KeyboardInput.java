package com.oni.donjon.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.oni.donjon.entity.Character;

/**
 * Created by daniel on 20/05/14.
 */
public class KeyboardInput implements InputProcessor {
    Character character;

    public KeyboardInput(Character character) {
        this.character = character;
    }

    @Override
    public boolean keyDown(int keycode) {
        int val = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            val = 10;
        } else {
            val = 1;
        }
        switch (keycode) {
            case Input.Keys.RIGHT:
                character.addX(val);
                break;
            case Input.Keys.LEFT:
                character.addX(-val);
                break;
            case Input.Keys.UP:
                character.addY(val);
                break;
            case Input.Keys.DOWN:
                character.addY(-val);
                break;
            default:
                break;
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
}
