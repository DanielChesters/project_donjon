package com.oni.donjon.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.oni.donjon.Resources;
import com.oni.donjon.action.Action;
import com.oni.donjon.actor.MapActor;
import com.oni.donjon.data.GameData;
import com.oni.donjon.entity.Player;
import com.oni.donjon.input.KeyboardInput;
import com.oni.donjon.input.MouseInput;
import com.oni.donjon.map.Map;
import com.oni.donjon.map.Tile;
import com.oni.donjon.stage.GameStage;
import com.oni.donjon.stage.UIStage;

/**
 * @author Daniel Chesters (on 25/05/14).
 */
public class GameScreen extends ScreenAdapter {
    private GameData data;
    private UIStage uiStage;
    private GameStage gameStage;

    public GameScreen() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        createUi(skin);
        createGameScene(skin);
        createData();
        createInput();
    }

    private void createUi(Skin skin) {
        uiStage = new UIStage();

        final Label messageLabel = new Label("", skin, "default");
        messageLabel.setWidth(100);
        messageLabel.setHeight(20);
        messageLabel.setPosition(10, Gdx.graphics.getHeight() - 50);

        final List<Action> actionList = new List<>(skin);
        actionList.setItems(Action.values());
        actionList.getSelection().setRequired(false);
        actionList.getSelection().setMultiple(false);

        final Window actionWindow = new Window(Resources.BUNDLE.get("window.action.title"), skin);
        actionWindow.setPosition(20, Gdx.graphics.getHeight() / 2);
        actionWindow.setHeight(50);
        actionWindow.setWidth(200);
        actionWindow.add(actionList);
        actionWindow.pack();

        uiStage.setMessageLabel(messageLabel);
        uiStage.setActionList(actionList);

        Stage stage = uiStage.getStage();
        stage.addActor(messageLabel);
        stage.addActor(actionWindow);
    }

    private void createGameScene(Skin skin) {
        gameStage = new GameStage();
        MapActor mapActor = new MapActor();

        Label playerLabel = new Label("@", skin, "default");
        playerLabel.setWidth(16);
        playerLabel.setHeight(16);

        gameStage.setMapActor(mapActor);
        gameStage.setPlayerLabel(playerLabel);

        Stage stage = gameStage.getStage();
        stage.getCamera().position
            .set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        stage.addActor(mapActor);
        stage.addActor(playerLabel);

    }

    private void createData() {
        data = new GameData();
        Map map = new Map();
        Tile startTile = map.getStartTile();
        Vector2 startPosition =
            new Vector2(startTile.getRectangle().getX(), startTile.getRectangle().getY());
        Player player = new Player(startPosition);

        data.setMap(map);
        data.setPlayer(player);
        map.setPlayer(player);

        gameStage.setData(data);
        gameStage.getMapActor().setData(data);

        gameStage.updatePlayer();
        map.updateVisibility();
    }

    private void createInput() {
        KeyboardInput keyboardInput = new KeyboardInput();
        keyboardInput.setData(data);
        keyboardInput.setGameStage(gameStage);

        MouseInput mouseInput = new MouseInput();
        mouseInput.setData(data);
        mouseInput.setGameStage(gameStage);
        mouseInput.setUiStage(uiStage);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage.getStage());
        multiplexer.addProcessor(keyboardInput);
        multiplexer.addProcessor(mouseInput);

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Stage stageGame = gameStage.getStage();
        Player player = data.getPlayer();
        stageGame.getCamera().position
            .set(player.getPosition().x * Tile.SIZE, player.getPosition().y * Tile.SIZE, 0);
        stageGame.getCamera().update();
        stageGame.draw();
        uiStage.getStage().draw();
    }
}
