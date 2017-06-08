package com.oni.donjon.stage

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.oni.donjon.action.Actions

/**
 * @author Daniel Chesters (on 01/06/14).
 */
class UIStage : Stage() {

    var actionList: List<Actions>? = null
    var messageLabel: Label? = null
}
