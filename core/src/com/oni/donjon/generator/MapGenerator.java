package com.oni.donjon.generator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.oni.donjon.map.TileType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Chesters (on 11/02/16).
 */
public class MapGenerator {
    public final static int MAX_ROOMS = 10;
    public final static int ROOM_MAX_SIZE = 10;
    public final static int ROOM_MIN_SIZE = 6;
    public final static int MAP_HEIGHT = 50;
    public final static int MAP_WIDTH = 50;

    private List<Rectangle> rooms = new ArrayList<>();
    private List<Rectangle> tunnels = new ArrayList<>();
    private TileType[][] tileTypes = new TileType[MAP_WIDTH][MAP_HEIGHT];

    public TileType[][] getTileTypes() {
        return tileTypes;
    }

    public void generate() {
        placeRooms();

        createWalls();

        dig(rooms);
        dig(tunnels);

        addSpecialTile(TileType.STAIR_UP);
        addSpecialTile(TileType.STAIR_DOWN);
    }

    private void dig(List<Rectangle> rectangles) {
        for (Rectangle rectangle : rectangles) {
            Gdx.app.debug("Dig rect", rectangle.toString());
            for (int x = (int) rectangle.x; x < rectangle.x + rectangle.width; x++) {
                for (int y = (int) rectangle.y; y < rectangle.y + rectangle.height; y++) {
                    tileTypes[x][y] = TileType.GROUND;
                }
            }
        }
    }

    private void addSpecialTile(TileType tileType) {
        Rectangle startRoom = rooms.get(MathUtils.random(rooms.size() - 1));
        int x = MathUtils.random((int) startRoom.x, (int) startRoom.x + (int) startRoom.width - 1);
        int y = MathUtils.random((int) startRoom.y, (int) startRoom.y + (int) startRoom.height - 1);
        tileTypes[x][y] = tileType;
    }

    private void createWalls() {
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                tileTypes[x][y] = TileType.WALL;
            }
        }
    }

    private void placeRooms() {
        for (int i = 0; i < MAX_ROOMS; i++) {
            int w = MathUtils.random(ROOM_MIN_SIZE, ROOM_MAX_SIZE);
            int h = MathUtils.random(ROOM_MIN_SIZE, ROOM_MAX_SIZE);
            int x = MathUtils.random(MAP_WIDTH - w - 1) + 1;
            int y = MathUtils.random(MAP_HEIGHT - h - 1) + 1;

            Rectangle newRoom = new Rectangle(x, y, w, h);
            boolean failed = false;

            for (Rectangle otherRoom : rooms) {
                if (newRoom.overlaps(otherRoom)) {
                    failed = true;
                    break;
                }
            }
            if (!failed) {
                if (!rooms.isEmpty()) {
                    placeTunnels(newRoom);
                }
                rooms.add(newRoom);
            }
        }
    }

    private void placeTunnels(Rectangle newRoom) {
        Vector2 newCenter = newRoom.getCenter(new Vector2());
        Vector2 prevCenter = rooms.get(rooms.size() - 1).getCenter(new Vector2());

        Rectangle newHTunnel;
        Rectangle newVTunnel;
        if (MathUtils.random(1) == 1) {
            newHTunnel = placeHTunnel((int) prevCenter.x, (int) newCenter.x, (int) prevCenter.y);
            newVTunnel = placeVTunnel((int) newCenter.x, (int) prevCenter.y, (int) newCenter.y);
        } else {
            newVTunnel = placeVTunnel((int) prevCenter.x, (int) newCenter.y, (int) prevCenter.y);
            newHTunnel = placeHTunnel((int) prevCenter.x, (int) newCenter.x, (int) newCenter.y);
        }

        tunnels.add(newHTunnel);
        tunnels.add(newVTunnel);
    }

    private Rectangle placeHTunnel(int x1, int x2, int y) {
        int wight = Math.abs(x1 - x2) + 1;
        return new Rectangle(Math.min(x1, x2), y, wight, 1);
    }

    private Rectangle placeVTunnel(int x, int y1, int y2) {
        int height = Math.abs(y1 - y2) + 1;
        return new Rectangle(x, Math.min(y1, y2), 1, height);
    }
}