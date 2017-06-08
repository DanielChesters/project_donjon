package com.oni.donjon.action

import com.oni.donjon.map.Tile
import com.oni.donjon.stage.UIStage

/**
 * @author Daniel Chesters (on 02/06/14).
 */
interface Action {
    fun doAction(tile: Tile, stage: UIStage)
}