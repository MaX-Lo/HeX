package de.maxlo.hex.GameObjects;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by max on 21.09.17.
 */

public abstract class Hexagon {

    private int units;
    private Player owner;
    private float spawn_rate;

    private Texture texture;

    public Hexagon(Player owner, Texture texture) {
        this.owner = owner;
        this.texture = texture;
        units = 1;
        spawn_rate = 1.0f;
    }

    public Hexagon(Player owner, Texture texture, int units, float spawn_rate) {
        this.owner = owner;
        this.texture = texture;
        this.units = units;
        this.spawn_rate = spawn_rate;
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

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public float getSpawn_rate() {
        return spawn_rate;
    }

    public void setSpawn_rate(float spawn_rate) {
        this.spawn_rate = spawn_rate;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

}
