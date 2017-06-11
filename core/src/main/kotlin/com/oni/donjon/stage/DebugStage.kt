package com.oni.donjon.stage

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.oni.donjon.data.GameData
import com.oni.donjon.map.Tile

/**
 * @author Daniel Chesters (on 01/06/14).
 */
class DebugStage {
    private val debugRenderer: ShapeRenderer = ShapeRenderer()
    private var gameStage: GameStage? = null

    fun setGameStage(gameStage: GameStage) {
        this.gameStage = gameStage
    }

    fun drawDebug() {
        debugRenderer.projectionMatrix = gameStage!!.camera.combined
        debugRenderer.begin(ShapeRenderer.ShapeType.Line)
        GameData.map.tiles.forEach { t ->
            val rectangle = t.rectangle
            if (t.isKnow) {
                debugRenderer.color = Color.RED
            } else {
                debugRenderer.color = Color.BLUE
            }
            debugRenderer
                    .rect(rectangle.getX() * Tile.SIZE, rectangle.getY() * Tile.SIZE, Tile.SIZE,
                            Tile.SIZE)
        }
        debugRenderer.end()
    }

}
