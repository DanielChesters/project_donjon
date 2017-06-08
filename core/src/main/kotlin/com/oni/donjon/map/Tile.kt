package com.oni.donjon.map

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.*
import com.oni.donjon.screen.GameScreen

import java.util.Objects
import kotlin.experimental.or

/**
 * @author Daniel Chesters (on 22/05/14).
 */
class Tile {
    var type: TileType? = null
    var rectangle: Rectangle? = null
        private set
    var isKnow: Boolean = false
    var body: Body? = null

    override fun toString(): String {
        return "Tile{" +
                "type=" + type +
                ", rectangle=" + rectangle +
                ", know=" + isKnow +
                '}'
    }

    constructor() {
        this.rectangle = Rectangle()
    }

    constructor(x: Float, y: Float, type: TileType, know: Boolean, world: World) {
        this.rectangle = Rectangle(x, y, SIZE, SIZE)
        this.type = type
        this.isKnow = know
        if (type.categoryBits != GameScreen.NOTHING_BIT) {
            this.body = createBody(world)
        }
    }

    fun createBody(world: World): Body {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        bodyDef.position.set(rectangle!!.x * SIZE + rectangle!!.getWidth() / 2,
                rectangle!!.y * SIZE + rectangle!!.getHeight() / 2)
        val worldBody = world.createBody(bodyDef)

        val polygonShape = PolygonShape()
        polygonShape.setAsBox(rectangle!!.width / 2, rectangle!!.height / 2)
        val fixtureDef = FixtureDef()
        fixtureDef.shape = polygonShape
        fixtureDef.filter.categoryBits = type!!.categoryBits
        fixtureDef.filter.maskBits = GameScreen.PLAYER_BIT or GameScreen
                .LIGHT_BIT
        worldBody.createFixture(fixtureDef)
        polygonShape.dispose()

        return worldBody
    }

    override fun equals(o: Any?): Boolean {
        if (this === o)
            return true
        if (o == null || javaClass != o.javaClass)
            return false
        val tile = o as Tile?
        return isKnow == tile!!.isKnow &&
                type === tile.type &&
                rectangle == tile.rectangle &&
                body == tile.body
    }

    override fun hashCode(): Int {
        return Objects.hash(type, rectangle, isKnow, body)
    }

    companion object {
        val SIZE = 32f
    }
}
