package com.oni.donjon.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.physics.box2d.RayCastCallback
import com.oni.donjon.component.DirectionComponent
import com.oni.donjon.component.LightComponent
import com.oni.donjon.component.PositionComponent
import com.oni.donjon.data.GameData
import com.oni.donjon.map.Tile
import com.oni.donjon.screen.GameScreen
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.vec2
import java.math.BigDecimal

/**
 * @author Daniel Chesters (on 06/02/16).
 */
class MovementSystem : IteratingSystem(allOf(PositionComponent::class, DirectionComponent::class, LightComponent::class)
        .get()) {
    private var canMove: Boolean = false

    private val rayCastCallback: RayCastCallback = RayCastCallback { fixture, _, _, _ ->
        if (fixture.filterData.categoryBits == GameScreen.WALL_BIT) {
            canMove = false
        }
        0F
    }

    private val dm = mapperFor<DirectionComponent>()
    private val pm = mapperFor<PositionComponent>()
    private val lm = mapperFor<LightComponent>()

    override fun processEntity(player: Entity, deltaTime: Float) {
        updateMove(player)
    }

    private fun move(player: Entity, deltaX: Float, deltaY: Float) {
        val position = pm[player].position
        if (BigDecimal.valueOf(deltaX.toDouble()).compareTo(BigDecimal.ZERO) != 0) {
            position.x += deltaX
        }
        if (BigDecimal.valueOf(deltaY.toDouble()).compareTo(BigDecimal.ZERO) != 0) {
            position.y += deltaY
        }
    }

    private fun updateMove(player: Entity) {
        val numCase = caseToGo
        when (dm[player].direction) {
            DirectionComponent.Direction.UP -> goUp(player, numCase)
            DirectionComponent.Direction.DOWN -> goDown(player, numCase)
            DirectionComponent.Direction.RIGHT -> goRight(player, numCase)
            DirectionComponent.Direction.LEFT -> goLeft(player, numCase)
            DirectionComponent.Direction.NONE -> {}
        }
    }

    private val caseToGo: Int
        get() {
            return if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) || Gdx.input
                            .isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                Constants.caseToMoveOnRun
            } else {
                Constants.caseToMoveOnWalk
            }
        }

    private fun goDown(player: Entity, numberCase: Int) {
        movePlayer(player, numberCase, 0f, Constants.deltaYForGoDown, Constants.angleForGoDown)
    }

    private fun goUp(player: Entity, numberCase: Int) {
        movePlayer(player, numberCase, 0f, Constants.deltaYForGoUp, Constants.angleForGoUp)
    }

    private fun goLeft(player: Entity, numberCase: Int) {
        movePlayer(player, numberCase, Constants.deltaXForGoLeft, 0f, Constants.angleForGoLeft)
    }

    private fun goRight(player: Entity, numberCase: Int) {
        movePlayer(player, numberCase, Constants.deltaXForGoRight, 0f, Constants.angleForGoRight)
    }

    private fun movePlayer(player: Entity, numberCase: Int, deltaX: Float, deltaY: Float, angle: Int) {
        val radianAngle = Math.toRadians(angle.toDouble()).toFloat()
        val (position, body) = pm[player]
        lm[player].coneLight.direction = radianAngle

        (0..numberCase).forEach { _ ->
            val tileRight = GameData.map.getTile((position.x + deltaX).toInt().toFloat(),
                (position.y + deltaY).toInt().toFloat())
            if (tileRight.isPresent && checkMovable(player, deltaX, deltaY)) {
                move(player, deltaX, deltaY)
                body.setTransform((position.x + Constants.deltaTransformOnMovePlayer) * Tile
                    .SIZE + deltaX,
                    (position.y + Constants.deltaTransformOnMovePlayer) * Tile.SIZE + deltaY, radianAngle)
            }
            GameData.map.updateVisibility()
        }
    }

    private fun checkMovable(player: Entity, deltaX: Float, deltaY: Float): Boolean {
        canMove = true
        val body = pm[player].body
        val world = body.world

        val endPosition = vec2(body.position.x + deltaX * Tile.SIZE,
                body.position.y + deltaY * Tile.SIZE)
        world.rayCast(rayCastCallback, body.position, endPosition)
        return canMove
    }

    object Constants {
        const val caseToMoveOnRun = 10
        const val caseToMoveOnWalk = 1
        const val deltaYForGoDown = -0.5f
        const val angleForGoDown = 270
        const val deltaYForGoUp = 0.5f
        const val angleForGoUp = 90
        const val deltaXForGoLeft = -0.5f
        const val angleForGoLeft = 180
        const val deltaXForGoRight = 0.5f
        const val angleForGoRight = 0
        const val deltaTransformOnMovePlayer = 0.25f
    }
}
