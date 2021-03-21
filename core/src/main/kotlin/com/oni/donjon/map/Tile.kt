package com.oni.donjon.map

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.oni.donjon.screen.GameScreen
import ktx.box2d.body
import ktx.box2d.box
import ktx.math.vec2
import kotlin.experimental.or

/**
 * @author Daniel Chesters (on 22/05/14).
 */
data class Tile(
    val x: Float = -1f,
    val y: Float = -1f,
    var type: TileType,
    var isKnow: Boolean = false,
    private val world: World
) {
    var rectangle: Rectangle
    lateinit var body: Body

    companion object {
        const val SIZE = 32f
    }

    init {
        if (x < 0 || y < 0) {
            this.rectangle = Rectangle()
        } else {
            this.rectangle = Rectangle(x, y, SIZE, SIZE)
            if (type.categoryBits != GameScreen.NOTHING_BIT) {
                this.body = createBody(world)
            }
        }
    }

    fun createBody(world: World): Body {
        return world.body {
            val realX = rectangle.x * SIZE + rectangle.getWidth() / 2
            val realY = rectangle.y * SIZE + rectangle.getHeight() / 2
            box(
                width = rectangle.width,
                height = rectangle.height,
                position = vec2(
                    realX,
                    realY
                )
            ) {
                filter.categoryBits = this@Tile.type.categoryBits
                filter.maskBits = GameScreen.PLAYER_BIT or GameScreen.LIGHT_BIT
            }
        }
    }
}
