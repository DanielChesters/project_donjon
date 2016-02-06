package com.oni.donjon.component;

import com.badlogic.ashley.core.Component;

/**
 * @author Daniel Chesters (on 06/02/16).
 */
public class DirectionComponent implements Component {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }

    public DirectionComponent() {
        direction = Direction.NONE;
    }

    public Direction direction;

}
