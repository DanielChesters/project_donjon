package com.oni.donjon.screen

import box2dLight.ConeLight
import box2dLight.RayHandler
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.Json
import com.oni.donjon.DonjonGame
import com.oni.donjon.Resources
import com.oni.donjon.action.Actions
import com.oni.donjon.actor.MapActor
import com.oni.donjon.actor.MenuGameWindow
import com.oni.donjon.actor.SaveWindow
import com.oni.donjon.component.DirectionComponent
import com.oni.donjon.component.LightComponent
import com.oni.donjon.component.PositionComponent
import com.oni.donjon.data.GameData
import com.oni.donjon.data.GameSave
import com.oni.donjon.input.KeyboardInput
import com.oni.donjon.input.MouseInput
import com.oni.donjon.map.Map
import com.oni.donjon.map.Tile
import com.oni.donjon.sound.Sounds
import com.oni.donjon.stage.DebugStage
import com.oni.donjon.stage.GameStage
import com.oni.donjon.stage.UIStage
import com.oni.donjon.system.MovementSystem
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.box2d.body
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.plus
import ktx.math.times
import ktx.math.vec2

/**
 * @author Daniel Chesters (on 25/05/14).
 */
class GameScreen(val game: DonjonGame, val saveFile: String = "") : KtxScreen {
    val skin: Skin = game.context.inject()
    val bundle: I18NBundle = game.context.inject()

    val engine: PooledEngine = PooledEngine()
    val world: World = createWorld()
    val debugRenderer: Box2DDebugRenderer by lazy { Box2DDebugRenderer() }
    val rayHandler: RayHandler = RayHandler(world)
    val uiStage: UIStage = createUi()
    val gameStage: GameStage = createGameStage()
    val debugStage: DebugStage by lazy { createDebugStage() }
    var state: GameState = GameState.RUNNING


    init {
        val movementSystem = MovementSystem()
        engine.addSystem(movementSystem)
        GameData.world = world

        if (saveFile.isEmpty()) {
            createData()
        } else {
            loadData()
        }
        createInput()
    }

    override fun dispose() {
        super.dispose()
        world.dispose()
        rayHandler.dispose()
        debugRenderer.dispose()
        Sounds.disposeAll()
    }

    private fun createUi(): UIStage {
        val messageLabel = createMessageLabel()
        val actionList = createActionList()
        val actionWindow = createActionWindow(actionList)
        val saveWindow = SaveWindow(bundle["window.save.title"], skin)
        val menuWindow = MenuGameWindow(saveWindow, game, this)
        val menuButton = createMenuButton(menuWindow)


        val uiStage = UIStage(actionList, messageLabel)

        uiStage.addActor(messageLabel)
        uiStage.addActor(actionWindow)
        uiStage.addActor(menuWindow.window)
        uiStage.addActor(menuButton)
        uiStage.addActor(saveWindow.window)

        return uiStage
    }

    private fun createMenuButton(menuWindow: MenuGameWindow): TextButton {
        val menuButton = TextButton(bundle["game_menu.title"], skin)
        menuButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int,
                                   button: Int): Boolean {
                menuWindow.setVisible(true)
                state = GameState.PAUSE
                return true
            }
        })
        menuButton.pack()
        menuButton.setPosition(Gdx.graphics.width - (menuButton.width + 5),
                Gdx.graphics.height - (menuButton.height + 5))
        return menuButton
    }

    private fun createActionWindow(actionList: List<Actions>): Window {
        val actionWindow = Window(bundle["window.action.title"], skin)
        actionWindow.setPosition(20f, Gdx.graphics.height / 2f)
        actionWindow.height = 50f
        actionWindow.width = 200f
        actionWindow.add(actionList)
        actionWindow.pack()
        return actionWindow
    }

    private fun createActionList(): List<Actions> {
        val actionList = List<Actions>(skin)
        actionList.setItems(*Actions.values())
        actionList.selection.required = false
        actionList.selection.multiple = false
        return actionList
    }

    private fun createMessageLabel(): Label {
        val messageLabel = Label("", skin, "default")
        messageLabel.width = 100f
        messageLabel.height = 20f
        messageLabel.setPosition(10f, Gdx.graphics.height - 50f)
        return messageLabel
    }

    private fun createGameStage(): GameStage {
        val mapActor = MapActor()

        val playerLabel = createPlayerLabel()

        val gameStage = GameStage(playerLabel)

        gameStage.camera.position
                .set(Gdx.graphics.width / 2f, Gdx.graphics.height / 2f, 0f)
        gameStage.addActor(mapActor)
        gameStage.addActor(playerLabel)

        return gameStage

    }

    private fun createPlayerLabel(): Label {
        val playerLabel = Label("@", skin, "default")
        playerLabel.width = 16f
        playerLabel.height = 16f
        return playerLabel
    }

    private fun loadData() {
        val json = Json()
        val file = Gdx.files.external(saveFile)
        val save = json.fromJson(GameSave::class.java, file)

        GameData.map = Map(save)
        val playerEntity = createPlayerEntity(save.playerPosition)

        GameData.map.setPlayer(playerEntity)
        GameData.player = playerEntity
        gameStage.updatePlayer()
    }

    private fun createData() {
        val map = Map()
        val startTile = map.startTile
        val startPosition = Vector2(startTile?.rectangle!!.getX(), startTile.rectangle.getY())
        val player = createPlayerEntity(startPosition)
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

    private fun createPlayerEntity(playerPosition: Vector2): Entity {
        log.debug { playerPosition.toString() }

        val body = world.body {
            circle(10f) {
                (this@body.position.set(playerPosition) + vec2(0.25f, 0.25f)) * (Tile.SIZE)
                this@body.type = BodyDef.BodyType.DynamicBody
                filter.categoryBits = PLAYER_BIT
                filter.maskBits = WALL_BIT
            }
        }

        val coneLight = ConeLight(rayHandler, 50, Color.FIREBRICK, 100f, body.position.x,
                body.position.y, body.angle, 90f)
        coneLight.setContactFilter(LIGHT_BIT, NOTHING_BIT, WALL_BIT)
        coneLight.isSoft = true
        coneLight.setSoftnessLength(64f)
        coneLight.attachToBody(body)

        val player = engine.entity {
            with<DirectionComponent>()
            entity.add(PositionComponent(playerPosition, body))
            entity.add(LightComponent(coneLight))
        }
        return player
    }

    private fun createInput() {
        val gameInput = createInputMultiplexer(KeyboardInput(), createMouseInput())
        Gdx.input.inputProcessor = gameInput
    }

    private fun createInputMultiplexer(keyboardInput: KeyboardInput,
                                       mouseInput: MouseInput): InputMultiplexer {
        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(uiStage)
        multiplexer.addProcessor(keyboardInput)
        multiplexer.addProcessor(mouseInput)
        return multiplexer
    }

    private fun createMouseInput() = MouseInput(gameStage, uiStage)

    private fun createDebugStage() = DebugStage(gameStage)

    override fun render(delta: Float) {
        when (state) {
            GameScreen.GameState.RUNNING -> update(delta)
            GameScreen.GameState.PAUSE -> updatePause()
            else -> {
            }
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
        world.step(1 / 60f, 6, 2)
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
        val NOTHING_BIT: Short = 0
        val WALL_BIT: Short = 1
        val PLAYER_BIT = (1 shl 1).toShort()
        val LIGHT_BIT = (1 shl 5).toShort()
    }
}
