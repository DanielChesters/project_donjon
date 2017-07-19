package com.oni.donjon.generator

import com.badlogic.gdx.math.Rectangle
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * @author Daniel Chesters (on 21/04/17).
 */
class ImproveRectangleTest {

    @Test
    fun testIntersectsTrue() {
        val rectangle1 = Rectangle(1f, 1f, 3f, 3f)
        val rectangle2 = Rectangle(2f, 2f, 3f, 3f)
        assertTrue(rectangle1.intersects(rectangle2))
    }

    @Test
    fun testIntersectsFalse() {
        val rectangle1 = Rectangle(10f, 10f, 1f, 1f)
        val rectangle2 = Rectangle(5f, 5f, 3f, 3f)
        assertFalse(rectangle1.intersects(rectangle2))
    }
}
