package com.oni.donjon.actor

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.oni.donjon.data.GameData
import com.oni.donjon.map.Tile

/**
 * @author Daniel Chesters (on 25/05/14).
 */
class MapActor : Actor() {
    override fun draw(batch: Batch?, alpha: Float) {
        GameData.map.tiles!!.filter({ it.isKnow }).forEach { t ->
            batch!!.draw(t.type!!.texture, t.rectangle!!.getX() * Tile.SIZE,
                    t.rectangle!!.getY() * Tile.SIZE)
        }
    }
}
