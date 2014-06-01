package com.oni.donjon.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.oni.donjon.data.GameData;
import com.oni.donjon.entity.Player;
import com.oni.donjon.map.Map;
import com.oni.donjon.map.Tile;
import com.oni.donjon.stage.GameStage;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author Daniel Chesters (on 20/05/14).
 */
public class KeyboardInput extends InputAdapter {
    private GameData data;
    private GameStage gameStage;

    public void setData(GameData data) {
        this.data = data;
    }

    public void setGameStage(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    @Override
    public boolean keyDown(int keycode) {
        int numberCase = getCaseToGo();
        switch (keycode) {
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                goRight(numberCase);
                break;
            case Input.Keys.Q:
            case Input.Keys.LEFT:
                goLeft(numberCase);
                break;
            case Input.Keys.Z:
            case Input.Keys.UP:
                goUp(numberCase);
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                goDown(numberCase);
                break;
            default:
                break;
        }
        gameStage.updatePlayer();
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

    private void goDown(int numberCase) {
        Player player = data.getPlayer();
        Map map = data.getMap();
        IntStream.range(0, numberCase).forEach(i -> {
            Optional<Tile> tileDown = map.getTile((int) (player.getPosition().x),
                (int) (player.getPosition().y - 0.5f));
            if (tileDown.isPresent() && !tileDown.get().getType().isBlock()) {
                player.addY(-0.5f);
            }
            map.updateVisibility();
        });
    }

    private void goUp(int numberCase) {
        Player player = data.getPlayer();
        Map map = data.getMap();
        IntStream.range(0, numberCase).forEach(i -> {
            Optional<Tile> tileUp =
                map.getTile((int) (player.getPosition().x),
                    (int) (player.getPosition().y + 0.5f));
            if (tileUp.isPresent() && !tileUp.get().getType().isBlock()) {
                player.addY(0.5f);
            }
            map.updateVisibility();
        });
    }

    private void goLeft(int numberCase) {
        Player player = data.getPlayer();
        Map map = data.getMap();
        IntStream.range(0, numberCase).forEach(i -> {
            Optional<Tile> tileLeft =
                map.getTile((int) (player.getPosition().x - 0.5f),
                    (int) (player.getPosition().y));
            if (tileLeft.isPresent() && !tileLeft.get().getType().isBlock()) {
                player.addX(-0.5f);
            }
            map.updateVisibility();
        });
    }

    private void goRight(int numberCase) {
        Player player = data.getPlayer();
        Map map = data.getMap();
        IntStream.range(0, numberCase).forEach(i -> {
            Optional<Tile> tileRight =
                map.getTile((int) (player.getPosition().x + 0.5f),
                    (int) (player.getPosition().y));
            if (tileRight.isPresent() && !tileRight.get().getType().isBlock()) {
                player.addX(0.5f);
            }
            map.updateVisibility();
        });
    }

    private void debugMessage(int keycode) {
        Gdx.app.debug("Keyboard", Integer.toString(keycode));
        Gdx.app.debug("Player", data.getPlayer().toString());
    }
}
