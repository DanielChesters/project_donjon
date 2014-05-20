package com.oni.donjon;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.oni.donjon.entity.Character;

public class DonjonGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    BitmapFont font;
    Character character;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        character = new Character(font, batch);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        int diffX = 0;
        int diffY = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
                diffY = 10;
            } else {
                diffY = 1;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
                diffY = -10;
            } else {
                diffY = -1;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
                diffX = -10;
            } else {
                diffX = -1;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
                diffX = 10;
            } else {
                diffX = 1;
            }
        }
        character.addX(diffX);
        character.addY(diffY);
        batch.begin();
        batch.draw(img, 0, 0);
        character.drawCharacter();
        batch.end();
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            debug();
        }
    }

    private void debug() {
        Gdx.app.debug("Character", character.toString());
    }
}
