package com.oni.donjon.test;

import com.oni.donjon.generator.ImproveRectangle;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Daniel Chesters (on 4/21/17).
 */
public class ImproveRectangleTest {


    @Test
    public void testIntersects() {
        ImproveRectangle rectangle1 = new ImproveRectangle(1, 1, 3, 3);
        ImproveRectangle rectangle2 = new ImproveRectangle(2, 2, 3, 3);
        Assert.assertTrue(rectangle1.intersects(rectangle2));
    }
}
