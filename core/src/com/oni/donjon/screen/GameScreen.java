package com.oni.donjon.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Json;
import com.oni.donjon.DonjonGame;
import com.oni.donjon.Resources;
import com.oni.donjon.action.Actions;
import com.oni.donjon.actor.MenuGameWindow;
import com.oni.donjon.actor.SaveWindow;
import com.oni.donjon.component.DirectionComponent;
import com.oni.donjon.component.PositionComponent;
import com.oni.donjon.data.GameData;
import com.oni.donjon.data.GameSave;
import com.oni.donjon.input.KeyboardInput;
import com.oni.donjon.sound.Sounds;
import com.oni.donjon.stage.GameStage;
import com.oni.donjon.stage.UIStage;
import com.oni.donjon.system.MovementSystem;

/**
 * @author Daniel Chesters (on 25/05/14).
 */
public class GameScreen extends ScreenAdapter {
    private DonjonGame game;
    private GameData data;
    private UIStage uiStage;
    private GameStage gameStage;
    private InputMultiplexer gameInput;
    private GameState state;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;

    private Engine engine;
    private MovementSystem movementSystem;


    public enum GameState {
        RUNNING, PAUSE
    }

    public GameScreen(DonjonGame game) {
        createGame(game, () -> createData());
    }

    public GameScreen(DonjonGame game, String saveFile) {
        createGame(game, () -> loadData(saveFile));
    }

    @Override
    public void dispose() {
        super.dispose();
        Sounds.disposeAll();
    }

    private void createGame(DonjonGame game, Runnable runnable) {
        this.game = game;
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(10, 10 * (h / w));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        tiledMap = new TmxMapLoader().load("map/map0.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 16f);
        engine = new Engine();
        movementSystem = new MovementSystem();
        engine.addSystem(movementSystem);
        createUi(skin);
        createGameStage(skin);
        runnable.run();
        createInput();
        state = GameState.RUNNING;
    }

    private void createUi(Skin skin) {
        uiStage = new UIStage();

        final Label messageLabel = createMessageLabel(skin);
        final List<Actions> actionList = createActionList(skin);
        final Window actionWindow = createActionWindow(skin, actionList);
        final SaveWindow saveWindow =
            new SaveWindow(Resources.BUNDLE.get("window.save.title"), skin);
        final MenuGameWindow menuWindow =
            new MenuGameWindow(skin, saveWindow, game, this);
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
                state = GameState.PAUSE;
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

        Label playerLabel = createPlayerLabel(skin);

        gameStage.setPlayerLabel(playerLabel);

        Stage stage = gameStage.getStage();
        stage.getCamera().position
            .set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        stage.addActor(playerLabel);

    }

    private Label createPlayerLabel(Skin skin) {
        Label playerLabel = new Label("@", skin, "default");
        playerLabel.setWidth(8);
        playerLabel.setHeight(8);
        return playerLabel;
    }

    private void loadData(String saveFile) {
        Json json = new Json();
        FileHandle file = Gdx.files.external(saveFile);
        GameSave save = json.fromJson(GameSave.class, file);

        data = new GameData();

        Entity player = new Entity();
        player.add(new PositionComponent(save.getPlayerPosition()));
        player.add(new DirectionComponent());
        data.setPlayer(player);

        engine.addEntity(player);

        uiStage.getSaveWindow().setData(data);
        gameStage.setData(data);
        gameStage.updatePlayer();
    }

    private void createData() {
        data = new GameData();
        MapLayer layer = tiledMap.getLayers().get("Player");
        Vector2 startPosition = null;
        for (MapObject mapObject : layer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            startPosition = rectangle.getCenter(Vector2.Zero);
            startPosition.scl(1 / 16f);
        }
        Entity player = new Entity();
        player.add(new PositionComponent(startPosition));
        player.add(new DirectionComponent());

        data.setPlayer(player);
        engine.addEntity(player);

        uiStage.getSaveWindow().setData(data);

        gameStage.setData(data);

        gameStage.updatePlayer();
    }

    private void createInput() {
        if (gameInput == null) {
            final KeyboardInput keyboardInput = createKeyboardInput();
            gameInput = createInputMultiplexer(keyboardInput);
        }

        Gdx.input.setInputProcessor(gameInput);
    }

    private InputMultiplexer createInputMultiplexer(KeyboardInput keyboardInput) {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage.getStage());
        multiplexer.addProcessor(keyboardInput);
        return multiplexer;
    }


    private KeyboardInput createKeyboardInput() {
        KeyboardInput keyboardInput = new KeyboardInput();
        keyboardInput.setData(data);
        return keyboardInput;
    }

    @Override
    public void render(float delta) {
        switch (state) {
            case RUNNING:
                update(delta);
                break;
            case PAUSE:
                updatePause();
                break;
            default:
                break;
        }
    }

    private void updatePause() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStage.getStage().draw();
        uiStage.getStage().draw();
    }

    private void update(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.set(data.getPlayerPosition(), 0);
        camera.update();
        Stage stageGame = gameStage.getStage();
        engine.update(delta);
        gameStage.updatePlayer();
        stageGame.getCamera().position
            .set(data.getPlayerPosition().x * 16f, data.getPlayerPosition().y * 16f, 0);
        stageGame.getCamera().update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        uiStage.getStage().draw();
        stageGame.draw();
    }

    public void setState(GameState state) {
        this.state = state;
    }
}
