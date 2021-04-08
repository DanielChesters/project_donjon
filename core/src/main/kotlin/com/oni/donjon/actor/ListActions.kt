package com.oni.donjon.actor

import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import com.oni.donjon.action.Actions

class ListActions(skin: Skin, private val bundle: I18NBundle) : List<Actions>(skin) {
    override fun toString(actions: Actions): String = actions.getLabel(bundle)
}
