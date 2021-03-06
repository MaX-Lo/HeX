package de.maxlo.hex.GameObjects;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 27.09.17.
 */

public class Travel {
    private static final float SPEED = 1.0f; // Todo export to config file

    private Vector3 start;
    private Vector3 destination;
    private Player player;
    private int units;
    private RouteCalculator routeCalculator;

    private float progress; // float between 0 and 100
    private List<Vector3> wayPoints;

    public Travel(Vector3 start, Vector3 destination) {
        this.start = start;
        this.destination = destination;
        progress = 0.0f;

        routeCalculator = new RouteCalculator();
    }

    public boolean start(ObjectMap<Vector3, Hexagon> hexagons, Player player) {
        ObjectMap<Vector3, Hexagon> ownerMap = routeCalculator.constructOwnerMap(hexagons, player);

        wayPoints = routeCalculator.calculateRoute(ownerMap, start, destination);

        return wayPoints != null;
    }

    public void cancel() {

    }

    public boolean update(float delta) {
        // if progress reached 100% return true else false
        progress += SPEED * delta;
        if (progress >= 100.0f) {
            progress = 0.0f;            //reset progress
            start = wayPoints.get(0);   //set new start HexField
            // Todo start auf nächsten punkt setzen, gucken ob Ziel erreicht wurde
            return true;
        } else
            return false;
    }

}
