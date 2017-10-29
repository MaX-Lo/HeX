package de.maxlo.hex.GameObjects;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 28.10.17.
 */

public class RouteCalculator {

    /**
     * calculate a route from start to destination using the A* algorithm
     *
     * returns null if no route exists otherwise the route as a list with Vector3
     */
    public List<Vector3> calculateRoute(ObjectMap<Vector3, Hexagon> hexagons, Vector3 start, Vector3 destination) {
        if (!hexagons.containsKey(start))
            throw new IllegalArgumentException("Map doesn't contain start field");
        else if (!hexagons.containsKey(destination))
            throw new IllegalArgumentException("Map doesn't contain destination field");

        // TODO remove after finishing
        System.out.println("Start: " + start);
        System.out.println("Destination: " + destination);


        // convert the ObjectMap<Vector3, Hexagon> because the Hexagon object isn't of interest
        // and the cost and predecessor has to be stored someway
        ObjectMap<Vector3, PredecessorCostTuple> undiscoveredMap = new ObjectMap<Vector3, PredecessorCostTuple>();
        for (ObjectMap.Entry<Vector3, Hexagon> entry : hexagons ) {
            // init cost with a number where it's sure it is never reached
            undiscoveredMap.put(entry.key, null);
        }
        undiscoveredMap.remove(start);

        ObjectMap<Vector3, PredecessorCostTuple> discoveredMap = new ObjectMap<Vector3, PredecessorCostTuple>();

        ObjectMap<Vector3, PredecessorCostTuple> nextCandidates = new ObjectMap<Vector3, PredecessorCostTuple>();
        nextCandidates.put(start, new PredecessorCostTuple(null, getDistance(start, destination)));

        boolean destinationFound = false;

        while (nextCandidates.size > 0 && !destinationFound) {

            Vector3 currentField = getFieldWithLowestCost(nextCandidates);
            int old_cost = nextCandidates.get(currentField).cost;

            for (Vector3 pos : getNeighbours(currentField)) {

                // check whether destination is found
                if (pos.equals(destination)) {
                    destinationFound = true;
                    discoveredMap.put(pos, new PredecessorCostTuple(currentField, getDistance(start, destination)));
                }
                // only calculate cost if field exists
                else if (undiscoveredMap.containsKey(pos)) {
                    // add field to candidates
                    Integer cost = old_cost+1 + getDistance(pos, destination);
                    nextCandidates.put(pos, new PredecessorCostTuple(currentField, cost));
                    undiscoveredMap.remove(pos);
                }
            }

            discoveredMap.put(currentField, nextCandidates.get(currentField));
            nextCandidates.remove(currentField);
        }


        if (!destinationFound)
            return null;
        else {
            // TODO remove after finishing
            List<Vector3> route = reconstructRoute(discoveredMap, destination);
            System.out.println("Route:");
            System.out.println(route);
            return route;
        }
    }

    /**
     * takes the given destination field and tracks it back to the start point while constructing the route
     */
    private static List<Vector3> reconstructRoute(ObjectMap<Vector3, PredecessorCostTuple> map, Vector3 destination) {
        if (map == null || destination == null)
            throw new NullPointerException();
        if (!map.containsKey(destination))
            throw new IllegalArgumentException("Map must contain destination!" + destination);

        List<Vector3> route = new ArrayList<Vector3>();

        Vector3 currentField = destination;
        while (map.get(currentField).predecessor != null) {
            route.add(currentField);
            currentField = map.get(currentField).predecessor;
        }
        route.add(currentField);

        return route;
    }


    /**
     * get the shortest possible way between two hexagon fields (assuming there are no holes or
     * obstacles
     */
    private static int getDistance(Vector3 pos1, Vector3 pos2) {
        return (int)(Math.abs(pos1.x-pos2.x) + Math.abs(pos1.y-pos2.y));
    }

    private static Vector3 getFieldWithLowestCost(ObjectMap<Vector3, PredecessorCostTuple> map) {
        if (map == null)
            throw new NullPointerException();

        ObjectMap.Entry<Vector3, PredecessorCostTuple> bestField = null;
        for (ObjectMap.Entry<Vector3, PredecessorCostTuple> entry : map) {
            if (bestField == null)
                bestField = entry;
            else if (bestField.value.cost > entry.value.cost)
                bestField = entry;
        }

        assert bestField != null;
        return bestField.key;
    }

    /**
     * Helper class to store predecessor and cost in one map entry
     */
    private class PredecessorCostTuple {

        private Vector3 predecessor;
        private Integer cost;

        public PredecessorCostTuple(Vector3 predecessor, Integer cost) {
            this.predecessor = predecessor;
            this.cost = cost;
        }
    }

    /**
     * Constructs a map with only fields that are owned by the given owner
     * @param map - original map where none owner field should be removed from
     * @param owner - owner of the desired fields
     * @return - map only with fields being owned by the owner
     */
    public static ObjectMap<Vector3, Hexagon> constructOwnerMap(ObjectMap<Vector3, Hexagon> map, Player owner) {
        ObjectMap<Vector3, Hexagon> ownerMap = new ObjectMap<Vector3, Hexagon>();

        for (ObjectMap.Entry<Vector3, Hexagon> entry : map) {
            if (entry.value.getOwner().equals(owner)) {
                ownerMap.put(entry.key, entry.value);
            }
        }
        return ownerMap;
    }

    /**
     * returns a list with Vector3's containing the surrounding fields positions
     */
    private static List<Vector3> getNeighbours(Vector3 pos) {
        List<Vector3> neighbours = new ArrayList<Vector3>();

        neighbours.add(new Vector3(pos.x, pos.y+1, 0));
        neighbours.add(new Vector3(pos.x+1, pos.y+1, 0));
        neighbours.add(new Vector3(pos.x+1, pos.y, 0));
        neighbours.add(new Vector3(pos.x, pos.y-1, 0));
        neighbours.add(new Vector3(pos.x-1, pos.y-1, 0));
        neighbours.add(new Vector3(pos.x-1, pos.y, 0));

        return neighbours;
    }

}
