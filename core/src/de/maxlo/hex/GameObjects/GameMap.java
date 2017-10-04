package de.maxlo.hex.GameObjects;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.maxlo.hex.Helpers.MapGenerator;

/**
 * Created by max on 21.09.17.
 */

public class GameMap {

    private ObjectMap<Vector3, Hexagon> hexagons;
    private List<Travel> travelList; // contains all current movements of units
    private List<Player> players;

    private Vector3 selectedHexagon;

    public GameMap() {
        hexagons = new ObjectMap<Vector3, Hexagon>();
        players = new ArrayList<Player>();
        travelList = new ArrayList<Travel>();

        test();
    }

    /**
     * update game objects, should be called everytime render is called
     *
     * @param delta - time difference since last update call
     */
    public void update(float delta) {
        for (Travel travel : travelList) {
            travel.update(delta);
        }

        for (Hexagon hexagon : hexagons.values())
            hexagon.getOwner().addUnits(hexagon.update(delta));

    }

    private void test() {

        MapGenerator mapGenerator = new MapGenerator(12, 8, 3, 1);
        players = mapGenerator.getPlayers();
        hexagons = mapGenerator.getMap();
    }

    /**
     * @param hexPos - coordinates of the hexagon
     * @return hexagon corresponding to the given coordinates
     */
    public Hexagon getHexagon(Vector3 hexPos) {
        return hexagons.get(hexPos);
    }

    /**
     * @param hexagon to be set
     * @param hexPos - position of the hexagon
     */
    public void setHexagon(Hexagon hexagon, Vector3 hexPos) {
        // sets a specified hexagon
        hexagons.put(hexPos, hexagon);
    }

    public ObjectMap<Vector3, Hexagon> getHexagons() {
        return hexagons;
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public void selectHexagon(Vector3 pos) {
        if (hexagons.get(pos) == null)
            throw new IllegalArgumentException("Hexagon doesn't exist!");
        selectedHexagon = pos;
    }

    public void unselectHexagon() {
        selectedHexagon = null;
    }

    public Vector3 getSelectedHexagon() {
        return selectedHexagon;
    }
}
