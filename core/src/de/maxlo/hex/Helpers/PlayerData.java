package de.maxlo.hex.Helpers;

/**
 * Created by max on 04.10.17.
 */

import de.maxlo.hex.GameObjects.Player;

/**
 * stores relevant data needed to restore a full player object
 */
public class PlayerData {

    private Player.Color color;
    private String type;

    PlayerData() {
        // empty constructor needed for deserialization
    }

    PlayerData(Player player) {
        color = player.getColor();
        type = player.toString();
    }

    public Player.Color getColor() {
        return color;
    }

    public String getType() {
        return type;
    }
}
