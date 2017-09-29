package de.maxlo.hex.GameObjects;

/**
 * Created by max on 21.09.17.
 */

public class Player {

    private static final int START_UNITS = 10;

    public enum Color {none, red, blue, green, yellow, purple}

    private Color color;
    private float remainingUnits;

    public Player() {
        color = Color.none;
        remainingUnits = START_UNITS;
    }

    public Player(Color color) {
        this.color = color;
        remainingUnits = START_UNITS;
    }

    public void addUnits(float units) {
        remainingUnits += units;
    }

    public void removeUnits(float units) {
        if (remainingUnits - units < 1)
            throw new IllegalArgumentException("It has to be at least one unit left!");

        remainingUnits -= units;
    }

    public Color getColor() {
        return color;
    }

    public float getRemainingUnits() {
        return remainingUnits;
    }

    public void setRemainingUnits(float remainingUnits) {
        this.remainingUnits = remainingUnits;
    }
}
