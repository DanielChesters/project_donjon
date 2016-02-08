package com.oni.donjon.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
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
import com.oni.donjon.component.DirectionComponent;
import com.oni.donjon.component.PositionComponent;
import com.oni.donjon.data.GameData;
import com.oni.donjon.data.GameSave;
import com.oni.donjon.input.KeyboardInput;
import com.oni.donjon.input.MouseInput;
import com.oni.donjon.map.Map;
import com.oni.donjon.map.Tile;
import com.oni.donjon.stage.DebugStage;
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
    private DebugStage debugStage;
    private InputMultiplexer gameInput;
    private GameState state;

    private Engine engine;
    private MovementSystem movementSystem;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    public static final short NOTHING_BIT = 0;
    public static final short WALL_BIT = 1;
    public static final short PLAYER_BIT = 1 << 1;


    public enum GameState {
        RUNNING, PAUSE
    }

    public GameScreen(DonjonGame game) {
        createGame(game, () -> createData());
    }

    public GameScreen(DonjonGame game, String saveFile) {
        createGame(game, () -> loadData(saveFile));
    }

    private void createGame(DonjonGame game, Runnable runnable) {
        this.game = game;
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        world = new World(Vector2.Zero, true);
        debugRenderer = new Box2DDebugRenderer();
        engine = new Engine();
        movementSystem = new MovementSystem();
        engine.addSystem(movementSystem);
        createUi(skin);
        createGameStage(skin);
        runnable.run();
        createInput();
        state = GameState.RUNNING;
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
        GameSave save = json.fromJson(GameSave.class, file);

        data = GameData.INSTANCE;
        data.setMap(save.getMap());
        data.setWorld(world);

        Entity player = new Entity();
        player.add(new PositionComponent(save.getPlayerPosition(),
            createPlayerBody(save.getPlayerPosition())));
        player.add(new DirectionComponent());
        data.setPlayer(player);

        engine = new Engine();
        movementSystem = new MovementSystem();
        engine.addSystem(movementSystem);
        engine.addEntity(player);

        data.getMap().setPlayer(data.getPlayer());
        movementSystem.map = save.getMap();
        uiStage.getSaveWindow().setData(data);
        gameStage.setData(data);
        gameStage.getMapActor().setData(data);
        gameStage.updatePlayer();
    }

    private void createData() {
        data = GameData.INSTANCE;
        Map map = new Map("map/map.json", world);
        Tile startTile = map.getStartTile();
        Vector2 startPosition =
            new Vector2(startTile.getRectangle().getX(), startTile.getRectangle().getY());
        Entity player = new Entity();
        player.add(new PositionComponent(startPosition, createPlayerBody(startPosition)));
        player.add(new DirectionComponent());

        data.setMap(map);
        data.setPlayer(player);
        data.setWorld(world);
        map.setPlayer(player);
        movementSystem.map = map;
        engine.addEntity(player);

        uiStage.getSaveWindow().setData(data);

        gameStage.setData(data);
        gameStage.getMapActor().setData(data);

        gameStage.updatePlayer();
        map.updateVisibility();
    }

    private Body createPlayerBody(Vector2 playerPosition) {
        Gdx.app.debug("createPlayerBody", playerPosition.toString());
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(playerPosition).add(0.25f, 0.25f).scl(Tile.SIZE);

        Body body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(10f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = PLAYER_BIT;
        fixtureDef.filter.maskBits = WALL_BIT;
        body.createFixture(fixtureDef);
        circleShape.dispose();
        return body;
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

    @Override public void render(float delta) {
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
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            debugStage.drawDebug();
            debugRenderer.render(world, gameStage.getStage().getCamera().combined);
        }
        uiStage.getStage().draw();
    }

    private void update(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Stage stageGame = gameStage.getStage();
        engine.update(delta);
        gameStage.updatePlayer();
        stageGame.getCamera().position
            .set(data.getPlayerPosition().x * Tile.SIZE, data.getPlayerPosition().y * Tile.SIZE, 0);
        stageGame.getCamera().update();
        stageGame.draw();
        world.step(1 / 60f, 6, 2);
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            debugStage.drawDebug();
            debugRenderer.render(world, gameStage.getStage().getCamera().combined);
        }
        uiStage.getStage().draw();
    }

    public void setState(GameState state) {
        this.state = state;
    }
}
