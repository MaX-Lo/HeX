package de.maxlo.hex.GameObjects;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by max on 21.09.17.
 */

public abstract class Hexagon {

    private int units;
    private Player owner;
    private float spawnRate;

    private Texture texture;

    public Hexagon(Player owner) {
        this.owner = owner;
        units = 1;
        spawnRate = 0.5f;
    }

    public Hexagon(Player owner, int units) {
        this.owner = owner;
        this.units = units;
        spawnRate = 0.5f;
    }

    public Hexagon(Player owner, int units, float spawnRate) {
        this.owner = owner;
        this.units = units;
        this.spawnRate = spawnRate;
    }

    public float update(float delta) {
        if (owner.getColor().equals(Player.Color.none))
            return 0.0f;

        return spawnRate *delta;
    }

    public void increase(int units) throws IllegalArgumentException {
        if (units < 0)
            throw new IllegalArgumentException();
        this.units += units;
    }

    public boolean decrease(int units) throws IllegalArgumentException {
        // returns false if unit amount would get less than 1 else true
        if (units < 0)
            throw new IllegalArgumentException();

        if (this.units-units < 1)
            return false;
        else {
            this.units -= units;
            return true;
        }
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) throws IllegalArgumentException {
        if (units < 1)
            throw new IllegalArgumentException();
        this.units = units;
    }

    public abstract void initTexture(Player owner);

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public float getSpawnRate() {
        return spawnRate;
    }

    public void setSpawnRate(float spawnRate) {
        this.spawnRate = spawnRate;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public abstract String toString();
}
