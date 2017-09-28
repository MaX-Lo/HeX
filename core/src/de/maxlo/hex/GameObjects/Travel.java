package de.maxlo.hex.GameObjects;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.List;

/**
 * Created by max on 27.09.17.
 */

public class Travel {
    private static final float SPEED = 1.0f; // Todo export to config file

    private Vector3 start;
    private Vector3 destination;
    private Player player;
    private int units;

    private float progress; // float between 0 and 100
    private List<Vector3> wayPoints;

    public Travel(Vector3 start, Vector3 destination) {
        this.start = start;
        this.destination = destination;
        progress = 0.0f;
    }

    public boolean start(GameMap map, Player player) {
        GameMap ownerMap = constructOwnerMap(map, player);

        wayPoints = calculateRoute(ownerMap);
        if (wayPoints != null)
            return true;
        else
            return false;
    }

    public void cancel() {

    }

    public boolean update(float delta) {
        // if progress reached 100% return true else false
        progress += SPEED * delta;
        if (progress >= 100.0f) {
            progress = 0.0f;
            // Todo start auf nächsten punkt setzen, gucken ob Ziel erreicht wurde
            return true;
        } else
            return false;
    }

    /**
     * returns null if no route exists
     */
    private List<Vector3> calculateRoute(GameMap map) {
        // Todo AI Algorithm
        return null;
    }

    private GameMap constructOwnerMap(GameMap map, Player owner) {
        // returns a new map with owner hexagon fields only
        // Todo
        return null;
    }
}
