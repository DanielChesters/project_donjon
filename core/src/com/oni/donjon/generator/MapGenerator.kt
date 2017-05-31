package com.oni.donjon.generator

import com.badlogic.gdx.math.MathUtils
import com.oni.donjon.map.TileType

/**
 * @author Daniel Chesters (on 10/05/2017).
 */
abstract class MapGenerator {
    var mapHeight: Int = 0
    var mapWidth: Int = 0
    var tileTypes: Array<Array<TileType?>>? = null

    protected fun placeSpecialTile(tileType: TileType) {
        var x: Int
        var y: Int
        do {
            x = MathUtils.random(0, mapWidth - 1)
            y = MathUtils.random(0, mapWidth - 1)
        } while (TileType.GROUND != tileTypes!![x][y])
        tileTypes!![x][y] = tileType
    }

    abstract fun generate()
}
