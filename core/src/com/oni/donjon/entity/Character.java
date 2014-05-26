package com.oni.donjon.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.oni.donjon.map.Tile;

/**
 * @author Daniel Chesters (on 20/05/14).
 */
public class Character {
    private Vector2 position;
    private Label label;

    public Vector2 getPosition() {
        return position;
    }

    public void addX(float x) {
        position.x += x;
        if (position.x > 20) {
            position.x = 0;
        } else if (position.x < 0) {
            position.x = 20;
        }
    }

    public void addY(float y) {
        position.y += y;
        if (position.y > 20) {
            position.y = 0;
        } else if (position.y < 0) {
            position.y = 20;
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Character character = (Character) o;

        return position.equals(character.position);

    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

    public Character(Label label, Vector2 startPosition) {
        this.label = label;
        position = startPosition;
        updateCharacter();
    }

    public void updateCharacter() {
        label.setPosition(position.x * Tile.SIZE, position.y * Tile.SIZE);
    }
}
