package com.oni.donjon.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.Data;

/**
 * @author Daniel Chesters (on 06/02/16).
 */
@Data
public class PositionComponent implements Component {
    private Vector2 position;
    private Body body;

    public PositionComponent(Vector2 position, Body body) {
        this.position = position;
        this.body = body;
    }
}
