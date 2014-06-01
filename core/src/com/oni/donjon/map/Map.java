package com.oni.donjon.map;

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
        IntStream.rangeClosed(0, 20).forEach(x -> IntStream.rangeClosed(0, 20).forEach(y -> {
            if (x == 6 && y == 4) {
                tiles.add(new Tile(x, y, TileType.STAIR_UP, false));
            } else if (isGroundTile(x, y)) {
                tiles.add(new Tile(x, y, TileType.GROUND, false));
            } else if (x == 10 && y == 10) {
                tiles.add(new Tile(x, y, TileType.DOOR_CLOSE, false));
            } else {
                tiles.add(new Tile(x, y, TileType.WALL, false));
            }
        }));
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Optional<Tile> getTile(float x, float y) {
        return tiles.stream()
            .filter(t -> t.getRectangle().getX() == x && t.getRectangle().getY() == y).findFirst();
    }

    private boolean isGroundTile(int x, int y) {
        return columnGround(x) && rowGround(y) && isNotInnerWall(x);
    }

    private boolean isNotInnerWall(int x) {
        return x != 10;
    }

    private boolean rowGround(int y) {
        return y > 1 && y < 19;
    }

    private boolean columnGround(int x) {
        return x > 1 && x < 19;
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
}
