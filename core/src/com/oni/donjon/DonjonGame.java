package com.oni.donjon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

public class DonjonGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    BitmapFont font;
    int posX;
    int posY;

    @Override
    public void create () {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        posX = Gdx.graphics.getWidth() / 2;
        posY = Gdx.graphics.getHeight() / 2;
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        font.draw(batch, "@", posX, posY);
        batch.end();
    }
}
