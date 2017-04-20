package com.oni.donjon.generator;

/**
 * @author Daniel Chesters (on 13/02/16).
 */
public class ImproveRectangle extends com.badlogic.gdx.math.Rectangle {

    public ImproveRectangle(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

    public boolean intersects(ImproveRectangle r) {
        return x <= r.x + r.width && x + width >= r.x && y <= r.y + r.height && y + height >= r.y;
    }
}
