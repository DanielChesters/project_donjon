package com.oni.donjon.action

import com.badlogic.gdx.utils.I18NBundle
import com.oni.donjon.map.Tile
import com.oni.donjon.stage.UIStage

/**
 * @author Daniel Chesters (on 26/05/14).
 */
enum class Actions(private val labelKey: String, private val action: Action) {
    LOOK("action.look", LookAction()),
    OPEN("action.open", OpenAction()),
    CLOSE("action.close", CloseAction());

    fun getLabel(bundle: I18NBundle): String = bundle[labelKey]

    fun doAction(tile: Tile, stage: UIStage, bundle: I18NBundle) {
        action.doAction(tile, stage, bundle)
    }
}
