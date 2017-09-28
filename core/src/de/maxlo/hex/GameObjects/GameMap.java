package de.maxlo.hex.GameObjects;

import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by max on 21.09.17.
 */

public class GameMap {

    private Map<Vector3, Hexagon> hexagons;
    private List<Travel> travelList;

    public GameMap() {
        hexagons = new HashMap<Vector3, Hexagon>();
    }

    public void test() {
    }

    public void loadMap(String filename) {

    }

    public Hexagon getHexagon(Vector3 coordinates) {ß
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
}
