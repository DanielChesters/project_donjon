package com.oni.donjon.component;

import box2dLight.ConeLight;
import com.badlogic.ashley.core.Component;
import lombok.Data;

/**
 * @author Daniel Chesters (on 13/02/16).
 */
@Data
public class LightComponent implements Component {
    private ConeLight coneLight;

    public LightComponent(ConeLight coneLight) {
        this.coneLight = coneLight;
    }
}
