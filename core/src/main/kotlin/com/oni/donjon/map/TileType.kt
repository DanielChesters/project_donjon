package com.oni.donjon.map

import com.badlogic.gdx.graphics.Texture
import com.oni.donjon.screen.GameScreen
import ktx.collections.gdxMapOf
import ktx.collections.set

/**
 * @author Daniel Chesters (on 22/05/14).
 */
enum class TileType constructor(private val texturePath: String, val categoryBits: Short) {
    WALL("textures/wall.png", GameScreen.WALL_BIT),
    DOOR_CLOSE("textures/door_close.png", GameScreen.WALL_BIT),
    DOOR_OPEN("textures/door_open.png", GameScreen.NOTHING_BIT),
    GROUND("textures/ground.png", GameScreen.NOTHING_BIT),
    STAIR_UP("textures/stair_up.png", GameScreen.NOTHING_BIT),
    STAIR_DOWN("textures/stair_down.png", GameScreen.NOTHING_BIT);

    val texture: Texture?
        get() {
            if (!cacheTexture.containsKey(texturePath)) {
                cacheTexture[texturePath] = Texture(texturePath)
            }
            return cacheTexture[texturePath]
        }

    companion object {
        private val cacheTexture = gdxMapOf<String, Texture>()
    }
}
