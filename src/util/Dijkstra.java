package util;

import java.util.*;

import graph.Edge;
import graph.Graph;

/**
 * The Dijkstra class implements Dijkstra's algorithm for finding the shortest paths in a weighted graph.
 */
public class Dijkstra {

    /**
     * Computes the shortest paths from a start vertex to a list of target vertices in the given graph.
     *
     * @param graph        The graph on which Dijkstra's algorithm is applied.
     * @param startVertex  The starting vertex for computing the shortest paths.
     * @param targetVertexes  A list of target vertices to compute the shortest paths to.
     * @return An array containing the shortest distances from the start vertex to all vertices in the graph.
     */
    public static double[] dijkstra(Graph graph, int startVertex, List<Integer> targetVertexes) {
        int V = graph.getVertexCount();
        double[] dist = new double[V]; // Array for distances
        int[] previous = new int[V]; // Array to track the previous vertex in the optimal path
        boolean[] visited = new boolean[V]; // Array to mark visited vertices

        // Initialize distances: the start vertex has distance 0, others have "infinity"
        Arrays.fill(dist, Double.MAX_VALUE);
        dist[startVertex] = 0;

        // Main loop of the algorithm
        for (int i = 0; i < V - 1; i++) {
            int u = findMinDistanceVertex(dist, visited);
            visited[u] = true;

            List<Edge> neighbors = graph.getNeighbors(u);
            for (Edge edge : neighbors) {
                int v = edge.getTo();
                double weight = edge.getWeight();
                if (!visited[v] && dist[u] != Double.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    previous[v] = u; // Update the previous vertex
                }
            }
        }

        // Print the shortest paths from startVertex to targetVertexes
        for (int vertex : targetVertexes) {
            List<Integer> shortestPath = new ArrayList<>();
            int currentVertex = vertex;
            while (currentVertex != startVertex) {
                shortestPath.add(currentVertex);
                currentVertex = previous[currentVertex];
            }
            shortestPath.add(startVertex);
            Collections.reverse(shortestPath);
            System.out.println("Shortest path from " + startVertex + " to " + vertex + ": " + shortestPath);
        }

        return dist;
    }

    /**
     * Finds the vertex with the minimum distance that has not been visited yet.
     *
     * @param dist    The array of distances.
     * @param visited The array indicating visited vertices.
     * @return The index of the minimum distance vertex.
     */
    private static int findMinDistanceVertex(double[] dist, boolean[] visited) {
        double min = Double.MAX_VALUE;
        int minIndex = -1;

        for (int i = 0; i < dist.length; i++) {
            if (!visited[i] && dist[i] <= min) {
                min = dist[i];
                minIndex = i;
            }
        }

        return minIndex;
    }
}
