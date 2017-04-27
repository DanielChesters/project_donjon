package com.oni.donjon.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.oni.donjon.component.DirectionComponent;
import com.oni.donjon.component.LightComponent;
import com.oni.donjon.component.PositionComponent;
import com.oni.donjon.data.GameData;
import com.oni.donjon.map.Tile;
import com.oni.donjon.screen.GameScreen;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author Daniel Chesters (on 06/02/16).
 */
public class MovementSystem extends IteratingSystem {
    boolean canMove;

    private final RayCastCallback rayCastCallback = (fixture, point, normal, fraction) -> {
        if (fixture.getFilterData().categoryBits == GameScreen.WALL_BIT) {
            canMove = false;
        }
        return 0;
    };

    private ComponentMapper<DirectionComponent> dm =
        ComponentMapper.getFor(DirectionComponent.class);
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<LightComponent> lm = ComponentMapper.getFor(LightComponent.class);

    public MovementSystem() {
        super(Family.all(PositionComponent.class, DirectionComponent.class, LightComponent.class)
            .get());
    }

    @Override protected void processEntity(Entity player, float deltaTime) {
        updateMove(player);
    }

    private void move(Entity player, float deltaX, float deltaY) {
        PositionComponent positionComponent = pm.get(player);
        if (BigDecimal.valueOf(deltaX).compareTo(BigDecimal.ZERO) != 0) {
            addX(deltaX, positionComponent.getPosition());
        }
        if (BigDecimal.valueOf(deltaY).compareTo(BigDecimal.ZERO) != 0) {
            addY(deltaY, positionComponent.getPosition());
        }
    }

    private void addX(float x, Vector2 position) {
        position.x += x;
    }

    private void addY(float y, Vector2 position) {
        position.y += y;
    }

    private void updateMove(Entity player) {
        int numCase = getCaseToGo();
        DirectionComponent directionComponent = dm.get(player);
        switch (directionComponent.getDirection()) {
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
        movePlayer(player, numberCase, 0, -0.5f, 270);
    }

    private void goUp(Entity player, int numberCase) {
        movePlayer(player, numberCase, 0, 0.5f, 90);
    }

    private void goLeft(Entity player, int numberCase) {
        movePlayer(player, numberCase, -0.5f, 0, 180);
    }

    private void goRight(Entity player, int numberCase) {
        movePlayer(player, numberCase, 0.5f, 0, 0);
    }

    private void movePlayer(Entity player, int numberCase, float deltaX, float deltaY, int angle) {
        float radianAngle = (float) Math.toRadians(angle);
        PositionComponent positionComponent = pm.get(player);
        Vector2 position = positionComponent.getPosition();
        lm.get(player).getConeLight().setDirection(radianAngle);
        IntStream.range(0, numberCase).forEach(i -> {
            Optional<Tile> tileRight =
                GameData.INSTANCE.getMap().getTile((int) (position.x + deltaX),
                    (int) (position.y + deltaY));
            if (tileRight.isPresent() && checkMovable(player, deltaX, deltaY)) {
                move(player, deltaX, deltaY);
                positionComponent.getBody().setTransform((position.x + 0.25f) * Tile.SIZE + deltaX,
                    (position.y + 0.25f) * Tile.SIZE + deltaY, radianAngle);
            }
            GameData.INSTANCE.getMap().updateVisibility();
        });
    }

    private boolean checkMovable(Entity player, float deltaX, float deltaY) {
        canMove = true;
        Body body = pm.get(player).getBody();
        World world = body.getWorld();

        Vector2 endPosition = new Vector2(body.getPosition().x + deltaX * Tile.SIZE,
            body.getPosition().y + deltaY * Tile.SIZE);
        world.rayCast(rayCastCallback, body.getPosition(), endPosition);
        return canMove;
    }
}
