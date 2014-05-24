package com.oni.donjon;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.oni.donjon.entity.Character;
import com.oni.donjon.input.KeyboardInput;
import com.oni.donjon.input.MouseInput;
import com.oni.donjon.map.Map;
import com.oni.donjon.map.Tile;
import com.oni.donjon.map.TileType;

import java.util.stream.IntStream;

public class DonjonGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Character character;
    private OrthographicCamera cam;
    private Map map;
    private ShapeRenderer debugRenderer;
    private Stage stage;

    @Override
    public void create() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        stage = new Stage();
        final Label label = new Label("", skin, "default-font", Color.BLUE);
        label.setWidth(100f);
        label.setHeight(20f);
        label.setPosition(10f, Gdx.graphics.getHeight() - 50f);
        stage.addActor(label);
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        cam.update();
        batch = new SpriteBatch();
        character = new Character(skin.getFont("default-font"), batch);
        map = new Map();
        IntStream.rangeClosed(0, 20).forEach(x -> IntStream.rangeClosed(0, 20).forEach(y -> {
            if ((x > 1 && x < 19) && (y > 1 && y < 19)) {
                map.getTiles().add(new Tile(x, y, TileType.GROUND));
            } else {
                map.getTiles().add(new Tile(x, y, TileType.WALL));
            }
        }));
        KeyboardInput keyboardInput = new KeyboardInput(character, map);
        MouseInput mouseInput = new MouseInput(character, map, cam, label);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(keyboardInput);
        multiplexer.addProcessor(mouseInput);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
        debugRenderer = new ShapeRenderer();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.position.set(character.getPosition().x * 32, character.getPosition().y * 32, 0);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        map.getTiles().stream().forEach(t -> batch.draw(t.getType().getTexture(), t.getRectangle().getX() * Tile.SIZE, t.getRectangle().getY() * Tile.SIZE));
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            drawDebug();
//            debugCharacter();
        }
        stage.draw();
        character.drawCharacter();

        batch.end();

    }

    private void debugCharacter() {
        Gdx.app.debug("Character", character.toString());
    }

    private void drawDebug() {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        map.getTiles().stream().forEach(t -> {
            Rectangle rectangle = t.getRectangle();
            debugRenderer.setColor(Color.RED);
            debugRenderer.rect(rectangle.getX() * Tile.SIZE, rectangle.getY() * Tile.SIZE, Tile.SIZE, Tile.SIZE);
        });
        debugRenderer.end();
    }
}
