package com.oni.donjon.screen;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.oni.donjon.sound.Sounds;
import com.oni.donjon.stage.DebugStage;
import com.oni.donjon.stage.GameStage;
import com.oni.donjon.stage.UIStage;
import com.oni.donjon.system.MovementSystem;

/**
 * @author Daniel Chesters (on 25/05/14).
 */
public class GameScreen extends ScreenAdapter {
    private DonjonGame game;
    private UIStage uiStage;
    private GameStage gameStage;
    private DebugStage debugStage;
    private InputMultiplexer gameInput;
    private GameState state;

    private Engine engine;
    private MovementSystem movementSystem;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private RayHandler rayHandler;

    public static final short NOTHING_BIT = 0;
    public static final short WALL_BIT = 1;
    public static final short PLAYER_BIT = 1 << 1;
    public static final short LIGHT_BIT = 1 << 5;


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
        world.dispose();
        rayHandler.dispose();
        debugRenderer.dispose();
        Sounds.disposeAll();
    }

    private void createGame(DonjonGame game, Runnable runnable) {
        this.game = game;
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        world = new World(Vector2.Zero, true);
        debugRenderer = new Box2DDebugRenderer();
        engine = new Engine();
        movementSystem = new MovementSystem();
        engine.addSystem(movementSystem);
        rayHandler = new RayHandler(world);
        createUi(skin);
        createGameStage(skin);
        GameData.INSTANCE.setWorld(world);
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

        GameData.INSTANCE.setMap(new Map(save));

        Entity player = new Entity();
        player.add(new PositionComponent(save.getPlayerPosition(),
            createPlayerBody(save.getPlayerPosition())));
        player.add(new DirectionComponent());
        GameData.INSTANCE.setPlayer(player);

        engine.addEntity(player);

        GameData.INSTANCE.getMap().setPlayer(GameData.INSTANCE.getPlayer());
        movementSystem.map = GameData.INSTANCE.getMap();
        gameStage.updatePlayer();
    }

    private void createData() {
        Map map = new Map();
        Tile startTile = map.getStartTile();
        Vector2 startPosition =
            new Vector2(startTile.getRectangle().getX(), startTile.getRectangle().getY());
        Entity player = new Entity();
        player.add(new PositionComponent(startPosition, createPlayerBody(startPosition)));
        player.add(new DirectionComponent());

        GameData.INSTANCE.setMap(map);
        GameData.INSTANCE.setPlayer(player);

        map.setPlayer(player);
        movementSystem.map = map;
        engine.addEntity(player);

        gameStage.updatePlayer();
        map.updateVisibility();
    }

    @Override public void resize(int width, int height) {
        gameStage.getStage().getViewport().update(width, height);
        uiStage.getStage().getViewport().update(width, height);
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

        PointLight pointLight =
            new PointLight(rayHandler, 50, Color.FIREBRICK, 100, body.getPosition().x,
                body.getPosition().y);
        pointLight.setContactFilter(LIGHT_BIT, NOTHING_BIT, WALL_BIT);
        pointLight.setSoft(true);
        pointLight.setSoftnessLength(2);
        pointLight.attachToBody(body);

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
        mouseInput.setGameStage(gameStage);
        mouseInput.setUiStage(uiStage);
        return mouseInput;
    }

    private KeyboardInput createKeyboardInput() {
        KeyboardInput keyboardInput = new KeyboardInput();
        return keyboardInput;
    }

    private void createDebugStage() {
        debugStage = new DebugStage();
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
            .set(GameData.INSTANCE.getPlayerPosition().x * Tile.SIZE,
                GameData.INSTANCE.getPlayerPosition().y * Tile.SIZE, 0);
        stageGame.getCamera().update();
        stageGame.draw();
//        rayHandler.setCombinedMatrix((OrthographicCamera) gameStage.getStage().getCamera());
//        rayHandler.updateAndRender();
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
