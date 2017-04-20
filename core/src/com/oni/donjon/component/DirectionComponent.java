package com.oni.donjon.component;

import com.badlogic.ashley.core.Component;
import lombok.Data;

/**
 * @author Daniel Chesters (on 06/02/16).
 */
@Data
public class DirectionComponent implements Component {
    private Direction direction;

    public DirectionComponent() {
        direction = Direction.NONE;
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }

}
