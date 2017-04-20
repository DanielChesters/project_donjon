package com.oni.donjon.test;

import com.oni.donjon.generator.ImproveRectangle;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Daniel Chesters (on 21/04/17).
 */
public class ImproveRectangleTest {


    @Test
    public void testIntersectsTrue() {
        ImproveRectangle rectangle1 = new ImproveRectangle(1, 1, 3, 3);
        ImproveRectangle rectangle2 = new ImproveRectangle(2, 2, 3, 3);
        Assert.assertTrue(rectangle1.intersects(rectangle2));
    }

    @Test
    public void testIntersectsFalse() {
        ImproveRectangle rectangle1 = new ImproveRectangle(10, 10, 1, 1);
        ImproveRectangle rectangle2 = new ImproveRectangle(5, 5, 3, 3);
        Assert.assertFalse(rectangle1.intersects(rectangle2));
    }
}
