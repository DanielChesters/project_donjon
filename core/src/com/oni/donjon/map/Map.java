package com.oni.donjon.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.oni.donjon.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Daniel Chesters (on 22/05/14).
 */
public class Map {
    private Set<Tile> tiles;
    private Player player;

    public Map() {
        this.tiles = new HashSet<>();
        Json json = new Json();
        @SuppressWarnings("unchecked")
        Array<Tile> tileArray =
            json.fromJson(Array.class, Tile.class, Gdx.files.internal("map/map.json"));
        for (Tile t : tileArray) {
            tiles.add(t);
        }
    }

    public void setPlayer(Player player) {
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
        IntStream
            .rangeClosed((int) player.getPosition().x - 1, (int) player.getPosition().x + 1)
            .forEach(x ->
                    IntStream.rangeClosed((int) player.getPosition().y - 1,
                        (int) player.getPosition().y + 1).forEach(y -> {
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
}
