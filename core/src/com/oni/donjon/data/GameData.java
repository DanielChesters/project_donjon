package com.oni.donjon.data;

import com.oni.donjon.entity.Player;
import com.oni.donjon.map.Map;

/**
 * @author Daniel Chesters (on 01/06/14).
 */
public class GameData {
    private Map map;
    private Player player;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
