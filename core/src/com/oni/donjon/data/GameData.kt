package com.oni.donjon.data

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.oni.donjon.component.DirectionComponent
import com.oni.donjon.component.PositionComponent
import com.oni.donjon.map.Map

/**
 * @author Daniel Chesters (on 30/05/2017).
 */
object GameData {
    lateinit var map: Map
    lateinit var player: Entity
    lateinit var world: World

    fun getPlayerPosition(): Vector2 = ComponentMapper.getFor(PositionComponent::class.java).get(player).position

    fun setPlayerDirection(direction: DirectionComponent.Direction) {
        ComponentMapper.getFor(DirectionComponent::class.java).get(player).direction = direction
    }

    fun toGameSave(): GameSave {
        val savedTiles = Array(map.mapWidth) { arrayOfNulls<GameSave.SavedTile>(map.mapHeight) }

        for (tile in map.tiles) {
            val savedTile = GameSave.SavedTile(tile.type, tile.isKnow)
            savedTiles[tile.rectangle.x.toInt()][tile.rectangle.y.toInt()] = savedTile
        }

        return GameSave(map.mapHeight, map.mapWidth, savedTiles, getPlayerPosition())
    }
}
