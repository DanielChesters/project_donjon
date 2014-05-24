package com.oni.donjon.output;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Chesters (on 24/05/14).
 */
public class TextOutput {

    private BitmapFont font;
    private SpriteBatch batch;
    private List<String> messages;

    public TextOutput(BitmapFont font, SpriteBatch batch) {
        this.font = font;
        this.batch = batch;
        this.messages = new ArrayList<>();
    }

    public List<String> getMessages() {
        return messages;
    }

    public void drawMessages() {
        float x = Gdx.graphics.getWidth() - 50;
        float y = Gdx.graphics.getHeight() - 50;
        for (int i = 0; i < messages.size(); i++) {
            Gdx.app.debug("TextOutput", String.format("Message : %s (%f,%f)%n", messages.get(i), x, y));
            font.draw(batch, messages.get(i), x, y - i * 16);
        }
    }

}
