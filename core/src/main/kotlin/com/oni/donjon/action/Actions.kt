package com.oni.donjon.action

import com.oni.donjon.Resources
import com.oni.donjon.map.Tile
import com.oni.donjon.stage.UIStage

/**
 * @author Daniel Chesters (on 26/05/14).
 */
enum class Actions(private val label: String, private val action: Action) {
    LOOK(Resources.BUNDLE.get("action.look"), LookAction()),
    OPEN(Resources.BUNDLE.get("action.open"), OpenAction()),
    CLOSE(Resources.BUNDLE.get("action.close"), CloseAction());

    override fun toString(): String {
        return label
    }

    fun doAction(tile: Tile, stage: UIStage) {
        action.doAction(tile, stage)
    }
}