package com.oni.donjon.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.oni.donjon.map.Map;
import com.oni.donjon.map.Tile;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author Daniel Chesters (on 20/05/14).
 */
public class Player {
    public static enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }

    private Vector2 position;
    private Direction direction;

    public Player(Vector2 startPosition) {
        position = startPosition;
        direction = Direction.NONE;
    }

    public Player() {
        position = new Vector2();
        direction = Direction.NONE;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
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

    public void updateMove(Map map) {
        int numCase = getCaseToGo();
        switch (direction) {
            case UP:
                goUp(map, numCase);
                break;
            case DOWN:
                goDown(map, numCase);
                break;
            case RIGHT:
                goRight(map, numCase);
                break;
            case LEFT:
                goLeft(map, numCase);
                break;
            default:
                break;
        }
    }

    private int getCaseToGo() {
        int numberCase;
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) || Gdx.input
            .isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            numberCase = 10;
        } else {
            numberCase = 1;
        }
        return numberCase;
    }

    private void goDown(Map map, int numberCase) {
        movePlayer(map, numberCase, 0, -0.5f);
    }

    private void goUp(Map map, int numberCase) {
        movePlayer(map, numberCase, 0, 0.5f);
    }

    private void goLeft(Map map, int numberCase) {
        movePlayer(map, numberCase, -0.5f, 0);
    }

    private void goRight(Map map, int numberCase) {
        movePlayer(map, numberCase, 0.5f, 0);
    }

    private void movePlayer(Map map, int numberCase, float deltaX, float deltaY) {
        IntStream.range(0, numberCase).forEach(i -> {
            Optional<Tile> tileRight =
                map.getTile((int) (position.x + deltaX),
                    (int) (position.y + deltaY));
            if (tileRight.isPresent() && !tileRight.get().getType().isBlock()) {
                move(deltaX, deltaY);
            }
            map.updateVisibility();
        });
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
