package com.oni.donjon.actor

import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.oni.donjon.DonjonGame
import com.oni.donjon.action.Actions

class ListActions(skin: Skin, private val game: DonjonGame) : List<Actions>(skin) {
    override fun toString(actions: Actions): String = actions.getLabel(game)
}
