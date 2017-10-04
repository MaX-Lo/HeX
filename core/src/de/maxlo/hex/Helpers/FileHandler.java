package de.maxlo.hex.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.maxlo.hex.GameObjects.Hexagon;
import de.maxlo.hex.GameObjects.Player;

/**
 * Created by max on 02.10.17.
 */

public class FileHandler {

    private  Json json;

    private List<Player> players;
    private ObjectMap<Vector3, Hexagon> hexagons;

    public FileHandler() {
        json = new Json();
    }

    /**
     * Save the current game. Includes players, units, map
     *
     * @param filepath where the game should be saved
     * @param hexagonMap - map of the current game
     * @param players in the current game
     */
    public void saveGame(String filepath, ObjectMap<Vector3, Hexagon> hexagonMap, List<Player> players) {

        // converting wrapping to new Objects containing only necessary data
        // wrapping players to a playerData list
        List<PlayerData> playerDataList = new ArrayList<PlayerData>();
        for (Player player : players) {
            playerDataList.add(new PlayerData(player));
        }

        // wrapping the hexagons with position to a hexagonData list
        List<HexagonData> hexagonDataList = new ArrayList<HexagonData>();
        for (Vector3 pos : hexagonMap.keys()) {

            // the hexagonData object contains a playerData reference which hast to be figured out at first
            // currently that's done by comparing their color
            PlayerData owner = null;
            for (PlayerData playerData : playerDataList) {
                if (hexagonMap.get(pos).getOwner().getColor().equals(playerData.getColor())) {
                    owner = playerData;
                }
            }
            hexagonDataList.add(new HexagonData(hexagonMap.get(pos), owner, pos));
        }

        // object containing all data to load a game from storage
        GameData GameData = new GameData(playerDataList, hexagonDataList);

        // actually saving the game state
        String gameString = json.toJson(GameData);
        FileHandle file = Gdx.files.local(filepath);
        file.writeString(gameString, false);
        Gdx.app.log("Ok", "Saved map successfully");
    }

    /**
     * load a game from the given filepath
     *
     * @param filepath where the game is located
     */
    public boolean loadGame(String filepath) {
        String gameString;
        try {
            FileHandle file = Gdx.files.local(filepath);
            gameString = file.readString();
        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.error("FileError", "File could not be found or isn't readable!");
            return false;
        }

        players = new ArrayList<Player>();
        hexagons = new ObjectMap<Vector3, Hexagon>();

        try {
            GameData gameData = json.fromJson(GameData.class, gameString);

            List<PlayerData> playerDataList = gameData.getPlayerDataList();
            List<HexagonData> hexagonDataList = gameData.getHexagonList();

            // load players
            for (PlayerData playerData : playerDataList) {
                Class cl = Class.forName(playerData.getType());
                java.lang.reflect.Constructor constructor = cl.getConstructor(new Class[]{Player.Color.class});
                Object invoker = constructor.newInstance(playerData.getColor());
                players.add((Player)invoker);
            }

            // load hexagon map
            for (HexagonData hexagonData : hexagonDataList) {
                Class cl = Class.forName(hexagonData.getType());
                java.lang.reflect.Constructor constructor = cl.getConstructor(new Class[]{Player.class, Integer.class, Float.class});

                // figure out which player in the list is the right one by comparing their color
                Player owner = null;
                for (Player player : players) {
                    if (hexagonData.getOwner().getColor().equals(player.getColor())) {
                        owner = player;
                        break;
                    }
                }
                int units = hexagonData.getUnits();
                float spawnrate = hexagonData.getSpawnRate();
                Vector3 pos = new Vector3(hexagonData.getX(), hexagonData.getY(), 0);
                // reconstruct the hexagon object
                Hexagon recoveredHexagon = (Hexagon)constructor.newInstance(owner, units, spawnrate);
                hexagons.put(pos, recoveredHexagon);
            }

            Gdx.app.log("Ok", "Loaded map successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public ObjectMap<Vector3, Hexagon> getHexagonMap() {
        return hexagons;
    }
}
