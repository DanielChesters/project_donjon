package com.oni.donjon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.oni.donjon.DonjonGame;
import com.oni.donjon.Resources;

/**
 * @author Daniel Chesters (on 01/06/14).
 */
public class MainScreen extends ScreenAdapter {
    private DonjonGame game;
    private Stage stage;


    public MainScreen(DonjonGame game) {
        this.game = game;
        this.stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        Window loadWindow =
            new Window(Resources.BUNDLE.get("main.screen.load.title"), skin, "dialog");

        List<String> saveList = createSaveList(skin);

        TextButton okLoadButton = createLoadButton(game, skin, saveList);

        TextButton closeLoadWindowButton = createCloseLoadWindowsButton(skin, loadWindow);

        ScrollPane saveScrollPane = new ScrollPane(saveList, skin);
        saveScrollPane.setFlickScroll(false);
        loadWindow.defaults().spaceBottom(10);
        loadWindow.row();
        loadWindow.add(saveScrollPane).fill().colspan(2).maxHeight(150);
        loadWindow.row().fill().expandX();
        loadWindow.add(okLoadButton).colspan(1);
        loadWindow.add(closeLoadWindowButton).colspan(1);
        loadWindow.setModal(true);

        loadWindow.setPosition(Gdx.graphics.getWidth() / 2 - loadWindow.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - loadWindow.getHeight() / 2);
        loadWindow.setVisible(false);

        TextButton newGameButton = createNewGameButton(game, skin);

        TextButton loadGameButton = createLoadGameButton(skin, loadWindow, saveList);

        TextButton exitGameButton = createExitGameButton(skin, loadGameButton);

        stage.addActor(newGameButton);
        stage.addActor(loadGameButton);
        stage.addActor(exitGameButton);
        stage.addActor(loadWindow);
        Gdx.input.setInputProcessor(stage);
    }

    private TextButton createExitGameButton(Skin skin, TextButton loadGameButton) {
        TextButton exitGameButton =
            new TextButton(Resources.BUNDLE.get("main.screen.exit.title"), skin);
        exitGameButton.pack();
        exitGameButton.setPosition(Gdx.graphics.getWidth() / 2 - exitGameButton.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - (exitGameButton.getHeight() + loadGameButton.getHeight()
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

    private TextButton createLoadGameButton(Skin skin, final Window loadWindow,
        final List<String> saveList) {
        TextButton loadGameButton =
            new TextButton(Resources.BUNDLE.get("main.screen.load_game.title"), skin);
        loadGameButton.pack();
        loadGameButton.setPosition(Gdx.graphics.getWidth() / 2 - loadGameButton.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - (loadGameButton.getHeight() + 20));
        loadGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                FileHandle saveFolder = Gdx.files.external(".config/donjon/save");
                Array<String> saveArray = new Array<>();
                for (FileHandle file : saveFolder.list((dir, name) -> name.endsWith(".json"))) {
                    saveArray.add(file.name());
                }
                saveList.setItems(saveArray);
                loadWindow.setVisible(true);
                loadWindow.pack();
                return true;
            }
        });
        return loadGameButton;
    }

    private TextButton createNewGameButton(final DonjonGame game, Skin skin) {
        TextButton newGameButton =
            new TextButton(Resources.BUNDLE.get("main.screen.new_game.title"), skin);
        newGameButton.pack();
        newGameButton.setPosition(Gdx.graphics.getWidth() / 2 - newGameButton.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - newGameButton.getHeight() / 2);
        newGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                return true;
            }
        });
        return newGameButton;
    }

    private TextButton createCloseLoadWindowsButton(Skin skin, final Window loadWindow) {
        TextButton closeLoadWindowButton =
            new TextButton(Resources.BUNDLE.get("main.screen.load.cancel"), skin);
        closeLoadWindowButton.pack();
        closeLoadWindowButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer,
                int button) {
                loadWindow.setVisible(false);
                return true;
            }
        });
        return closeLoadWindowButton;
    }

    private TextButton createLoadButton(final DonjonGame game, Skin skin,
        final List<String> saveList) {
        TextButton okLoadButton = new TextButton(Resources.BUNDLE.get("main.screen.load.ok"), skin);
        okLoadButton.pack();
        okLoadButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer,
                int button) {
                String save = saveList.getSelected();
                game.setScreen(new GameScreen(game, String.format(".config/donjon/save/%s", save)));
                return true;
            }
        });
        return okLoadButton;
    }

    private List<String> createSaveList(Skin skin) {
        List<String> saveList = new List<>(skin);
        saveList.getSelection().setRequired(true);
        saveList.getSelection().setMultiple(false);
        return saveList;
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }
}
