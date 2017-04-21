package com.oni.donjon.map;

import com.badlogic.gdx.graphics.Texture;
import com.oni.donjon.screen.GameScreen;

/**
 * @author Daniel Chesters (on 22/05/14).
 */
public enum TileType {
    WALL("textures/wall.png", GameScreen.WALL_BIT),
    DOOR_CLOSE("textures/door_close.png", GameScreen.WALL_BIT),
    DOOR_OPEN("textures/door_open.png", GameScreen.NOTHING_BIT),
    GROUND("textures/ground.png", GameScreen.NOTHING_BIT),
    STAIR_UP("textures/stair_up.png", GameScreen.NOTHING_BIT),
    STAIR_DOWN("textures/stair_down.png", GameScreen.NOTHING_BIT);

    private String texturePath;
    private short categoryBits;

    TileType(String texturePath, short categoryBits) {
        this.texturePath = texturePath;
        this.categoryBits = categoryBits;
    }

    public Texture getTexture() {
        return new Texture(texturePath);
    }

    public short getCategoryBits() {
        return categoryBits;
    }
}
