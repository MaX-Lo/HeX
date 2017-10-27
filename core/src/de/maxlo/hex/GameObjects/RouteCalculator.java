package de.maxlo.hex.GameObjects;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by moritz on 02.10.17.
 */

public final class RouteCalculator{
    private static final int AMMOUNT_OF_TMP_SETS = 40; // should euquals the ammount of HexFields

    private Set<Vector3> nodeSet;
    private Map<Vector3, Set<Vector3>> verticesMap;

    private List<Set<Vector3>> tmpSetList;

    private List<Vector3> frontier;
    private List<Vector3> tmpFrontier;
    private List<Vector3> waypoints;
    private List<Vector3> closedNodes;
    private int distance_from_source;
    private Comparator<Vector3> priorityComperator;


    private RouteCalculator(){//TODO private construct was a hint on stackoverflow (static class java)
        initialise();
    }

    public List<Vector3> calculateRoute(GameMap map, Player player, Vector3 start, Vector3 destination){
        clearAll();
        constructNodeSet(map, player);
        constructVerticesMap();
        return aStar(start, destination);
    }

    private List<Vector3> aStar(Vector3 start, Vector3 destination) {
        //trick 17: z coord of Vector3 is priority
        distance_from_source = 0;
        start.z = 0;
        frontier.add(start);
        while (!frontier.isEmpty()){
            frontier.sort(priorityComperator);
            distance_from_source++;              //TODO
            for (Vector3 currentNode : frontier){
                if (currentNode == destination){
                    closedNodes.add(currentNode);
                    break;
                }        //maybe add it as last point to waypoints

                tmpFrontier.addAll(verticesMap.get(currentNode));
                for (Vector3 currentAdjancentNode : tmpFrontier)
                    //priority is distance from goal + distance from source
                    currentAdjancentNode.z = Math.abs(destination.x - currentAdjancentNode.x) + Math.abs(destination.y - currentAdjancentNode.y) + distance_from_source;

                closedNodes.add(currentNode);
                frontier.remove(currentNode);

            }
        }
        return closedNodes;
    }

    private void constructVerticesMap() {
        int countOfTmpSets = 0;
        for (Vector3 currentNode : nodeSet){
            verticesMap.put(currentNode, tmpSetList.get(countOfTmpSets));
            countOfTmpSets++;
            for (Vector3 adjacentNode : nodeSet)
                //complicated if explanation:
                //1st big expression: checking directly above and below ( x++ / x-- while y const)
                //2nd big expression: checking diagonal lower right and upper left ( x const while y++ / y-- )
                //3rd big expression: checking diagonal lower left and upper right (x++ && y++ || x-- && y-- )
                //4th big expression: check not to make currentCoords also as adjancent (xc != xa && yc != ya)
                if ((((currentNode.x == adjacentNode.x - 1) || currentNode.x == adjacentNode.x + 1) && currentNode.y == adjacentNode.y) || (currentNode.x == adjacentNode.x && ((currentNode.y == adjacentNode.y +1) || (currentNode.y == adjacentNode.y-1) )) || (currentNode.x == adjacentNode.x - 1 && currentNode.y == adjacentNode.y -1) || (currentNode.x == adjacentNode.x + 1 && currentNode.y == adjacentNode.y +1) && (currentNode.x != adjacentNode.x && currentNode.y != adjacentNode.y))
                    verticesMap.get(currentNode).add(adjacentNode);
        }
    }

    private void initialise() {
        nodeSet = new HashSet<Vector3>();
        verticesMap = new HashMap<Vector3, Set<Vector3>>();

        frontier = new ArrayList<Vector3>();
        waypoints = new ArrayList<Vector3>();

        tmpSetList = new ArrayList<Set<Vector3>>();
        for (int i = 0; i<AMMOUNT_OF_TMP_SETS; i++)
            tmpSetList.add(new HashSet<Vector3>());

        priorityComperator = new Comparator<Vector3>() {
            @Override
            public int compare(Vector3 vector3, Vector3 t1) {
                if (vector3.z < t1.z)
                    return -1;
                else if (vector3.z > t1.z)
                    return 1;
                else
                    return 0;
            }
        };
    }

    private void clearAll() {
        waypoints.clear();
        //clear frontier
        frontier.clear();
        //clear nodesSet
        nodeSet.clear();
        //clear verticesMap
        verticesMap.clear();
        //clear tmpSets
        for (Set<Vector3> tmpSet : tmpSetList)
            tmpSet.clear();
        distance_from_source = 0;
        heuristic = 0;
    }

    private void constructNodeSet(GameMap map, Player player){
        //checks each hexagon if the current owner is the given player
        for (Vector3 coordinates : map.getHexagons().keySet())
            if (player == map.getHexagon(coordinates).getOwner())
                nodeSet.add(coordinates);
    }
}
