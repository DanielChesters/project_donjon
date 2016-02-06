package com.oni.donjon.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Daniel Chesters (on 06/02/16).
 */
public class PositionComponent implements Component {
    public PositionComponent(Vector2 position) {
        this.position = position;
    }

    public Vector2 position;
}
