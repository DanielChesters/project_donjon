package com.oni.donjon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DonjonGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    BitmapFont font;
    int posX;
    int posY;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        posX = Gdx.graphics.getWidth() / 2;
        posY = Gdx.graphics.getHeight() / 2;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
                posY += 10;
            } else {
                posY += 1;
            }
            if (posY > Gdx.graphics.getHeight()) {
                posY = 0;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
                posY -= 10;
            } else {
                posY -= 1;
            }
            if (posY < 0) {
                posY = Gdx.graphics.getHeight();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
                posX -= 10;
            } else {
                posX -= 1;
            }
            if (posX < 0) {
                posX = Gdx.graphics.getWidth();
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
                posX += 10;
            } else {
                posX += 1;
            }
            if (posX > Gdx.graphics.getWidth()) {
                posX = 0;
            }
        }
        batch.begin();
        batch.draw(img, 0, 0);
        font.draw(batch, "@", posX, posY);
        batch.end();
    }
}
