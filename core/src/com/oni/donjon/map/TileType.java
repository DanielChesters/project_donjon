package com.oni.donjon.map;

import com.badlogic.gdx.graphics.Texture;
import com.oni.donjon.screen.GameScreen;

import java.util.HashMap;

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

    private static final java.util.Map<String, Texture> cacheTexture = new HashMap<>();

    private String texturePath;
    private short categoryBits;

    TileType(String texturePath, short categoryBits) {
        this.texturePath = texturePath;
        this.categoryBits = categoryBits;
    }

    public Texture getTexture() {
        if (!cacheTexture.containsKey(texturePath)) {
            cacheTexture.put(texturePath, new Texture(texturePath));
        }
        return cacheTexture.get(texturePath);
    }

    public short getCategoryBits() {
        return categoryBits;
    }
}
