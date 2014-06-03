package com.oni.donjon.entity;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Daniel Chesters (on 20/05/14).
 */
public class Player {
    private Vector2 position;

    public Player(Vector2 startPosition) {
        position = startPosition;
    }

    public Player() {
        position = new Vector2();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void move(float deltaX, float deltaY) {
        if (deltaX != 0) {
            addX(deltaX);
        }
        if (deltaY != 0) {
            addY(deltaY);
        }
    }

    private void addX(float x) {
        position.x += x;
        if (position.x > 20) {
            position.x = 0;
        } else if (position.x < 0) {
            position.x = 20;
        }
    }

    private void addY(float y) {
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

        Player character = (Player) o;

        return position.equals(character.position);

    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }
}
