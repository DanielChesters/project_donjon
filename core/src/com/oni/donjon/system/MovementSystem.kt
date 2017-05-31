package com.oni.donjon.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.RayCastCallback
import com.oni.donjon.component.DirectionComponent
import com.oni.donjon.component.LightComponent
import com.oni.donjon.component.PositionComponent
import com.oni.donjon.data.GameData
import com.oni.donjon.map.Tile
import com.oni.donjon.screen.GameScreen
import java.math.BigDecimal
import java.util.stream.IntStream

/**
 * @author Daniel Chesters (on 06/02/16).
 */
class MovementSystem : IteratingSystem(Family.all(PositionComponent::class.java, DirectionComponent::class.java, LightComponent::class.java)
        .get()) {
    internal var canMove: Boolean = false

    private val rayCastCallback: RayCastCallback = RayCastCallback { fixture, _, _, _ ->
        if (fixture.filterData.categoryBits == GameScreen.WALL_BIT) {
            canMove = false
        }
        0F
    }

    private val dm = ComponentMapper.getFor(DirectionComponent::class.java)
    private val pm = ComponentMapper.getFor(PositionComponent::class.java)
    private val lm = ComponentMapper.getFor(LightComponent::class.java)

    override fun processEntity(player: Entity, deltaTime: Float) {
        updateMove(player)
    }

    private fun move(player: Entity, deltaX: Float, deltaY: Float) {
        val (position) = pm.get(player)
        if (BigDecimal.valueOf(deltaX.toDouble()).compareTo(BigDecimal.ZERO) != 0) {
            addX(deltaX, position)
        }
        if (BigDecimal.valueOf(deltaY.toDouble()).compareTo(BigDecimal.ZERO) != 0) {
            addY(deltaY, position)
        }
    }

    private fun addX(x: Float, position: Vector2) {
        position.x += x
    }

    private fun addY(y: Float, position: Vector2) {
        position.y += y
    }

    private fun updateMove(player: Entity) {
        val numCase = caseToGo
        val (direction) = dm.get(player)
        when (direction) {
            DirectionComponent.Direction.UP -> goUp(player, numCase)
            DirectionComponent.Direction.DOWN -> goDown(player, numCase)
            DirectionComponent.Direction.RIGHT -> goRight(player, numCase)
            DirectionComponent.Direction.LEFT -> goLeft(player, numCase)
            else -> {
            }
        }
    }

    private val caseToGo: Int
        get() {
            val numberCase: Int
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) || Gdx.input
                    .isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                numberCase = 10
            } else {
                numberCase = 1
            }
            return numberCase
        }

    private fun goDown(player: Entity, numberCase: Int) {
        movePlayer(player, numberCase, 0f, -0.5f, 270)
    }

    private fun goUp(player: Entity, numberCase: Int) {
        movePlayer(player, numberCase, 0f, 0.5f, 90)
    }

    private fun goLeft(player: Entity, numberCase: Int) {
        movePlayer(player, numberCase, -0.5f, 0f, 180)
    }

    private fun goRight(player: Entity, numberCase: Int) {
        movePlayer(player, numberCase, 0.5f, 0f, 0)
    }

    private fun movePlayer(player: Entity, numberCase: Int, deltaX: Float, deltaY: Float, angle: Int) {
        val radianAngle = Math.toRadians(angle.toDouble()).toFloat()
        val (position, body) = pm.get(player)
        lm.get(player).coneLight.setDirection(radianAngle)
        IntStream.range(0, numberCase).forEach { _ ->
            val tileRight = GameData.map.getTile((position.x + deltaX).toInt().toFloat(),
                    (position.y + deltaY).toInt().toFloat())
            if (tileRight.isPresent && checkMovable(player, deltaX, deltaY)) {
                move(player, deltaX, deltaY)
                body.setTransform((position.x + 0.25f) * Tile
                        .SIZE + deltaX,
                        (position.y + 0.25f) * Tile.SIZE + deltaY, radianAngle)
            }
            GameData.map.updateVisibility()
        }
    }

    private fun checkMovable(player: Entity, deltaX: Float, deltaY: Float): Boolean {
        canMove = true
        val body = pm.get(player).body
        val world = body.world

        val endPosition = Vector2(body.position.x + deltaX * Tile.SIZE,
                body.position.y + deltaY * Tile.SIZE)
        world.rayCast(rayCastCallback, body.position, endPosition)
        return canMove
    }
}
