package com.oni.donjon.map;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.oni.donjon.component.PositionComponent;
import com.oni.donjon.data.GameData;
import com.oni.donjon.data.GameSave;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Daniel Chesters (on 22/05/14).
 */
public class Map {
    private Set<Tile> tiles;
    private transient Entity player;

    public Map() {
        this.tiles = new HashSet<>();
    }

    public Map(String mapFile) {
        this.tiles = new HashSet<>();
        Json json = new Json();
        @SuppressWarnings("unchecked")
        Array<Tile> tileArray =
            json.fromJson(Array.class, Tile.class, Gdx.files.internal(mapFile));

        for (Tile t : tileArray) {
            tiles.add(new Tile(t.getRectangle().x, t.getRectangle().y, t.getType(), t.isVisible(),
                GameData.INSTANCE.getWorld()));
        }
    }

    public Map(GameSave gameSave) {
        this.tiles = new HashSet<>();

        for (int x = 1; x <= 20; x++) {
            for (int y = 1; y <= 20; y++) {
                GameSave.SavedTile savedTile = gameSave.getMap()[x][y];
                if (savedTile != null) {
                    tiles.add(new Tile((float) x, (float) y, savedTile.type, savedTile.visible,
                        GameData.INSTANCE.getWorld()));
                }
            }
        }
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    public Optional<Tile> getTile(float x, float y) {
        return tiles.stream()
            .filter(t -> t.getRectangle().getX() == x && t.getRectangle().getY() == y).findFirst();
    }

    public Set<Tile> getTiles() {
        return tiles;
    }

    public void updateVisibility() {
        Vector2 position = ComponentMapper.getFor(PositionComponent.class).get(player).position;
        IntStream
            .rangeClosed((int) position.x - 1, (int) position.x + 1)
            .forEach(x ->
                IntStream.rangeClosed((int) position.y - 1,
                    (int) position.y + 1).forEach(y -> {
                    Optional<Tile> tile = getTile(x, y);
                    if (tile.isPresent()) {
                        tile.get().setVisible(true);
                    }
                })
            );
    }

    public Tile getStartTile() {
        return tiles.stream().filter(t -> t.getType().equals(TileType.STAIR_UP)).findFirst().get();
    }

    @Override public String toString() {
        return "Map{" +
            "tiles=" + tiles +
            '}';
    }
}
