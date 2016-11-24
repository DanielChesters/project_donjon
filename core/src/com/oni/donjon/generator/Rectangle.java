package com.oni.donjon.generator;

/**
 * @author Daniel Chesters (on 13/02/16).
 */
public class Rectangle extends com.badlogic.gdx.math.Rectangle {

    public Rectangle(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

    public boolean intersects(Rectangle r) {
        return x <= r.x + r.width && x + width >= r.x && y <= r.y + r.height && y + height >= r.y;
    }
}
