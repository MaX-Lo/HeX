package de.maxlo.hex.GameObjects;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.maxlo.hex.Hex;

/**
 * Created by max on 21.09.17.
 */

public class GameMap {

    private Map<Vector3, Hexagon> hexagons;
    private List<Travel> travelList;
    private List<Player> players;

    public GameMap(Hex game) {
        hexagons = new HashMap<Vector3, Hexagon>();
        players = new ArrayList<Player>();
        travelList = new ArrayList<Travel>();

        test(game);
    }

    public void update(float delta) {
        for (Travel travel : travelList) {
            travel.update(delta);
        }
    }

    public void test(Hex game) {
        players.add(new Player(Player.Color.yellow));
        players.add(new Player(Player.Color.green));
        players.add(new Player(Player.Color.red));

        hexagons.put(new Vector3(0, 2, 0), new NormalHexagon(players.get(0), game.getAssets().yellowHex));
        hexagons.put(new Vector3(0, 0, 0), new NormalHexagon(players.get(1), game.getAssets().greenHex));
        hexagons.put(new Vector3(1, 2, 0), new NormalHexagon(players.get(2), game.getAssets().redHex));
    }

    public void loadMap(String filename) {

    }

    public Hexagon getHexagon(Vector3 coordinates) {
        return hexagons.get(coordinates);
    }

    public boolean setHexagon(Hexagon hexagon, Vector3 coordinates) {
        // sets a specified hexagon returns false if field does not exist
        if (hexagons.get(coordinates) == null)
            return false;
        else {
            hexagons.put(coordinates, hexagon);
            return true;
        }
    }

    public Map<Vector3, Hexagon> getHexagons() {
        return hexagons;
    }
}
