package com.oni.donjon.map;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Chesters (on 22/05/14).
 */
public class Map {
    private Set<Tile> tiles;

    public Tile getTile(float x, float y) {
        return tiles.stream().filter(t -> t.getRectangle().getX() == x && t.getRectangle().getY() == y).findFirst().get();
    }

    public Map() {
        tiles = new HashSet<>();
    }

    public Set<Tile> getTiles() {
        return tiles;
    }
}
