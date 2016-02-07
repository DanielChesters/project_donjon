package com.oni.donjon.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.oni.donjon.component.DirectionComponent;
import com.oni.donjon.component.PositionComponent;
import com.oni.donjon.map.Map;

import java.util.stream.IntStream;

/**
 * @author Daniel Chesters (on 06/02/16).
 */
public class MovementSystem extends IteratingSystem {
    public Map map;

    private ComponentMapper<DirectionComponent> dm =
        ComponentMapper.getFor(DirectionComponent.class);
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    public MovementSystem() {
        super(Family.all(PositionComponent.class, DirectionComponent.class).get());
    }

    @Override protected void processEntity(Entity player, float deltaTime) {
        updateMove(player);
    }

    private void move(Entity player, float deltaX, float deltaY) {
        PositionComponent positionComponent = pm.get(player);
        if (deltaX != 0) {
            addX(deltaX, positionComponent.position);
        }
        if (deltaY != 0) {
            addY(deltaY, positionComponent.position);
        }
    }

    private void addX(float x, Vector2 position) {
        position.x += x;
        if (position.x > 20) {
            position.x = 0;
        } else if (position.x < 0) {
            position.x = 20;
        }
    }

    private void addY(float y, Vector2 position) {
        position.y += y;
        if (position.y > 20) {
            position.y = 0;
        } else if (position.y < 0) {
            position.y = 20;
        }
    }

    private void updateMove(Entity player) {
        int numCase = getCaseToGo();
        DirectionComponent directionComponent = dm.get(player);
        switch (directionComponent.direction) {
            case UP:
                goUp(player, numCase);
                break;
            case DOWN:
                goDown(player, numCase);
                break;
            case RIGHT:
                goRight(player, numCase);
                break;
            case LEFT:
                goLeft(player, numCase);
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

    private void goDown(Entity player, int numberCase) {
        movePlayer(player, numberCase, 0, -0.5f);
    }

    private void goUp(Entity player, int numberCase) {
        movePlayer(player, numberCase, 0, 0.5f);
    }

    private void goLeft(Entity player, int numberCase) {
        movePlayer(player, numberCase, -0.5f, 0);
    }

    private void goRight(Entity player, int numberCase) {
        movePlayer(player, numberCase, 0.5f, 0);
    }

    private void movePlayer(Entity player, int numberCase, float deltaX, float deltaY) {
        IntStream.range(0, numberCase).forEach(i -> {
            move(player, deltaX, deltaY);
            map.updateVisibility();
        });
    }


}
