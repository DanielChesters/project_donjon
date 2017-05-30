package com.oni.donjon.generator;

import com.badlogic.gdx.math.Rectangle;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Daniel Chesters (on 21/04/17).
 */
public class ImproveRectangleTest {


    @Test
    public void testIntersectsTrue() {
        Rectangle rectangle1 = new Rectangle(1, 1, 3, 3);
        Rectangle rectangle2 = new Rectangle(2, 2, 3, 3);
        Assert.assertTrue(
            com.oni.donjon.generator.ImproveRectangleKt.intersects(rectangle1, rectangle2));
    }

    @Test
    public void testIntersectsFalse() {
        Rectangle rectangle1 = new Rectangle(10, 10, 1, 1);
        Rectangle rectangle2 = new Rectangle(5, 5, 3, 3);
        Assert.assertFalse(com.oni.donjon.generator.ImproveRectangleKt.intersects(rectangle1, rectangle2));
    }
}
