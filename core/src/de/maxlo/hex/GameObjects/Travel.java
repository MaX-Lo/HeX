package de.maxlo.hex.GameObjects;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.maxlo.hex.Hex;
import javafx.collections.transformation.SortedList;

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

    public boolean start(ObjectMap<Vector3, Hexagon> hexagons, Player player) {
        ObjectMap<Vector3, Hexagon> ownerMap = constructOwnerMap(hexagons, player);

        wayPoints = calculateRoute(ownerMap, start, destination);

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

    /**
     * returns null if no route exists
     *
     * --- A* algorithm ---
     * evaluated full path costs
     *
     * full_cost - f(n),
     * cost_to_current_point - c(n)
     * cost_to_destination - d(n)
     *
     * Steps:
     * 1) calculate full_cost for every unexplored neighbour
     * 2) choose field with the lowest cost and expand it
     * 3) if destination is not reached yet calculate costs for the new explored field
     */
    private List<Vector3> calculateRoute(ObjectMap<Vector3, Hexagon> hexagons, Vector3 start, Vector3 destination) {
        if (!hexagons.containsKey(start))
            throw new IllegalArgumentException("Map doesn't contain start field");
        else if (!hexagons.containsKey(destination))
            throw new IllegalArgumentException("Map doesn't contain destination field");

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
    private List<Vector3> reconstructRoute(ObjectMap<Vector3, PredecessorCostTuple> map, Vector3 destination) {
        if (map == null || destination == null)
            throw new NullPointerException();
        if (!map.containsKey(destination))
            throw new IllegalArgumentException("Map must contain destination!" + destination);

        List<Vector3> route = new ArrayList<Vector3>();

        Vector3 currentField = destination;
        while (map.get(currentField).getPredecessor() != null) {
            route.add(currentField);
            currentField = map.get(currentField).getPredecessor();
        }
        route.add(currentField);

        return route;
    }


    /**
     * get the shortest possible way between two hexagon fields (assuming there are no holes or
     * obstacles
     */
    private int getDistance(Vector3 pos1, Vector3 pos2) {
        return (int)(Math.abs(pos1.x-pos2.x) + Math.abs(pos1.y-pos2.y));
    }

    private Vector3 getFieldWithLowestCost(ObjectMap<Vector3, PredecessorCostTuple> map) {
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

        public Vector3 getPredecessor() {
            return predecessor;
        }

        public void setPredecessor(Vector3 predecessor) {
            this.predecessor = predecessor;
        }

        public Integer getCost() {
            return cost;
        }

        public void setCost(Integer cost) {
            this.cost = cost;
        }
    }

    /**
     * Constructs a map with only fields that are owned by the given owner
     * @param map - original map where none owner field should be removed from
     * @param owner - owner of the desired fields
     * @return - map only with fields being owned by the owner
     */
    private ObjectMap<Vector3, Hexagon> constructOwnerMap(ObjectMap<Vector3, Hexagon> map, Player owner) {
        ObjectMap<Vector3, Hexagon> ownerMap = new ObjectMap<Vector3, Hexagon>();

        for (ObjectMap.Entry<Vector3, Hexagon> entry : map) {
            if (entry.value.getOwner().equals(owner)) {
                ownerMap.put(entry.key, entry.value);
            }
        }
        return ownerMap;
    }

    /**
     * @param pos their neighbours should be returned
     * @return neighbours of pos
     */
    private List<Vector3> getNeighbours(Vector3 pos) {
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
