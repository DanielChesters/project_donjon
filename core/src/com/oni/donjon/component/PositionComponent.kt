package com.oni.donjon.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

/**
 * @author Daniel Chesters (on 06/02/16).
 */
data class PositionComponent(var position: Vector2, val body: Body) : Component
