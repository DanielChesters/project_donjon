package com.oni.donjon.map;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.oni.donjon.component.PositionComponent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Daniel Chesters (on 22/05/14).
 */
public class Map implements Json.Serializable {
    private Set<Tile> tiles;
    private transient Entity player;

    public Map() {
        this.tiles = new HashSet<>();
    }

    public Map(String mapFile, World world) {
        this.tiles = new HashSet<>();
        Json json = new Json();
        @SuppressWarnings("unchecked")
        Array<Tile> tileArray =
            json.fromJson(Array.class, Tile.class, Gdx.files.internal(mapFile));

        for (Tile t : tileArray) {
            tiles.add(new Tile(t.getRectangle().x, t.getRectangle().y, t.getType(), t.isVisible(),
                world));
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

    @Override public void write(Json json) {
        json.writeArrayStart("tiles");
        tiles.forEach(t -> json.writeValue(t, Tile.class));
        json.writeArrayEnd();
    }

    @Override public void read(Json json, JsonValue jsonData) {
        tiles = new HashSet<>();
        @SuppressWarnings("unchecked")
        Array<Tile> array = json.readValue("tiles", Array.class, Tile.class, jsonData);
        for (Tile tile : array) {
            tiles.add(tile);
        }
    }

    @Override public String toString() {
        return "Map{" +
            "tiles=" + tiles +
            '}';
    }
}
