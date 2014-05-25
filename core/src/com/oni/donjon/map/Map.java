package com.oni.donjon.map;

import com.oni.donjon.entity.Character;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Daniel Chesters (on 22/05/14).
 */
public class Map {
    private Set<Tile> tiles;

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    private Character character;

    public Optional<Tile> getTile(float x, float y) {
        return tiles.stream().filter(t -> t.getRectangle().getX() == x && t.getRectangle().getY() == y).findFirst();
    }

    public Map() {
        this.tiles = new HashSet<>();
    }

    public Set<Tile> getTiles() {
        return tiles;
    }

    public void updateVisibility() {
        IntStream.rangeClosed((int) character.getPosition().x - 1, (int) character.getPosition().x + 1).forEach(x ->
                        IntStream.rangeClosed((int) character.getPosition().y - 1, (int) character.getPosition().y + 1).forEach(y -> {
                            Optional<Tile> tile = getTile(x, y);
                            if (tile.isPresent()) {
                                tile.get().setVisible(true);
                            }
                        })
        );
    }
}
