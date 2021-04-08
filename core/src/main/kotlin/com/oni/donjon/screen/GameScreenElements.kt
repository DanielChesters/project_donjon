package com.oni.donjon.screen

import box2dLight.ConeLight
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.I18NBundle
import com.oni.donjon.DonjonGame
import com.oni.donjon.action.Actions
import com.oni.donjon.actor.ListActions
import com.oni.donjon.actor.MapActor
import com.oni.donjon.actor.MenuGameWindow
import com.oni.donjon.actor.SaveWindow
import com.oni.donjon.component.DirectionComponent
import com.oni.donjon.component.LightComponent
import com.oni.donjon.component.PositionComponent
import com.oni.donjon.input.KeyboardInput
import com.oni.donjon.input.MouseInput
import com.oni.donjon.map.Tile
import com.oni.donjon.stage.GameStage
import com.oni.donjon.stage.UIStage
import ktx.ashley.entity
import ktx.ashley.with
import ktx.box2d.body
import ktx.box2d.circle
import ktx.log.debug
import ktx.math.plus
import ktx.math.times
import ktx.math.vec2

class GameScreenElements(private val game: DonjonGame, private val gameScreen: GameScreen) {
    private val skin: Skin = game.context.inject()
    private val bundle: I18NBundle = game.context.inject()

    fun createUi(): UIStage {
        val messageLabel = createMessageLabel()
        val actionList = createActionList()
        val actionWindow = createActionWindow(actionList)
        val saveWindow = SaveWindow(bundle["window.save.title"], skin, game)
        val menuWindow = MenuGameWindow(saveWindow, game, gameScreen)
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
            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                menuWindow.setVisible(true)
                gameScreen.state = GameScreen.GameState.PAUSE
                return true
            }
        })
        menuButton.pack()
        menuButton.setPosition(
            Gdx.graphics.width - (menuButton.width + MENU_BUTTON_MARGIN),
            Gdx.graphics.height - (menuButton.height + MENU_BUTTON_MARGIN)
        )
        return menuButton
    }

    private fun createActionWindow(actionList: List<Actions>): Window {
        val actionWindow = Window(bundle["window.action.title"], skin)
        actionWindow.setPosition(ACTION_WINDOW_POSITION_X, Gdx.graphics.height / 2f)
        actionWindow.height = ACTION_WINDOW_HEIGHT
        actionWindow.width = ACTION_WINDOW_WIDTH
        actionWindow.add(actionList)
        actionWindow.pack()
        return actionWindow
    }

    private fun createActionList(): List<Actions> {
        val actionList = ListActions(skin, bundle)
        actionList.setItems(Actions.LOOK, Actions.OPEN, Actions.CLOSE)
        actionList.selection.required = false
        actionList.selection.multiple = false
        return actionList
    }

    private fun createMessageLabel(): Label {
        val messageLabel = Label("", skin, "default")
        messageLabel.width = MESSAGE_WIDTH
        messageLabel.height = MESSAGE_HEIGHT
        messageLabel.setPosition(MESSAGE_X, Gdx.graphics.height - MESSAGE_Y)
        return messageLabel
    }

    fun createGameStage(): GameStage {
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
        playerLabel.width = PLAYER_WIDTH
        playerLabel.height = PLAYER_HEIGHT
        return playerLabel
    }

    fun createPlayerEntity(playerPosition: Vector2): Entity {
        GameScreen.log.debug { playerPosition.toString() }

        val body = gameScreen.world.body {
            circle(PLAYER_RADIUS) {
                (this@body.position.set(playerPosition) + vec2(PLAYER_CENTER, PLAYER_CENTER)) * (Tile.SIZE)
                this@body.type = BodyDef.BodyType.DynamicBody
                filter.categoryBits = GameScreen.PLAYER_BIT
                filter.maskBits = GameScreen.WALL_BIT
            }
        }

        val coneLight = ConeLight(
            gameScreen.rayHandler, DEFAULT_CONE_RAYS,
            Color.FIREBRICK, DEFAULT_CONE_DISTANCE, body.position.x,
            body.position.y, body.angle, DEFAULT_CONE_DEGREE
        )
        coneLight.setContactFilter(GameScreen.LIGHT_BIT, GameScreen.NOTHING_BIT, GameScreen.WALL_BIT)
        coneLight.isSoft = true
        coneLight.setSoftnessLength(DEFAULT_SOFTNESS_LENGTH)
        coneLight.attachToBody(body)

        return gameScreen.engine.entity {
            with<DirectionComponent>()
            entity.add(PositionComponent(playerPosition, body))
            entity.add(LightComponent(coneLight))
        }
    }

    fun createInput() {
        Gdx.input.inputProcessor = createInputMultiplexer(
            KeyboardInput(),
            MouseInput(gameScreen.gameStage, gameScreen.uiStage, bundle)
        )
    }

    private fun createInputMultiplexer(
        keyboardInput: KeyboardInput,
        mouseInput: MouseInput
    ): InputMultiplexer {
        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(gameScreen.uiStage)
        multiplexer.addProcessor(keyboardInput)
        multiplexer.addProcessor(mouseInput)
        return multiplexer
    }

    companion object {
        const val DEFAULT_SOFTNESS_LENGTH = 64f
        const val DEFAULT_CONE_RAYS = 50
        const val DEFAULT_CONE_DEGREE = 90f
        const val DEFAULT_CONE_DISTANCE = 100f
        const val PLAYER_WIDTH = 16f
        const val PLAYER_HEIGHT = 16f
        const val PLAYER_RADIUS = 10f
        const val MESSAGE_WIDTH = 100f
        const val MESSAGE_HEIGHT = 20f
        const val ACTION_WINDOW_WIDTH = 200f
        const val ACTION_WINDOW_HEIGHT = 50f
        const val ACTION_WINDOW_POSITION_X = 20f
        const val MESSAGE_X = 10f
        const val MESSAGE_Y = 50f
        const val MENU_BUTTON_MARGIN = 5
        const val PLAYER_CENTER = 0.25f
    }
}
