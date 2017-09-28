package de.maxlo.hex.GameObjects;

/**
 * Created by max on 21.09.17.
 */

public class Player {

    private enum Color {none, red, blue, green, yellow}

    private Color color;

    public Player() {
        color = Color.none;
    }

    private Color getColor() {
        return color;
    }
}
