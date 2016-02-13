package com.oni.donjon.component;

import box2dLight.ConeLight;
import com.badlogic.ashley.core.Component;

/**
 * @author Daniel Chesters (on 13/02/16).
 */
public class LightComponent implements Component {
    public ConeLight coneLight;

    public LightComponent(ConeLight coneLight) {
        this.coneLight = coneLight;
    }
}
