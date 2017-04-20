package com.oni.donjon.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.oni.donjon.DonjonGame;
import com.oni.donjon.Resources;
import com.oni.donjon.screen.GameScreen;
import com.oni.donjon.screen.MainScreen;

/**
 * @author Daniel Chesters (on 08/06/14).
 */
public class MenuGameWindow extends Window {
    private TextButton saveButton;
    private TextButton exitButton;
    private TextButton closeButton;

    public MenuGameWindow(Skin skin, SaveWindow saveWindow, DonjonGame game, GameScreen screen) {
        super(Resources.BUNDLE.get("game_menu.title"), skin, "dialog");
        createSaveButton(skin, saveWindow);
        createExitButton(skin, game);
        createCloseButton(skin, screen);
        createWindow();
    }

    private void createWindow() {
        row();
        add(saveButton).center();
        row();
        add(exitButton).center();
        row();
        add(closeButton).center();
        pack();
        setPosition(Gdx.graphics.getWidth() / 2f - getWidth() / 2f,
            Gdx.graphics.getHeight() / 2f - getHeight() / 2f);
        setModal(true);
        setMovable(false);
        setVisible(false);
    }

    private void createCloseButton(Skin skin, GameScreen screen) {
        closeButton =
            new TextButton(Resources.BUNDLE.get("game_menu.action.close"), skin);
        closeButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer,
                int button) {
                setVisible(false);
                screen.setState(GameScreen.GameState.RUNNING);
                return true;
            }
        });
        closeButton.pack();
    }

    private void createExitButton(Skin skin, DonjonGame game) {
        exitButton = new TextButton(Resources.BUNDLE.get("game_menu.action.exit"), skin);
        exitButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer,
                int button) {
                game.setScreen(new MainScreen(game));
                return true;
            }
        });
        exitButton.pack();
    }

    private void createSaveButton(Skin skin, SaveWindow saveWindow) {
        saveButton = new TextButton(Resources.BUNDLE.get("game_menu.action.save"), skin);
        saveButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer,
                int button) {
                saveWindow.show();
                setVisible(false);
                return true;
            }
        });
        saveButton.pack();
    }
}
