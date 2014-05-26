package com.oni.donjon.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.oni.donjon.action.Action;
import com.oni.donjon.actor.MapActor;
import com.oni.donjon.entity.Character;
import com.oni.donjon.input.KeyboardInput;
import com.oni.donjon.input.MouseInput;
import com.oni.donjon.map.Tile;
import com.oni.donjon.map.TileType;

/**
 * @author Daniel Chesters (on 25/05/14).
 */
public class GameScreen extends ScreenAdapter {
    private Character character;
    private MapActor mapActor;
    private ShapeRenderer debugRenderer;
    private Stage stageUi;
    private Stage stage;
    private List<Action> actionList;

    public GameScreen() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        final Label messageLabel = createUi(skin);

        Window actionWindow = new Window("Action", skin);
        actionWindow.setPosition(20, Gdx.graphics.getHeight() / 2);
        actionWindow.setHeight(50);
        actionWindow.setWidth(200);
        actionList = new List<>(skin);
        actionList.setItems(Action.values());
        actionList.getSelection().setRequired(false);
        actionList.getSelection().setMultiple(false);
        actionWindow.add(actionList);
        actionWindow.pack();

        stageUi.addActor(actionWindow);
        mapActor = new MapActor();
        Label characterLabel = createGameScene(skin);
        Tile startTile = mapActor.getMap().getTiles().stream().filter(t -> t.getType().equals(TileType.STAIR_UP)).findFirst().get();
        Vector2 startPosition = new Vector2(startTile.getRectangle().getX(), startTile.getRectangle().getY());
        character = new Character(characterLabel, startPosition);
        mapActor.getMap().setCharacter(character);
        mapActor.getMap().updateVisibility();
        createInput(messageLabel);

        debugRenderer = new ShapeRenderer();
    }

    private void createInput(Label messageLabel) {
        KeyboardInput keyboardInput = new KeyboardInput(character, mapActor.getMap());
        MouseInput mouseInput = new MouseInput();
        mouseInput.setCharacter(character);
        mouseInput.setMap(mapActor.getMap());
        mouseInput.setCamera(stage.getCamera());
        mouseInput.setMessageLabel(messageLabel);
        mouseInput.setActionList(actionList);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stageUi);
        multiplexer.addProcessor(keyboardInput);
        multiplexer.addProcessor(mouseInput);

        Gdx.input.setInputProcessor(multiplexer);
    }

    private Label createGameScene(Skin skin) {
        stage = new Stage();
        Label characterLabel = new Label("@", skin, "default");
        characterLabel.setWidth(16);
        characterLabel.setHeight(16);
        stage.getCamera().position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        stage.addActor(mapActor);
        stage.addActor(characterLabel);
        return characterLabel;
    }

    private Label createUi(Skin skin) {
        stageUi = new Stage();
        final Label messageLabel = new Label("", skin, "default");
        messageLabel.setWidth(100f);
        messageLabel.setHeight(20f);
        messageLabel.setPosition(10f, Gdx.graphics.getHeight() - 50f);
        stageUi.addActor(messageLabel);
        return messageLabel;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getCamera().position.set(character.getPosition().x * Tile.SIZE, character.getPosition().y * Tile.SIZE, 0);
        stage.getCamera().update();
        stage.draw();
        stageUi.draw();

        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            drawDebug();
        }
    }

    private void drawDebug() {
        debugRenderer.setProjectionMatrix(stage.getCamera().combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        mapActor.getMap().getTiles().stream().forEach(t -> {
            Rectangle rectangle = t.getRectangle();
            if (t.isVisible()) {
                debugRenderer.setColor(Color.RED);
            } else {
                debugRenderer.setColor(Color.BLUE);
            }
            debugRenderer.rect(rectangle.getX() * Tile.SIZE, rectangle.getY() * Tile.SIZE, Tile.SIZE, Tile.SIZE);
        });
        debugRenderer.end();
    }
}
