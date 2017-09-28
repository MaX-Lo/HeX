package de.maxlo.hex.GameObjects;

/**
 * Created by max on 21.09.17.
 */

public class Player {

    public enum Color {none, red, blue, green, yellow}

    private Color color;

    public Player() {
        color = Color.none;
    }

    public Player(Color color) {
        this.color = color;
    }

    private Color getColor() {
        return color;
    }
}
