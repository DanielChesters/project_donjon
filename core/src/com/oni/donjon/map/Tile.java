package com.oni.donjon.map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.NumberUtils;

/**
 * @author Daniel Chesters (on 22/05/14).
 */
public class Tile {
    public static final float SIZE = 32f;
    private TileType type;
    private Rectangle rectangle;

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Tile(float x, float y, TileType type) {
        this.rectangle = new Rectangle(x, y, SIZE, SIZE);
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tile tile = (Tile) o;

        return this.rectangle.getX() == tile.rectangle.getX() && this.rectangle.getY() == tile.rectangle.getY();
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + NumberUtils.floatToIntBits(rectangle.getX());
        result = prime * result + NumberUtils.floatToIntBits(rectangle.getY());
        return result;
    }
}
