package de.maxlo.hex.Helpers;

/**
 * Created by max on 04.10.17.
 */

import com.badlogic.gdx.math.Vector3;

import java.io.Serializable;

import de.maxlo.hex.GameObjects.Hexagon;

/**
 * The hexagon object rewritten for saving
 * texture has to be reconstructed from owner color
 */
public class HexagonData {

    private int units;
    private PlayerData owner;
    private float spawnRate;
    private String type;
    private int x;
    private int y;

    HexagonData() {
        // empty constructor needed for deserialization
    }

    HexagonData(Hexagon hexagon, PlayerData owner, Vector3 pos) {
        units = hexagon.getUnits();
        spawnRate = hexagon.getSpawnRate();
        type = hexagon.toString();
        this.owner = owner;
        x = (int)pos.x;
        y = (int)pos.y;
    }

    public int getUnits() {
        return units;
    }

    public PlayerData getOwner() {
        return owner;
    }

    public float getSpawnRate() {
        return spawnRate;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}