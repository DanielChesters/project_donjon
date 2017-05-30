package com.oni.donjon.component

import box2dLight.ConeLight
import com.badlogic.ashley.core.Component

/**
 * @author Daniel Chesters (on 13/02/16).
 */
data class LightComponent(val coneLight: ConeLight) : Component
