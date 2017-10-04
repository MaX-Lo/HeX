package de.maxlo.hex.GameObjects;

/**
 * Created by max on 02.10.17.
 */

public class NeutralPlayer extends Player {

    public NeutralPlayer() {
        super(Color.none);
    }

    public NeutralPlayer(Color color) {
        super(color);
    }

    @Override
    public String toString() {
        return "de.maxlo.hex.GameObjects.NeutralPlayer";
    }
}
