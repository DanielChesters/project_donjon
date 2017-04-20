package com.oni.donjon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.oni.donjon.DonjonGame;
import com.oni.donjon.Resources;
import com.oni.donjon.actor.LoadWindow;

/**
 * @author Daniel Chesters (on 01/06/14).
 */
public class MainScreen extends ScreenAdapter {
    private Stage stage;


    public MainScreen(DonjonGame game) {
        this.stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        Table mainTable = new Table(skin);

        LoadWindow loadWindow =
            new LoadWindow(Resources.BUNDLE.get("main.screen.load.title"), skin, game);

        TextButton newGameButton = createNewGameButton(game, skin);

        TextButton loadGameButton = createLoadGameButton(skin, loadWindow);

        TextButton exitGameButton = createExitGameButton(skin, loadGameButton);

        mainTable.defaults().space(5);
        mainTable.row();
        mainTable.add(newGameButton).center();
        mainTable.row();
        mainTable.add(loadGameButton).center();
        mainTable.row();
        mainTable.add(exitGameButton).center();
        mainTable.pack();
        mainTable.setPosition(Gdx.graphics.getWidth() / 2f - mainTable.getWidth() / 2f,
            Gdx.graphics.getHeight() / 2f - mainTable.getHeight() / 2f);

        stage.addActor(mainTable);
        stage.addActor(loadWindow.getWindow());
        Gdx.input.setInputProcessor(stage);
    }

    private TextButton createExitGameButton(Skin skin, TextButton loadGameButton) {
        TextButton exitGameButton =
            new TextButton(Resources.BUNDLE.get("main.screen.exit.title"), skin);
        exitGameButton.pack();
        exitGameButton.setPosition(Gdx.graphics.getWidth() / 2f - exitGameButton.getWidth() / 2f,
            Gdx.graphics.getHeight() / 2f - (exitGameButton.getHeight() + loadGameButton.getHeight()
                + 30));
        exitGameButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer,
                int button) {
                Gdx.app.exit();
                return true;
            }
        });
        return exitGameButton;
    }

    private TextButton createLoadGameButton(Skin skin, final LoadWindow loadWindow) {
        TextButton loadGameButton =
            new TextButton(Resources.BUNDLE.get("main.screen.load_game.title"), skin);
        loadGameButton.pack();
        loadGameButton.setPosition(Gdx.graphics.getWidth() / 2f - loadGameButton.getWidth() / 2f,
            Gdx.graphics.getHeight() / 2f - (loadGameButton.getHeight() + 20));
        loadGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                loadWindow.show();
                return true;
            }
        });
        return loadGameButton;
    }

    private TextButton createNewGameButton(final DonjonGame game, Skin skin) {
        TextButton newGameButton =
            new TextButton(Resources.BUNDLE.get("main.screen.new_game.title"), skin);
        newGameButton.pack();
        newGameButton.setPosition(Gdx.graphics.getWidth() / 2f - newGameButton.getWidth() / 2f,
            Gdx.graphics.getHeight() / 2f - newGameButton.getHeight() / 2f);
        newGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                return true;
            }
        });
        return newGameButton;
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }
}
