package com.oni.donjon.map

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.StringBuilder
import com.oni.donjon.component.PositionComponent
import com.oni.donjon.data.GameData
import com.oni.donjon.data.GameSave
import com.oni.donjon.generator.CellularAutomataCaveGenerator
import com.oni.donjon.generator.MapGenerator
import ktx.collections.gdxSetOf
import ktx.log.logger
import java.math.BigDecimal
import java.util.*

/**
 * @author Daniel Chesters (on 22/05/14).
 */
class Map {
    companion object {
        val log = logger<Map>()
    }

    var mapHeight: Int = 0
    var mapWidth: Int = 0
    val tiles = gdxSetOf<Tile>()
    private var player: Entity? = null

    @JvmOverloads constructor(generator: MapGenerator = CellularAutomataCaveGenerator()) {
        generator.generate()
        for (x in 0..generator.mapWidth - 1) {
            for (y in 0..generator.mapHeight - 1) {
                tiles.add(Tile(x.toFloat(), y.toFloat(), generator.tileTypes!![x][y]!!, false,
                        GameData.world))
            }
        }
        this.mapHeight = generator.mapHeight
        this.mapWidth = generator.mapWidth
        logMap()
    }

    constructor(gameSave: GameSave) {
        for (x in 0..gameSave.mapWidth!! - 1) {
            for (y in 0..gameSave.mapHeight!! - 1) {
                val savedTile = gameSave.map!![x][y]
                if (savedTile != null) {
                    tiles.add(
                            Tile(x.toFloat(), y.toFloat(), savedTile.type!!, savedTile.know!!,
                                    GameData.world))
                }
            }
        }
        this.mapHeight = gameSave.mapHeight!!
        this.mapWidth = gameSave.mapWidth!!
        logMap()
    }

    fun setPlayer(player: Entity) {
        this.player = player
    }

    fun getTile(x: Float, y: Float): Optional<Tile> {
        val tile = tiles
                .find { BigDecimal.valueOf(it.rectangle.getX().toDouble()).compareTo(BigDecimal.valueOf(x.toDouble())) == 0
                        && BigDecimal.valueOf(it.rectangle.getY().toDouble()).compareTo(BigDecimal.valueOf(y.toDouble())) == 0 }
        return Optional.ofNullable(tile)

    }

    fun updateVisibility() {
        val position = ComponentMapper.getFor(PositionComponent::class.java).get(player!!).position
        for (x in position.x.toInt() - 2..position.x.toInt() + 2) {
            for (y in position.y.toInt() - 2..position.y.toInt() + 2) {
                getTile(x.toFloat(), y.toFloat()).ifPresent { it.isKnow = true }
            }
        }
    }

    val startTile: Tile?
        get() = tiles.find { it.type == TileType.STAIR_UP }

    override fun toString(): String {
        return "Map{" +
                "mapHeight=" + mapHeight +
                ", mapWidth=" + mapWidth +
                ", tiles=" + tiles +
                ", player=" + player +
                '}'
    }

    private fun logMap() {
        val builder = StringBuilder("\n")

        for (y in mapHeight - 1 downTo 0) {
            for (x in 0..mapWidth - 1) {
                getTile(x.toFloat(), y.toFloat()).ifPresent {
                    if (it.type === TileType.WALL) {
                        builder.append('X')
                    } else if (it.type === TileType.GROUND) {
                        builder.append('.')
                    } else {
                        builder.append('*')
                    }
                }
            }
            builder.append('\n')
        }
        log.debug { builder.toString() }
    }
}
