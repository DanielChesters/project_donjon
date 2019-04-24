package com.oni.donjon.screen

import box2dLight.RayHandler
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.utils.Json
import com.oni.donjon.DonjonGame
import com.oni.donjon.data.GameData
import com.oni.donjon.data.GameSave
import com.oni.donjon.map.Map
import com.oni.donjon.map.Tile
import com.oni.donjon.sound.Sounds
import com.oni.donjon.stage.DebugStage
import com.oni.donjon.system.MovementSystem
import ktx.app.KtxScreen
import ktx.box2d.createWorld
import ktx.log.logger

/**
 * @author Daniel Chesters (on 25/05/14).
 */
class GameScreen(game: DonjonGame, private val saveFile: String = "") : KtxScreen {
    val engine = PooledEngine()
    val world = createWorld()
    private val debugRenderer by lazy { Box2DDebugRenderer() }
    val rayHandler = RayHandler(world)
    private val gameScreenElements = GameScreenElements(game, this)

    val uiStage = gameScreenElements.createUi()
    val gameStage = gameScreenElements.createGameStage()
    private val debugStage by lazy { DebugStage(gameStage) }
    var state = GameState.RUNNING

    init {
        val movementSystem = MovementSystem()
        engine.addSystem(movementSystem)
        GameData.world = world

        if (saveFile.isEmpty()) {
            createData()
        } else {
            loadData()
        }
        gameScreenElements.createInput()
    }

    override fun dispose() {
        super.dispose()
        world.dispose()
        rayHandler.dispose()
        if (Gdx.app.logLevel == Application.LOG_DEBUG) {
            debugRenderer.dispose()
        }
        Sounds.disposeAll()
    }

    private fun loadData() {
        val json = Json()
        val file = Gdx.files.external(saveFile)
        val save = json.fromJson(GameSave::class.java, file)

        GameData.map = Map(save)
        val playerEntity = gameScreenElements.createPlayerEntity(save.playerPosition)

        GameData.map.setPlayer(playerEntity)
        GameData.player = playerEntity
        gameStage.updatePlayer()
    }

    private fun createData() {
        val map = Map()
        val startTile = map.startTile
        val startPosition = Vector2(startTile?.rectangle!!.getX(), startTile.rectangle.getY())
        val player = gameScreenElements.createPlayerEntity(startPosition)
        GameData.map = map
        GameData.player = player

        map.setPlayer(player)

        gameStage.updatePlayer()
        map.updateVisibility()
    }

    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height)
        uiStage.viewport.update(width, height)
    }

    override fun render(delta: Float) {
        when (state) {
            GameState.RUNNING -> update(delta)
            GameState.PAUSE -> updatePause()
        }
    }

    private fun updatePause() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        gameStage.draw()
        if (Gdx.app.logLevel == Application.LOG_DEBUG) {
            debugStage.drawDebug()
            debugRenderer.render(world, gameStage.camera.combined)
        }
        uiStage.draw()
    }

    private fun update(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        engine.update(delta)
        gameStage.updatePlayer()
        gameStage.camera.position
                .set(GameData.getPlayerPosition().x * Tile.SIZE,
                        GameData.getPlayerPosition().y * Tile.SIZE, 0f)
        gameStage.camera.update()
        gameStage.draw()
        rayHandler.setCombinedMatrix(gameStage.camera as OrthographicCamera)
        rayHandler.updateAndRender()
        world.step(1 / FRAME_PER_SECOND, DEFAULT_VELOCITY_ITERATIONS, 2)
        if (Gdx.app.logLevel == Application.LOG_DEBUG) {
            debugStage.drawDebug()
            debugRenderer.render(world, gameStage.camera.combined)
        }
        uiStage.draw()
    }

    enum class GameState {
        RUNNING, PAUSE
    }

    companion object {
        val log = logger<GameScreen>()
        const val NOTHING_BIT: Short = 0
        const val WALL_BIT: Short = 1
        const val PLAYER_BIT = (1 shl 1).toShort()
        const val LIGHT_BIT = (1 shl 5).toShort()
        const val FRAME_PER_SECOND = 60f
        const val DEFAULT_VELOCITY_ITERATIONS = 6
    }
}
