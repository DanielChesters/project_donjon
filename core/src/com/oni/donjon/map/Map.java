package com.oni.donjon.map;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import com.oni.donjon.component.PositionComponent;
import com.oni.donjon.data.GameData;
import com.oni.donjon.data.GameSave;
import com.oni.donjon.generator.CellularAutomataCaveGenerator;
import com.oni.donjon.generator.MapGenerator;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Daniel Chesters (on 22/05/14).
 */
public class Map {
    private int mapHeight;
    private int mapWidth;
    private Set<Tile> tiles;
    private Entity player;

    public Map() {
        this(new CellularAutomataCaveGenerator());
    }

    public Map(MapGenerator generator) {
        generator.generate();
        this.tiles = new HashSet<>();
        for (int x = 0; x < generator.getMapWidth(); x++) {
            for (int y = 0; y < generator.getMapHeight(); y++) {
                tiles.add(new Tile(x, y, generator.getTileTypes()[x][y], false,
                    GameData.INSTANCE.getWorld()));
            }
        }
        this.mapHeight = generator.getMapHeight();
        this.mapWidth = generator.getMapWidth();
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            logMap();
        }
    }

    public Map(GameSave gameSave) {
        this.tiles = new HashSet<>();

        for (int x = 0; x < gameSave.getMapWidth(); x++) {
            for (int y = 0; y < gameSave.getMapHeight(); y++) {
                GameSave.SavedTile savedTile = gameSave.getMap()[x][y];
                if (savedTile != null) {
                    tiles.add(
                        new Tile((float) x, (float) y, savedTile.getType(), savedTile.getKnow(),
                            GameData.INSTANCE.getWorld()));
                }
            }
        }
        this.mapHeight = gameSave.getMapHeight();
        this.mapWidth = gameSave.getMapWidth();
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            logMap();
        }
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    public Optional<Tile> getTile(float x, float y) {
        return tiles.stream()
            .filter(
                t -> BigDecimal.valueOf(t.getRectangle().getX()).compareTo(BigDecimal.valueOf(x))
                    == 0
                    && BigDecimal.valueOf(t.getRectangle().getY()).compareTo(BigDecimal.valueOf(y))
                    == 0)
            .findFirst();
    }

    public Set<Tile> getTiles() {
        return tiles;
    }

    public void updateVisibility() {
        Vector2 position =
            ComponentMapper.getFor(PositionComponent.class).get(player).getPosition();
        for (int x = (int) position.x - 2; x <= (int) position.x + 2; x++) {
            for (int y = (int) position.y - 2; y <= (int) position.y + 2; y++) {
                getTile(x, y).ifPresent(t -> t.setKnow(true));
            }
        }
    }

    public Tile getStartTile() {
        return tiles.stream().filter(t -> t.getType().equals(TileType.STAIR_UP)).findFirst()
            .orElse(null);
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    @Override public String toString() {
        return "Map{" +
            "mapHeight=" + mapHeight +
            ", mapWidth=" + mapWidth +
            ", tiles=" + tiles +
            ", player=" + player +
            '}';
    }

    private void logMap() {
        StringBuilder builder = new StringBuilder("\n");

        for (int y = mapHeight - 1; y >= 0; y--) {
            for (int x = 0; x < mapWidth; x++) {
                getTile(x, y).ifPresent(t -> {
                    if (t.getType() == TileType.WALL) {
                        builder.append('X');
                    } else if (t.getType() == TileType.GROUND) {
                        builder.append('.');
                    } else {
                        builder.append('*');
                    }
                });
            }
            builder.append('\n');
        }
        Gdx.app.debug("Map", builder.toString());
    }
}
