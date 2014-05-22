package com.oni.donjon.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Daniel Chesters (on 20/05/14).
 */
public class Character {
    private int x;
    private int y;
    BitmapFont font;
    SpriteBatch batch;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void addX(int x) {
        this.x += x;
        if (this.x > Gdx.graphics.getWidth()) {
            this.x = 0;
        } else if (this.x < 0) {
            this.x = Gdx.graphics.getWidth();
        }
    }

    public void addY(int y) {
        this.y += y;
        if (this.y > Gdx.graphics.getHeight()) {
            this.y = 0;
        } else if (this.y < 0) {
            this.y = Gdx.graphics.getHeight();
        }
    }

    @Override
    public String toString() {
        return "@{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Character character = (Character) o;

        if (x != character.x) return false;
        return y == character.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public Character(BitmapFont font, SpriteBatch batch) {
        this.font = font;
        this.batch = batch;
        this.x = Gdx.graphics.getWidth() / 2;
        this.y = Gdx.graphics.getHeight() / 2;
    }

    public void drawCharacter() {
        font.draw(batch, "@", x, y);
    }
}
