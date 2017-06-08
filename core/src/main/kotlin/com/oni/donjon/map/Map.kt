package com.oni.donjon.map

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.StringBuilder
import com.oni.donjon.component.PositionComponent
import com.oni.donjon.data.GameData
import com.oni.donjon.data.GameSave
import com.oni.donjon.generator.CellularAutomataCaveGenerator
import com.oni.donjon.generator.MapGenerator
import java.math.BigDecimal
import java.util.*

/**
 * @author Daniel Chesters (on 22/05/14).
 */
class Map {
    var mapHeight: Int = 0
    var mapWidth: Int = 0
    internal var tiles: MutableSet<Tile>? = null
    private var player: Entity? = null

    @JvmOverloads constructor(generator: MapGenerator = CellularAutomataCaveGenerator()) {
        generator.generate()
        this.tiles = HashSet<Tile>()
        for (x in 0..generator.mapWidth - 1) {
            for (y in 0..generator.mapHeight - 1) {
                tiles!!.add(Tile(x.toFloat(), y.toFloat(), generator.tileTypes!![x][y]!!, false,
                        GameData.world))
            }
        }
        this.mapHeight = generator.mapHeight
        this.mapWidth = generator.mapWidth
        if (Gdx.app.logLevel == Application.LOG_DEBUG) {
            logMap()
        }
    }

    constructor(gameSave: GameSave) {
        this.tiles = HashSet<Tile>()

        for (x in 0..gameSave.mapWidth!! - 1) {
            for (y in 0..gameSave.mapHeight!! - 1) {
                val savedTile = gameSave.map!![x][y]
                if (savedTile != null) {
                    tiles!!.add(
                            Tile(x.toFloat(), y.toFloat(), savedTile.type!!, savedTile.know!!,
                                    GameData.world))
                }
            }
        }
        this.mapHeight = gameSave.mapHeight!!
        this.mapWidth = gameSave.mapWidth!!
        if (Gdx.app.logLevel == Application.LOG_DEBUG) {
            logMap()
        }
    }

    fun setPlayer(player: Entity) {
        this.player = player
    }

    fun getTile(x: Float, y: Float): Optional<Tile> {
        return tiles!!.stream()
                .filter { t -> BigDecimal.valueOf(t.rectangle!!.getX().toDouble()).compareTo(BigDecimal.valueOf(x.toDouble())) == 0 && BigDecimal.valueOf(t.rectangle!!.getY().toDouble()).compareTo(BigDecimal.valueOf(y.toDouble())) == 0 }
                .findFirst()
    }

    fun getTiles(): Set<Tile> {
        return tiles!!
    }

    fun updateVisibility() {
        val position = ComponentMapper.getFor(PositionComponent::class.java).get(player!!).position
        for (x in position.x.toInt() - 2..position.x.toInt() + 2) {
            for (y in position.y.toInt() - 2..position.y.toInt() + 2) {
                getTile(x.toFloat(), y.toFloat()).ifPresent { t -> t.isKnow = true }
            }
        }
    }

    val startTile: Tile
        get() = tiles!!.stream().filter { t -> t.type == TileType.STAIR_UP }.findFirst()
                .orElse(null)

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
                getTile(x.toFloat(), y.toFloat()).ifPresent { t ->
                    if (t.type === TileType.WALL) {
                        builder.append('X')
                    } else if (t.type === TileType.GROUND) {
                        builder.append('.')
                    } else {
                        builder.append('*')
                    }
                }
            }
            builder.append('\n')
        }
        Gdx.app.debug("Map", builder.toString())
    }
}
