package com.oni.donjon.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Daniel Chesters (on 20/05/14).
 */
public class Character {
    private Vector2 position;
    BitmapFont font;
    SpriteBatch batch;

    public Vector2 getPosition() {
        return position;
    }

    public void addX(int x) {
        position.x += x;
        if (position.x > Gdx.graphics.getWidth()) {
            position.x = 0;
        } else if (position.x < 0) {
            position.x = Gdx.graphics.getWidth();
        }
    }

    public void addY(int y) {
        position.y += y;
        if (position.y > Gdx.graphics.getHeight()) {
            position.y = 0;
        } else if (position.y < 0) {
            position.y = Gdx.graphics.getHeight();
        }
    }

    @Override
    public String toString() {
        return "@{" +
                "x=" + position.x +
                ", y=" + position.y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Character character = (Character) o;

        return position.equals(character.position);

    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

    public Character(BitmapFont font, SpriteBatch batch) {
        this.font = font;
        this.batch = batch;
        position = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    public void drawCharacter() {
        font.draw(batch, "@", position.x, position.y);
    }
}
