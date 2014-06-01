package com.oni.donjon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

        TextButton newGameButton =
            new TextButton(Resources.BUNDLE.get("main.screen.new_game.title"), skin);
        newGameButton.pack();
        newGameButton.setPosition(Gdx.graphics.getWidth() / 2 - newGameButton.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - newGameButton.getHeight() / 2);
        newGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen());
                return true;
            }
        });

        stage.addActor(newGameButton);
        Gdx.input.setInputProcessor(stage);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }
}
