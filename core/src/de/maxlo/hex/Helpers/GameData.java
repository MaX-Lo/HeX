package de.maxlo.hex.Helpers;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.List;
import java.util.Map;

/**
 * stores all data needed to restore a game
 */
public class GameData {

    private List<PlayerData> PlayerDataList;
    private List<HexagonData> hexagonList;

    GameData() {
        // empty constructor needed for deserialization
    }

    GameData(List<PlayerData> PlayerDataList, List<HexagonData> hexagonDataList) {
        this.PlayerDataList = PlayerDataList;
        this.hexagonList = hexagonDataList;
    }

    public List<PlayerData> getPlayerDataList() {
        return PlayerDataList;
    }

    public List<HexagonData> getHexagonList() {
        return hexagonList;
    }
}
