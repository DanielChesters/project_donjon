package com.oni.donjon.generator

import com.badlogic.gdx.math.Rectangle

/**
 * @param r the other {@link Rectangle}
 * @return whether this rectangle overlaps the other rectangle or the two rectangle are side by side.
 * @author Daniel Chesters (on 30/05/2017).
 *
 */
fun Rectangle.intersects(r: Rectangle): Boolean =
        x <= r.x + r.width && x + width >= r.x && y <= r.y + r.height && y + height >= r.y
