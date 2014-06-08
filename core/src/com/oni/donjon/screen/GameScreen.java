package com.oni.donjon.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Json;
import com.oni.donjon.DonjonGame;
import com.oni.donjon.Resources;
import com.oni.donjon.action.Actions;
import com.oni.donjon.actor.MapActor;
import com.oni.donjon.actor.MenuGameWindow;
import com.oni.donjon.actor.SaveWindow;
import com.oni.donjon.data.GameData;
import com.oni.donjon.entity.Player;
import com.oni.donjon.input.KeyboardInput;
import com.oni.donjon.input.MouseInput;
import com.oni.donjon.map.Map;
import com.oni.donjon.map.Tile;
import com.oni.donjon.stage.DebugStage;
import com.oni.donjon.stage.GameStage;
import com.oni.donjon.stage.UIStage;

/**
 * @author Daniel Chesters (on 25/05/14).
 */
public class GameScreen extends ScreenAdapter {
    private DonjonGame game;
    private GameData data;
    private UIStage uiStage;
    private GameStage gameStage;
    private DebugStage debugStage;
    private InputMultiplexer gameInput;

    public GameScreen(DonjonGame game) {
        this.game = game;
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        createUi(skin);
        createGameStage(skin);
        createData();
        createInput();
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            createDebugStage();
        }
    }

    public GameScreen(DonjonGame game, String saveFile) {
        this.game = game;
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        createUi(skin);
        createGameStage(skin);
        loadData(saveFile);
        createInput();
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            createDebugStage();
        }
    }

    private void createUi(Skin skin) {
        uiStage = new UIStage();

        final Label messageLabel = createMessageLabel(skin);
        final List<Actions> actionList = createActionList(skin);
        final Window actionWindow = createActionWindow(skin, actionList);
        final SaveWindow saveWindow =
            new SaveWindow(Resources.BUNDLE.get("window.save.title"), skin);
        final MenuGameWindow menuWindow =
            new MenuGameWindow(Resources.BUNDLE.get("game_menu.title"), skin, saveWindow, game);
        final TextButton menuButton = createMenuButton(skin, menuWindow);

        uiStage.setMessageLabel(messageLabel);
        uiStage.setActionList(actionList);
        uiStage.setSaveWindow(saveWindow);

        Stage stage = uiStage.getStage();
        stage.addActor(messageLabel);
        stage.addActor(actionWindow);
        stage.addActor(menuWindow);
        stage.addActor(menuButton);
        stage.addActor(saveWindow);
    }

    private TextButton createMenuButton(Skin skin, Window menuWindow) {
        TextButton menuButton = new TextButton(Resources.BUNDLE.get("game_menu.title"), skin);
        menuButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer,
                int button) {
                menuWindow.setVisible(true);
                return true;
            }
        });
        menuButton.pack();
        menuButton.setPosition(Gdx.graphics.getWidth() - (menuButton.getWidth() + 5),
            Gdx.graphics.getHeight() - (menuButton.getHeight() + 5));
        return menuButton;
    }

    private Window createActionWindow(Skin skin, List<Actions> actionList) {
        final Window actionWindow = new Window(Resources.BUNDLE.get("window.action.title"), skin);
        actionWindow.setPosition(20, Gdx.graphics.getHeight() / 2);
        actionWindow.setHeight(50);
        actionWindow.setWidth(200);
        actionWindow.add(actionList);
        actionWindow.pack();
        return actionWindow;
    }

    private List<Actions> createActionList(Skin skin) {
        final List<Actions> actionList = new List<>(skin);
        actionList.setItems(Actions.values());
        actionList.getSelection().setRequired(false);
        actionList.getSelection().setMultiple(false);
        return actionList;
    }

    private Label createMessageLabel(Skin skin) {
        final Label messageLabel = new Label("", skin, "default");
        messageLabel.setWidth(100);
        messageLabel.setHeight(20);
        messageLabel.setPosition(10, Gdx.graphics.getHeight() - 50);
        return messageLabel;
    }

    private void createGameStage(Skin skin) {
        gameStage = new GameStage();
        MapActor mapActor = new MapActor();

        Label playerLabel = createPlayerLabel(skin);

        gameStage.setMapActor(mapActor);
        gameStage.setPlayerLabel(playerLabel);

        Stage stage = gameStage.getStage();
        stage.getCamera().position
            .set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        stage.addActor(mapActor);
        stage.addActor(playerLabel);

    }

    private Label createPlayerLabel(Skin skin) {
        Label playerLabel = new Label("@", skin, "default");
        playerLabel.setWidth(16);
        playerLabel.setHeight(16);
        return playerLabel;
    }

    private void loadData(String saveFile) {
        Json json = new Json();
        FileHandle file = Gdx.files.external(saveFile);
        data = json.fromJson(GameData.class, null, file);
        data.getMap().setPlayer(data.getPlayer());
        uiStage.getSaveWindow().setData(data);
        gameStage.setData(data);
        gameStage.getMapActor().setData(data);
        gameStage.updatePlayer();
    }

    private void createData() {
        data = new GameData();
        Map map = new Map("map/map.json");
        Tile startTile = map.getStartTile();
        Vector2 startPosition =
            new Vector2(startTile.getRectangle().getX(), startTile.getRectangle().getY());
        Player player = new Player(startPosition);

        data.setMap(map);
        data.setPlayer(player);
        map.setPlayer(player);

        uiStage.getSaveWindow().setData(data);

        gameStage.setData(data);
        gameStage.getMapActor().setData(data);

        gameStage.updatePlayer();
        map.updateVisibility();
    }

    private void createInput() {
        if (gameInput == null) {
            final KeyboardInput keyboardInput = createKeyboardInput();
            final MouseInput mouseInput = createMouseInput();
            gameInput = createInputMultiplexer(keyboardInput, mouseInput);
        }

        Gdx.input.setInputProcessor(gameInput);
    }

    private InputMultiplexer createInputMultiplexer(KeyboardInput keyboardInput,
        MouseInput mouseInput) {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage.getStage());
        multiplexer.addProcessor(keyboardInput);
        multiplexer.addProcessor(mouseInput);
        return multiplexer;
    }

    private MouseInput createMouseInput() {
        MouseInput mouseInput = new MouseInput();
        mouseInput.setData(data);
        mouseInput.setGameStage(gameStage);
        mouseInput.setUiStage(uiStage);
        return mouseInput;
    }

    private KeyboardInput createKeyboardInput() {
        KeyboardInput keyboardInput = new KeyboardInput();
        keyboardInput.setData(data);
        return keyboardInput;
    }

    private void createDebugStage() {
        debugStage = new DebugStage();
        debugStage.setData(data);
        debugStage.setGameStage(gameStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Stage stageGame = gameStage.getStage();
        Player player = data.getPlayer();
        player.updateMove(data.getMap());
        gameStage.updatePlayer();
        stageGame.getCamera().position
            .set(player.getPosition().x * Tile.SIZE, player.getPosition().y * Tile.SIZE, 0);
        stageGame.getCamera().update();
        stageGame.draw();

        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            debugStage.drawDebug();
        }
        uiStage.getStage().draw();
    }
}
