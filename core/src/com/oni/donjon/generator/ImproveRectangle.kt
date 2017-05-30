package com.oni.donjon.generator

import com.badlogic.gdx.math.Rectangle

/**
 * @author Daniel Chesters (on 30/05/2017).
 */

fun Rectangle.intersects(r: Rectangle): Boolean = x <= r.x + r.width && x + width >= r.x && y <= r.y + r.height && y + height >= r.y
