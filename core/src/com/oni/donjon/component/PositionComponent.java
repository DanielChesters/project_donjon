package com.oni.donjon.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * @author Daniel Chesters (on 06/02/16).
 */
public class PositionComponent implements Component {
    public Vector2 position;
    public Body body;

    public PositionComponent(Vector2 position, Body body) {
        this.position = position;
        this.body = body;
    }
}
