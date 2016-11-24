package com.oni.donjon.generator;

import com.oni.donjon.map.TileType;

/**
 * @author Daniel Chesters (on 24/11/16).
 */
public interface MapGenerator {

    TileType[][] getTileTypes();

    void generate();

    int getMapHeight();

    int getMapWidth();
}
