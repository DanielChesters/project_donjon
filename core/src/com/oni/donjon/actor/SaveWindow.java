package com.oni.donjon.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.oni.donjon.Resources;
import com.oni.donjon.data.GameData;
import lombok.Getter;

/**
 * @author Daniel Chesters (on 06/06/14).
 */
public class SaveWindow {
    @Getter
    private Window window;
    private List<String> saveList;
    private TextButton saveButton;
    private TextButton cancelButton;
    private TextButton newSaveButton;
    public SaveWindow(String title, Skin skin) {
        this.window = new Window(title, skin);

        createSaveList(skin);
        createSaveButton(skin);
        createCancelButton(skin);
        createNewSaveButton(skin);
        createWindow(skin);
    }

    private void createWindow(Skin skin) {
        ScrollPane saveScrollPane = new ScrollPane(saveList, skin);
        saveScrollPane.setFlickScroll(false);
        window.defaults().spaceBottom(10);
        window.row();
        window.add(saveScrollPane).fill().colspan(3).maxHeight(150);
        window.row().fill().expandX();
        window.add(saveButton).colspan(1);
        window.add(newSaveButton).colspan(1);
        window.add(cancelButton).colspan(1);
        window.setModal(true);
        window.setVisible(false);
    }

    private void createSaveList(Skin skin) {
        saveList = new List<>(skin);
        saveList.getSelection().setRequired(false);
        saveList.getSelection().setMultiple(false);
    }

    private void createNewSaveButton(Skin skin) {
        newSaveButton = new TextButton(Resources.BUNDLE.get("window.save.new"), skin);
        newSaveButton.pack();
        newSaveButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer,
                int button) {
                NewSaveInputLister newSaveInputLister =
                    new NewSaveInputLister(SaveWindow.this);
                Gdx.input
                    .getTextInput(newSaveInputLister, Resources.BUNDLE.get("window.save.new.input"),
                        "save", "");
                return true;
            }
        });
    }

    private void createCancelButton(Skin skin) {
        cancelButton = new TextButton(Resources.BUNDLE.get("window.save.cancel"), skin);
        cancelButton.pack();
        cancelButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer,
                int button) {
                SaveWindow.this.window.setVisible(false);
                return true;
            }
        });
    }

    private void createSaveButton(Skin skin) {
        saveButton = new TextButton(Resources.BUNDLE.get("window.save.ok"), skin);
        saveButton.pack();
        saveButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer,
                int button) {
                String saveName = saveList.getSelected();
                Json json = new Json();
                FileHandle file =
                    Gdx.files.external(String.format(".config/donjon/save/%s", saveName));
                String save = json.prettyPrint(GameData.INSTANCE.toGameSave());
                file.writeString(save, false);
                SaveWindow.this.window.setVisible(false);
                return true;
            }
        });
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


    static class NewSaveInputLister implements Input.TextInputListener {
        private SaveWindow saveWindow;

        NewSaveInputLister(SaveWindow saveWindow) {
            this.saveWindow = saveWindow;
        }

        @Override
        public void input(String text) {
            Json json = new Json();
            FileHandle file =
                Gdx.files.external(String.format(".config/donjon/save/%s.json", text));
            String save = json.prettyPrint(GameData.INSTANCE);
            file.writeString(save, false);
            saveWindow.getWindow().setVisible(false);
        }

        @Override
        public void canceled() {
            saveWindow.getWindow().setVisible(false);
        }
    }
}
