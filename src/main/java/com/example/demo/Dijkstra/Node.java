package com.example.demo.Dijkstra;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Node {
    
    private String name;
    
    private List<Node> shortestPath = new LinkedList<>();
    
    private Integer distance = Integer.MAX_VALUE;
    
    Map<Node, Integer> adjacentNodes = new HashMap<>();

    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }
 
    public Node(String name) {
        this.name = name;
    }
    
    // getters and setters
    public String getName() {
        return name;
    }

    public void setname(String newName) {
        name = newName;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(Integer newDistance) {
        distance = newDistance;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }
    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> newShortestPath) {
        shortestPath = newShortestPath;
    }
}
