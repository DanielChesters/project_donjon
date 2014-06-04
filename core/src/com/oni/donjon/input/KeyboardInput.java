package com.oni.donjon.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.oni.donjon.data.GameData;
import com.oni.donjon.entity.Player;

/**
 * @author Daniel Chesters (on 20/05/14).
 */
public class KeyboardInput extends InputAdapter {
    private GameData data;

    public void setData(GameData data) {
        this.data = data;
    }

    @Override public boolean keyDown(int keycode) {
        Player player = data.getPlayer();
        switch (keycode) {
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                player.setDirection(Player.Direction.RIGHT);
                break;
            case Input.Keys.Q:
            case Input.Keys.LEFT:
                player.setDirection(Player.Direction.LEFT);
                break;
            case Input.Keys.Z:
            case Input.Keys.UP:
                player.setDirection(Player.Direction.UP);
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                player.setDirection(Player.Direction.DOWN);
                break;
            default:
                break;
        }
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            debugMessage(keycode);
        }
        return true;
    }

    @Override public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.D:
            case Input.Keys.RIGHT:
            case Input.Keys.Q:
            case Input.Keys.LEFT:
            case Input.Keys.Z:
            case Input.Keys.UP:
            case Input.Keys.S:
            case Input.Keys.DOWN:
                data.getPlayer().setDirection(Player.Direction.NONE);
                break;
            default:
                break;
        }

        return true;
    }

    private void debugMessage(int keycode) {
        Gdx.app.debug("Keyboard", Input.Keys.toString(keycode));
    }
}
