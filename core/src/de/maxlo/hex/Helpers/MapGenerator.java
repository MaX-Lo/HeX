package de.maxlo.hex.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.maxlo.hex.GameObjects.AIPlayer;
import de.maxlo.hex.GameObjects.Hexagon;
import de.maxlo.hex.GameObjects.HumanPlayer;
import de.maxlo.hex.GameObjects.NeutralPlayer;
import de.maxlo.hex.GameObjects.NormalHexagon;
import de.maxlo.hex.GameObjects.Player;

/**
 * Created by max on 02.10.17.
 */

public class MapGenerator {

    // basic rectangle where Hexagons should be generated
    private int width;
    private int height;

    private Map<Vector3, Hexagon> map;
    private List<Player> players;
    private Player neutralPlayer;

    public MapGenerator(int width, int height, int humanPlayer, int aiPlayer) {
        if (width < 8 || height < 8)
            throw new IllegalArgumentException("Width and height has to be greater or equals to 8");
        if (humanPlayer + aiPlayer > 5)
            throw new IllegalArgumentException("Currently are only games with 5 or less player supported");
        if (humanPlayer + aiPlayer < 2)
            throw new IllegalArgumentException("A Game has to have at least two player");

        this.width = width;
        this.height = height;

        map = new HashMap<Vector3, Hexagon>();
        players = new ArrayList<Player>();

        generatePlayers(humanPlayer, aiPlayer);
        generateRawMap();
        refineMap1();
        locatePlayers();
    }


    /**
     * generate human and ai player for this map
     *
     * @param human - number of human players
     * @param ai - number of ai players
     */
    public void generatePlayers(int human, int ai) {
        int count = 1;

        // neutral player
        players.add(new NeutralPlayer());
        neutralPlayer = players.get(0);

        // ai players
        for (int i=0; i<ai; i++) {
            addPlayer(new AIPlayer(Player.Color.values()[count]));
            count++;
        }

        // human players
        for (int i=0; i<human; i++) {
            addPlayer(new HumanPlayer(Player.Color.values()[count]));
            count++;
        }
    }

    /**
     * Adds a new player. There may be no two player with the same color.
     * New players should be added via this method, because it checks for unique colors
     *
     * @param newPlayer that should be added
     */
    private void addPlayer(Player newPlayer) {
        for (Player player : players)
            if (player.getColor().equals(newPlayer.getColor()))
                throw new IllegalArgumentException("Player with that color already exists!");

        players.add(newPlayer);
    }

    /**
     * generate rectangular map filled all with neutral Hexagons
     */
    private void generateRawMap() {
        Random random = new Random();

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
               map.put(new Vector3(x, y + (x/2), 0), new NormalHexagon(neutralPlayer, random.nextInt(5)+1));
            }
        }
    }

    /**
     * Method 1 for map generation
     *
     * Basic principle:
     *  1) make random holes
     *  2) neighbours of created holes get deleted with a certain probability
     */
    private void refineMap1() {
        int numberOfHexagons = map.size();
        int numberOfRandomHoles = numberOfHexagons/4;

        Random random = new Random();
        List<Vector3> removed = new ArrayList<Vector3>();
        for (int i=0; i<numberOfRandomHoles; i++) {
            Vector3 hexToRemove = getRandomMapPos();
            map.remove(hexToRemove);
            removed.add(new Vector3(hexToRemove));
        }

        Gdx.app.log("Map", "generation");
        Gdx.app.log("After first removing: ", String.valueOf(map.size()));

        for (int i=0; i<removed.size(); i++) {
            Vector3 pos = removed.remove(0);
            List<Vector3> neighbours = getNeighbours(pos);
            int neighbourCount = neighbours.size();
            float removeLikelihood = neighbourCount * neighbourCount / 3; // likelihood decreases quadratic with holes around
            if (random.nextInt(100) < removeLikelihood) {
                Vector3 toRemove = neighbours.get(random.nextInt(neighbourCount));
                map.remove(toRemove);
                removed.add(toRemove);
            }

        }
        Gdx.app.log("After second removing: ", String.valueOf(map.size()));
    }

    /**
     * give every player a start position
     */
    private void locatePlayers() {
        for (Player player : players) {
            map.put(getRandomMapPos(), new NormalHexagon(player, 8));
        }
    }

    /**
     * Get a random tile on the map, excluding holes
     *
     * @return a map position
     */
    private Vector3 getRandomMapPos() {
        if (map.size() < 1)
            return null;

        Random random = new Random();
        Vector3 pos = new Vector3(0, 0, 0);

        boolean found = false;
        while (!found) {
            pos.x = random.nextInt(width);
            pos.y = random.nextInt(height) + (int)(pos.x/2); // addition is needed for the offset
            Gdx.app.log("before check:", String.valueOf(pos.x) + " " + String.valueOf(pos.y));
            if (map.get(pos) != null) {
                found = true;
                }
            Gdx.app.log("check:", String.valueOf(found));
        }
        return pos;
    }


    /**
     * @param pos their neighbours should be returned
     * @return neighbours of pos
     */
    private List<Vector3> getNeighbours(Vector3 pos) {
        List<Vector3> neighbours = new ArrayList<Vector3>();

        neighbours.add(new Vector3(pos.x, pos.y+1, 0));
        neighbours.add(new Vector3(pos.x+1, pos.y+1, 0));
        neighbours.add(new Vector3(pos.x+1, pos.y, 0));
        neighbours.add(new Vector3(pos.x, pos.y-1, 0));
        neighbours.add(new Vector3(pos.x-1, pos.y-1, 0));
        neighbours.add(new Vector3(pos.x-1, pos.y, 0));

        return neighbours;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map<Vector3, Hexagon> getMap() {
        return map;
    }
}
