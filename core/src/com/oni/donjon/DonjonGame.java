package com.oni.donjon;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.oni.donjon.entity.Character;
import com.oni.donjon.input.KeyboardInput;

import java.util.stream.IntStream;

public class DonjonGame extends ApplicationAdapter {
    SpriteBatch batch;
    BitmapFont font;
    Character character;
    KeyboardInput keyboardInput;
    OrthographicCamera cam;
    Texture wall;

    @Override
    public void create() {
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        cam.update();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        character = new Character(font, batch);
        keyboardInput = new KeyboardInput(character);
        Gdx.input.setInputProcessor(keyboardInput);
        wall = new Texture("textures/wall.png");
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.position.set(character.getPosition().x * 32, character.getPosition().y * 32, 0);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        IntStream.rangeClosed(0, 20).filter(x -> x % 2 == 0).forEach(x -> {
            IntStream.rangeClosed(0, 20).filter(y -> y % 2 == 0).forEach(y -> {
                batch.draw(wall, x * wall.getWidth(), y * wall.getHeight());
            });
        });
        character.drawCharacter();
        batch.end();
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            debugCharacter();
        }
    }

    private void debugCharacter() {
        Gdx.app.debug("Character", character.toString());
    }
}
