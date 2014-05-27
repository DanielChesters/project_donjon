package com.oni.donjon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * @author Daniel Chesters (on 27/05/14).
 */
public class Resources {
    public static final I18NBundle bundle = I18NBundle.createBundle(Gdx.files.internal("i18n/messages"));

    private Resources() {
    }
}
