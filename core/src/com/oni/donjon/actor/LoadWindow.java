package com.oni.donjon.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.oni.donjon.DonjonGame;
import com.oni.donjon.Resources;
import com.oni.donjon.screen.GameScreen;
import lombok.Getter;

/**
 * @author Daniel Chesters (on 06/06/14).
 */
public class LoadWindow {
    @Getter
    private Window window;
    private List<String> saveList;
    private TextButton loadButton;
    private TextButton cancelButton;

    public LoadWindow(String title, Skin skin, DonjonGame game) {
        this.window = new Window(title, skin);

        createSaveList(skin);
        createLoadButton(game, skin, saveList);
        createCancelButton(skin);
        createWindow(skin);
    }

    private void createWindow(Skin skin) {
        ScrollPane saveScrollPane = new ScrollPane(saveList, skin);
        saveScrollPane.setFlickScroll(false);
        window.defaults().spaceBottom(10);
        window.row();
        window.add(saveScrollPane).fill().colspan(2).maxHeight(150);
        window.row().fill().expandX();
        window.add(loadButton).colspan(1);
        window.add(cancelButton).colspan(1);
        window.setModal(true);
        window.setVisible(false);
    }

    private void createCancelButton(Skin skin) {
        cancelButton =
            new TextButton(Resources.BUNDLE.get("main.screen.load.cancel"), skin);
        cancelButton.pack();
        cancelButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer,
                int button) {
                LoadWindow.this.window.setVisible(false);
                return true;
            }
        });
    }

    private void createLoadButton(final DonjonGame game, Skin skin,
        final List<String> saveList) {
        loadButton = new TextButton(Resources.BUNDLE.get("main.screen.load.ok"), skin);
        loadButton.pack();
        loadButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer,
                int button) {
                String save = saveList.getSelected();
                game.setScreen(new GameScreen(game, String.format(".config/donjon/save/%s", save)));
                return true;
            }
        });
    }

    private void createSaveList(Skin skin) {
        saveList = new List<>(skin);
        saveList.getSelection().setRequired(true);
        saveList.getSelection().setMultiple(false);
    }

    public void show() {
        FileHandle saveFolder = Gdx.files.external(".config/donjon/save");
        Array<String> saveArray = new Array<>();
        for (FileHandle file : saveFolder.list((dir, name) -> name.endsWith(".json"))) {
            saveArray.add(file.name());
        }
        saveList.setItems(saveArray);
        window.pack();
        window.setPosition(Gdx.graphics.getWidth() / 2f - window.getWidth() / 2f,
            Gdx.graphics.getHeight() / 2f - window.getHeight() / 2f);
        window.setVisible(true);
    }
}
