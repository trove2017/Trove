package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class graph_pathways {
    private Map<String, LinkedHashSet<String>> map;
    private String START;
    private String END;
    private ArrayList<ArrayList<String>> allPaths;
    
    public graph_pathways(){
    	map = new HashMap();
    	START=null;
    	END=null;
    	allPaths=new ArrayList<ArrayList<String>>();
    }
    
    public ArrayList<ArrayList<String>> getAllPaths(){
    	return allPaths;
    }
    
    public void addEdge(String node1, String node2) {
        LinkedHashSet<String> adjacent = map.get(node1);
        if(adjacent==null) {
            adjacent = new LinkedHashSet();
            map.put(node1, adjacent);
        }
        adjacent.add(node2);
    }

    public void addTwoWayVertex(String node1, String node2) {
        addEdge(node1, node2);
        addEdge(node2, node1);
    }

    public boolean isConnected(String node1, String node2) {
        Set adjacent = map.get(node1);
        if(adjacent==null) {
            return false;
        }
        return adjacent.contains(node2);
    }

    public LinkedList<String> adjacentNodes(String last) {
        LinkedHashSet<String> adjacent = map.get(last);
        if(adjacent==null) {
            return new LinkedList();
        }
        return new LinkedList<String>(adjacent);
    }
    
    public void setStartAndEndNodes(String S, String E)
    {
    	START=S;
    	END=E;
    }
    
    public void depthFirst(LinkedList<String> visited) {
        LinkedList<String> nodes = adjacentNodes(visited.getLast());
        // examine adjacent nodes
        for (String node : nodes) {
            if (visited.contains(node)) {
                continue;
            }
            if (node.equals(END)) {
                visited.add(node);
                printPath(visited);
                visited.removeLast();
                break;
            }
        }
        for (String node : nodes) {
            if (visited.contains(node) || node.equals(END)) {
                continue;
            }
            visited.addLast(node);
            depthFirst(visited);
            visited.removeLast();
        }
        System.out.println("done");
    }

    public void printPath(LinkedList<String> visited) {
    	ArrayList<String> aPath=new ArrayList<String>();
        for (String node : visited) {
        	aPath.add(node);
        	System.out.print(node);
            System.out.print(" ");
        }
        allPaths.add(aPath);
        System.out.println();
    }
}