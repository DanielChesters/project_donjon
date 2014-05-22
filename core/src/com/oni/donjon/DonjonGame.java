package com.oni.donjon;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.oni.donjon.entity.Character;
import com.oni.donjon.input.KeyboardInput;

public class DonjonGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    BitmapFont font;
    Character character;
    KeyboardInput keyboardInput;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        character = new Character(font, batch);
        keyboardInput = new KeyboardInput(character);
        Gdx.input.setInputProcessor(keyboardInput);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
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
