package com.oni.donjon.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.oni.donjon.action.Actions;
import com.oni.donjon.data.GameData;
import com.oni.donjon.map.Tile;
import com.oni.donjon.stage.GameStage;
import com.oni.donjon.stage.UIStage;

import java.util.Optional;

/**
 * @author Daniel Chesters (on 24/05/14).
 */
public class MouseInput extends InputAdapter {
    private GameStage gameStage;
    private UIStage uiStage;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 mouseLocation = getMouseLocation(screenX, screenY, button);

        if (button == Input.Buttons.LEFT) {
            mainAction(mouseLocation);
        }

        return true;
    }

    private Vector2 getMouseLocation(int screenX, int screenY, int button) {
        Gdx.app.debug("Mouse", String.format("Down : %d,%d : %d%n", screenX, screenY, button));
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        gameStage.getCamera().unproject(worldCoordinates);
        Vector2 mouseLocation = new Vector2(worldCoordinates.x, worldCoordinates.y);
        Gdx.app.debug("Mouse", String.format("%f,%f%n", mouseLocation.x, mouseLocation.y));
        Gdx.app.debug("Tile", String.format("%d,%d%n", (int) (mouseLocation.x / Tile.SIZE),
            (int) (mouseLocation.y / Tile.SIZE)));
        return mouseLocation;
    }

    private void mainAction(Vector2 mouseLocation) {
        Optional<Tile> tile =
            GameData.INSTANCE.getMap().getTile((int) (mouseLocation.x / Tile.SIZE),
                (int) (mouseLocation.y / Tile.SIZE));
        if (tile.isPresent() && tile.get().isKnow()) {
            Tile realTile = tile.get();
            Actions action = uiStage.getActionList().getSelected();
            if (action != null) {
                action.doAction(realTile, uiStage);
            }
        }
    }

    public void setGameStage(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    public void setUiStage(UIStage uiStage) {
        this.uiStage = uiStage;
    }
}
