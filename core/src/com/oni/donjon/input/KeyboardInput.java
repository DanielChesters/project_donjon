package com.oni.donjon.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.oni.donjon.entity.Character;
import com.oni.donjon.map.Map;
import com.oni.donjon.map.Tile;

import java.util.Optional;
import java.util.stream.IntStream;

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
        int numberCase = getCaseToGo();
        switch (keycode) {
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                IntStream.range(0, numberCase).forEach(i -> goRight());
                break;
            case Input.Keys.Q:
            case Input.Keys.LEFT:
                IntStream.range(0, numberCase).forEach(i -> goLeft());
                break;
            case Input.Keys.Z:
            case Input.Keys.UP:
                IntStream.range(0, numberCase).forEach(i -> goUp());
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                IntStream.range(0, numberCase).forEach(i -> goDown());
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

    private int getCaseToGo() {
        int numberCase;
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) || Gdx.input
            .isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            numberCase = 10;
        } else {
            numberCase = 1;
        }
        return numberCase;
    }

    private void goDown() {
        Optional<Tile> tileDown = map.getTile((int) (character.getPosition().x),
            (int) (character.getPosition().y - 0.5f));
        if (tileDown.isPresent() && !tileDown.get().getType().isBlock()) {
            character.addY(-0.5f);
        }
    }

    private void goUp() {
        Optional<Tile> tileUp =
            map.getTile((int) (character.getPosition().x),
                (int) (character.getPosition().y + 0.5f));
        if (tileUp.isPresent() && !tileUp.get().getType().isBlock()) {
            character.addY(0.5f);
        }
    }

    private void goLeft() {
        Optional<Tile> tileLeft =
            map.getTile((int) (character.getPosition().x - 0.5f),
                (int) (character.getPosition().y));
        if (tileLeft.isPresent() && !tileLeft.get().getType().isBlock()) {
            character.addX(-0.5f);
        }
    }

    private void goRight() {
        Optional<Tile> tileRight =
            map.getTile((int) (character.getPosition().x + 0.5f),
                (int) (character.getPosition().y));
        if (tileRight.isPresent() && !tileRight.get().getType().isBlock()) {
            character.addX(0.5f);
        }
    }

    private void debugMessage(int keycode) {
        Gdx.app.debug("Keyboard", Integer.toString(keycode));
        Gdx.app.debug("Character", character.toString());
    }
}
