package com.oni.donjon;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.oni.donjon.actor.MapActor;
import com.oni.donjon.entity.Character;
import com.oni.donjon.input.KeyboardInput;
import com.oni.donjon.input.MouseInput;
import com.oni.donjon.map.Tile;

public class DonjonGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Character character;
    private MapActor mapActor;
    private ShapeRenderer debugRenderer;
    private Stage stageUi;
    private Stage stage;

    @Override
    public void create() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        stageUi = new Stage();
        stage = new Stage();
        final Label messageLabel = new Label("", skin, "default");
        messageLabel.setWidth(100f);
        messageLabel.setHeight(20f);
        messageLabel.setPosition(10f, Gdx.graphics.getHeight() - 50f);

        Label characterLabel = new Label("@", skin, "default");
        characterLabel.setWidth(16);
        characterLabel.setHeight(16);
        stageUi.addActor(messageLabel);

        stage.getCamera().position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        batch = new SpriteBatch();
        character = new Character(characterLabel);
        mapActor = new MapActor();
        stage.addActor(mapActor);
        stage.addActor(characterLabel);
        KeyboardInput keyboardInput = new KeyboardInput(character, mapActor.getMap());
        MouseInput mouseInput = new MouseInput(character, mapActor.getMap(), stage.getCamera(), messageLabel);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(keyboardInput);
        multiplexer.addProcessor(mouseInput);
        multiplexer.addProcessor(stageUi);
        Gdx.input.setInputProcessor(multiplexer);
        debugRenderer = new ShapeRenderer();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getCamera().position.set(character.getPosition().x * 32, character.getPosition().y * 32, 0);
        stage.getCamera().update();
        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        stage.draw();
        stageUi.draw();

        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            drawDebug();
//            debugCharacter();
        }
        batch.end();

    }

    private void debugCharacter() {
        Gdx.app.debug("Character", character.toString());
    }

    private void drawDebug() {
        debugRenderer.setProjectionMatrix(stage.getCamera().combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        mapActor.getMap().getTiles().stream().forEach(t -> {
            Rectangle rectangle = t.getRectangle();
            debugRenderer.setColor(Color.RED);
            debugRenderer.rect(rectangle.getX() * Tile.SIZE, rectangle.getY() * Tile.SIZE, Tile.SIZE, Tile.SIZE);
        });
        debugRenderer.end();
    }
}
