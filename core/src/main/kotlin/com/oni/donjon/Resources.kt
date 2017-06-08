package com.oni.donjon

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.I18NBundle.createBundle

/**
 * @author Daniel Chesters (on 30/05/2017).
 */
object Resources {
    @JvmField
    val BUNDLE = createBundle(Gdx.files.internal("i18n/messages"))!!
}
