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

    private Vector3 selectedHexagon;

    public GameMap() {
        hexagons = new HashMap<Vector3, Hexagon>();
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

    public void test() {
        addPlayer(new Player());
        addPlayer(new HumanPlayer(Player.Color.yellow));
        addPlayer(new HumanPlayer(Player.Color.green));
        addPlayer(new HumanPlayer(Player.Color.red));
        addPlayer(new HumanPlayer(Player.Color.purple));
        addPlayer(new HumanPlayer(Player.Color.blue));

        hexagons.put(new Vector3(0, 0, 0), new NormalHexagon(players.get(2)));
        hexagons.put(new Vector3(0, 1, 0), new NormalHexagon(players.get(2)));
        hexagons.put(new Vector3(0, 2, 0), new NormalHexagon(players.get(2)));
        hexagons.put(new Vector3(0, 3, 0), new NormalHexagon(players.get(2)));
        hexagons.put(new Vector3(1, 0, 0), new NormalHexagon(players.get(2)));
        hexagons.put(new Vector3(1, 1, 0), new NormalHexagon(players.get(2)));
        hexagons.put(new Vector3(1, 2, 0), new NormalHexagon(players.get(2)));
        hexagons.put(new Vector3(1, 3, 0), new NormalHexagon(players.get(2)));
        hexagons.put(new Vector3(2, 0, 0), new NormalHexagon(players.get(2)));
        hexagons.put(new Vector3(2, 1, 0), new NormalHexagon(players.get(2)));
        hexagons.put(new Vector3(2, 2, 0), new NormalHexagon(players.get(2)));
        hexagons.put(new Vector3(2, 3, 0), new NormalHexagon(players.get(0)));
        hexagons.put(new Vector3(3, 0, 0), new NormalHexagon(players.get(1)));
        hexagons.put(new Vector3(3, 1, 0), new NormalHexagon(players.get(2)));
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

    public void addPlayer(Player newPlayer) {
        for (Player player : players)
            if (player.getColor().equals(newPlayer.getColor()))
                throw new IllegalArgumentException("Player with that color already exists!");

        players.add(newPlayer);
    }

    public Map<Vector3, Hexagon> getHexagons() {
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
