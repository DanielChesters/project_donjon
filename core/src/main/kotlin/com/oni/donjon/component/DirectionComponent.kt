package com.oni.donjon.component

import com.badlogic.ashley.core.Component

/**
 * @author Daniel Chesters (on 06/02/16).
 */
data class DirectionComponent(var direction: Direction = Direction.NONE) : Component {
    enum class Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }
}
