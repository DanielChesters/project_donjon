package com.oni.donjon.action

import com.badlogic.gdx.utils.I18NBundle
import com.oni.donjon.DonjonGame
import com.oni.donjon.map.Tile
import com.oni.donjon.stage.UIStage

/**
 * @author Daniel Chesters (on 26/05/14).
 */
enum class Actions(private val labelKey: String, private val action: Action) {
    LOOK("action.look", LookAction()),
    OPEN("action.open", OpenAction()),
    CLOSE("action.close", CloseAction());

    fun getLabel(game: DonjonGame): String = game.context.inject<I18NBundle>()[labelKey]

    fun doAction(tile: Tile, stage: UIStage, game: DonjonGame) {
        action.doAction(tile, stage, game)
    }
}
