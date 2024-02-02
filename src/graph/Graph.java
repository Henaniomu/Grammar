package graph;

import java.util.*;

/**
 * The Graph class represents a graph using an adjacency list.
 * Each vertex is associated with a list of neighboring vertices and their corresponding edge weights.
 */
public class Graph {

    private final int V;           // Number of vertices
    private final List<Edge>[] adj; // Adjacency list

    /**
     * Constructs a graph with the specified number of vertices.
     *
     * @param V The number of vertices in the graph.
     */
    public Graph(int V) {
        this.V = V;
        adj = new ArrayList[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    /**
     * Adds a weighted edge between two vertices.
     *
     * @param from   The starting vertex of the edge.
     * @param to     The ending vertex of the edge.
     * @param weight The weight of the edge.
     */
    public void addEdge(int from, int to, double weight) {
        adj[from].add(new Edge(to, weight));
    }

    /**
     * Gets the neighbors of a given vertex.
     *
     * @param vertex The vertex for which neighbors are retrieved.
     * @return A list of edges representing the neighbors of the given vertex.
     */
    public List<Edge> getNeighbors(int vertex) {
        return adj[vertex];
    }

    /**
     * Gets the total number of vertices in the graph.
     *
     * @return The number of vertices in the graph.
     */
    public int getVertexCount() {
        return V;
    }
}

