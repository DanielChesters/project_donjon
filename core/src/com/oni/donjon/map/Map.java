package com.oni.donjon.map;

import com.badlogic.gdx.math.Circle;
import com.oni.donjon.entity.Character;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        Circle circle = new Circle((int) character.getPosition().x, (int) character.getPosition().y, 6);
        tiles.stream().forEach(t -> t.setVisible(false));
        Set<Tile> tilesAroundCharacter = tiles.stream().filter(t ->
                circle.contains(t.getRectangle().getX(), t.getRectangle().getY())).collect(Collectors.toSet());

        tilesAroundCharacter.forEach(t -> t.setVisible(true));
        //Set<Tile> tilesBlock = tilesAroundCharacter.stream().filter(t -> t.getType().isBlock()).collect(Collectors.toSet());
    }
}
