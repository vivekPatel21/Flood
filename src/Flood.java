// Imports for the parameters of flood

import java.lang.reflect.Array;
import java.util.*;

public class Flood {

    public static void flood(WaterColor color,
                              LinkedList<Coord> flooded_list,
                              Tile[][] tiles,
                              Integer board_size) {
        ArrayList<Coord> newGuys = new ArrayList<>();
        for(Coord cd : flooded_list) {
            for(Coord neighbor : cd.neighbors(board_size)) {
                if(tiles[neighbor.getY()][neighbor.getX()].getColor() == color && neighbor.onBoard(board_size) && !flooded_list.contains(neighbor)) {
                    //flooded_list.add(neighbor);
                    newGuys.add(neighbor);
                }
            }
        }
        Set<Coord> cleaner = new HashSet<Coord>(newGuys);
        newGuys.clear();
        newGuys.addAll(cleaner);     //Now newGuys has no duplicates
        flooded_list.addAll(newGuys);
        ArrayList<Coord> newNewGuys = new ArrayList<>();
        do {
            newNewGuys.clear();
            for(Coord guy : newGuys) {
                for(Coord guyNeighbor : guy.neighbors(board_size)) {
                    if(tiles[guyNeighbor.getY()][guyNeighbor.getX()].getColor() == color && guyNeighbor.onBoard(board_size) && !newNewGuys.contains(guyNeighbor) && !flooded_list.contains(guyNeighbor)) {
                        flooded_list.add(guyNeighbor);
                        newNewGuys.add(guyNeighbor);
                    }
                }
                //newGuys.remove(guy);
            }
            newGuys.clear();
            newGuys.addAll(newNewGuys);

        } while(!newGuys.isEmpty());
    }

    // An alternative implementation goes here.
    // O(nlogn)
    public static void flood1(WaterColor color,
                              LinkedList<Coord> flooded_List,
                              Tile[][] tiles,
                              Integer board_Size) {

        Queue<Coord> queue = new LinkedList<>();
        Set<Coord> visited = new HashSet<>();

        for (Coord initialCoord : flooded_List) {
            queue.offer(initialCoord);
            visited.add(initialCoord);
        }

        while (!queue.isEmpty()) {
            Coord currentCoord = queue.poll();

            for (Coord neighbor : currentCoord.neighbors(board_Size)) {
                if (!visited.contains(neighbor) && tiles[neighbor.getY()][neighbor.getX()].getColor() == color) {
                    queue.offer(neighbor);
                    flooded_List.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }
    }


}
