package com.oni.donjon.map;

import com.badlogic.gdx.graphics.Texture;

/**
 * @author Daniel Chesters (on 22/05/14).
 */
public enum TileType {
    WALL("textures/wall.png", true),
    DOOR_CLOSE("textures/door_close.png", true),
    DOOR_OPEN("textures/door_open.png", false),
    GROUND("textures/ground.png", false),
    STAIR_UP("textures/stair_up.png", true),
    STAIR_DOWN("textures/stair_down.png", true);

    private Texture texture;
    private boolean block;

    TileType(String texture, boolean block) {
        this.texture = new Texture(texture);
        this.block = block;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isBlock() {
        return block;
    }
}
