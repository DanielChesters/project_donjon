package com.oni.donjon.generator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.oni.donjon.map.TileType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Chesters (on 11/02/16).
 */
public class DonjonGenerator implements MapGenerator {
    private int nbRooms;
    private int roomMaxSize;
    private int roomMinSize;

    private int mapHeight;
    private int mapWidth;
    private List<Rectangle> rooms = new ArrayList<>();
    private List<Rectangle> tunnels = new ArrayList<>();
    private TileType[][] tileTypes;



    public DonjonGenerator() {
        this(50, 50);
    }

    public DonjonGenerator(int mapHeight, int mapWidth) {
        this(10, 10, 6, mapHeight, mapWidth);
    }

    public DonjonGenerator(int nbRooms, int roomMaxSize, int roomMinSize, int mapHeight,
        int mapWidth) {
        this.nbRooms = nbRooms;
        this.roomMaxSize = roomMaxSize;
        this.roomMinSize = roomMinSize;
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.tileTypes = new TileType[mapWidth][mapHeight];
    }

    @Override
    public TileType[][] getTileTypes() {
        return tileTypes;
    }

    @Override
    public void generate() {
        placeRooms();

        createWalls();

        dig(rooms);
        dig(tunnels);

        placeDoors();

        addSpecialTile(TileType.STAIR_UP);
        addSpecialTile(TileType.STAIR_DOWN);
    }

    @Override
    public int getMapHeight() {
        return mapHeight;
    }

    @Override
    public int getMapWidth() {
        return mapWidth;
    }

    private void placeDoors() {
        for (Rectangle room : rooms) {
            float xMin = room.x - 1;
            float xMax = room.x + room.width;
            float yMin = room.y - 1;
            float yMax = room.y + room.height;

            tunnels.stream().filter(room::overlaps).forEach(t -> {
                if (t.height == 1) {
                    for (int x = (int) t.x; x < (int) t.x + (int) t.width; x++) {
                        if (x == (int) xMin || x == (int) xMax) {
                            tileTypes[x][(int) t.y] = TileType.DOOR_CLOSE;
                            break;
                        }
                    }
                } else if (t.width == 1) {
                    for (int y = (int) t.y; y < (int) t.y + (int) t.height; y++) {
                        if (y == (int) yMin || y == (int) yMax) {
                            tileTypes[(int) t.x][y] = TileType.DOOR_CLOSE;
                            break;
                        }
                    }
                }
            });
        }
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
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                tileTypes[x][y] = TileType.WALL;
            }
        }
    }

    private void placeRooms() {
        for (int i = 0; i < nbRooms; i++) {
            int w = MathUtils.random(roomMinSize, roomMaxSize);
            int h = MathUtils.random(roomMinSize, roomMaxSize);
            int x = MathUtils.random(mapWidth - w - 1) + 1;
            int y = MathUtils.random(mapHeight - h - 1) + 1;

            Rectangle newRoom = new Rectangle(x, y, w, h);
            boolean failed = rooms.stream().anyMatch(newRoom::intersects);

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
