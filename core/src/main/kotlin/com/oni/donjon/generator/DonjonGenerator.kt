package com.oni.donjon.generator

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.oni.donjon.map.TileType
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.min

/**
 * @author Daniel Chesters (on 11/02/16).
 */
class DonjonGenerator(
    private val nbRooms: Int,
    private val roomMaxSize: Int,
    private val roomMinSize: Int,
    mapHeight: Int,
    mapWidth: Int
) : MapGenerator(mapHeight, mapWidth) {

    private val rooms = ArrayList<Rectangle>()
    private val tunnels = ArrayList<Rectangle>()

    @JvmOverloads
    constructor(mapHeight: Int = DEFAULT_HEIGHT, mapWidth: Int = DEFAULT_WIDTH) :
        this(
            DEFAULT_NB_ROOM, DEFAULT_ROOM_MAX_SIZE, DEFAULT_ROOM_MIN_SIZE, mapHeight,
            mapWidth
        )

    init {
        this.tileTypes = Array(mapWidth) { arrayOfNulls(mapHeight) }
    }

    override fun generate() {
        placeRooms()

        createWalls()

        dig(rooms)
        dig(tunnels)

        placeDoors()

        placeSpecialTile(TileType.STAIR_UP)
        placeSpecialTile(TileType.STAIR_DOWN)
    }

    private fun placeDoors() {
        for (room in rooms) {
            val xMin = room.x - 1
            val xMax = room.x + room.width
            val yMin = room.y - 1
            val yMax = room.y + room.height

            tunnels.filter { room.overlaps(it) }.forEach { t ->
                if (BigDecimal.valueOf(t.height.toDouble()).compareTo(BigDecimal.ONE) == 0) {
                    placeDoorHeight(t, xMin.toInt(), xMax.toInt())
                } else if (BigDecimal.valueOf(t.width.toDouble()).compareTo(BigDecimal.ONE) == 0) {
                    placeDoorWidth(t, yMin.toInt(), yMax.toInt())
                }
            }
        }
    }

    private fun placeDoorWidth(t: Rectangle, yMin: Int, yMax: Int) {
        for (y in t.y.toInt() until t.y.toInt() + t.height.toInt()) {
            if (y == yMin || y == yMax) {
                tileTypes[t.x.toInt()][y] = TileType.DOOR_CLOSE
                break
            }
        }
    }

    private fun placeDoorHeight(t: Rectangle, xMin: Int, xMax: Int) {
        for (x in t.x.toInt() until t.x.toInt() + t.width.toInt()) {
            if (x == xMin || x == xMax) {
                tileTypes[x][t.y.toInt()] = TileType.DOOR_CLOSE
                break
            }
        }
    }

    private fun dig(rectangles: List<Rectangle>) {
        for (rectangle in rectangles) {
            var x = rectangle.x.toInt()
            while (x < rectangle.x + rectangle.width) {
                var y = rectangle.y.toInt()
                while (y < rectangle.y + rectangle.height) {
                    tileTypes[x][y] = TileType.GROUND
                    y++
                }
                x++
            }
        }
    }

    private fun createWalls() {
        for (x in 0 until mapWidth) {
            for (y in 0 until mapHeight) {
                tileTypes[x][y] = TileType.WALL
            }
        }
    }

    private fun placeRooms() {
        (0 until nbRooms).forEach { _ ->
            val w = MathUtils.random(roomMinSize, roomMaxSize)
            val h = MathUtils.random(roomMinSize, roomMaxSize)
            val x = MathUtils.random(mapWidth - w - 1) + 1
            val y = MathUtils.random(mapHeight - h - 1) + 1

            val newRoom = Rectangle(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat())
            val failed = rooms.stream().anyMatch { room -> room.intersects(newRoom) }

            if (!failed) {
                if (rooms.isNotEmpty()) {
                    placeTunnels(newRoom)
                }
                rooms.add(newRoom)
            }
        }
    }

    private fun placeTunnels(newRoom: Rectangle) {
        val newCenter = newRoom.getCenter(Vector2())
        val prevCenter = rooms[rooms.size - 1].getCenter(Vector2())

        val newHTunnel: Rectangle
        val newVTunnel: Rectangle
        if (MathUtils.random(1) == 1) {
            newHTunnel = placeHTunnel(prevCenter.x.toInt(), newCenter.x.toInt(), prevCenter.y.toInt())
            newVTunnel = placeVTunnel(newCenter.x.toInt(), prevCenter.y.toInt(), newCenter.y.toInt())
        } else {
            newVTunnel = placeVTunnel(prevCenter.x.toInt(), newCenter.y.toInt(), prevCenter.y.toInt())
            newHTunnel = placeHTunnel(prevCenter.x.toInt(), newCenter.x.toInt(), newCenter.y.toInt())
        }

        tunnels.add(newHTunnel)
        tunnels.add(newVTunnel)
    }

    private fun placeHTunnel(x1: Int, x2: Int, y: Int): Rectangle {
        val wight = abs(x1 - x2) + 1
        return Rectangle(min(x1, x2).toFloat(), y.toFloat(), wight.toFloat(), 1f)
    }

    private fun placeVTunnel(x: Int, y1: Int, y2: Int): Rectangle {
        val height = abs(y1 - y2) + 1
        return Rectangle(x.toFloat(), min(y1, y2).toFloat(), 1f, height.toFloat())
    }

    companion object {
        const val DEFAULT_HEIGHT = 50
        const val DEFAULT_WIDTH = 50
        const val DEFAULT_NB_ROOM = 10
        const val DEFAULT_ROOM_MAX_SIZE = 10
        const val DEFAULT_ROOM_MIN_SIZE = 6
    }
}
