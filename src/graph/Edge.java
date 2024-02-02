package graph;

/**
 * The Edge class represents an edge in a graph.
 * Each edge has a destination vertex and a weight (length).
 */
public class Edge {

    /**
     * The vertex to which the edge leads.
     */
    private final int to;

    /**
     * The weight or length of the edge.
     */
    private final double weight;

    /**
     * Creates a new edge with the specified destination vertex and weight.
     *
     * @param to     The destination vertex of the edge.
     * @param weight The weight or length of the edge.
     */
    public Edge(int to, double weight) {
        this.to = to;
        this.weight = weight;
    }

    /**
     * Gets the destination vertex of the edge.
     *
     * @return The destination vertex.
     */
    public int getTo() {
        return to;
    }

    /**
     * Gets the weight or length of the edge.
     *
     * @return The weight or length of the edge.
     */
    public double getWeight() {
        return weight;
    }
}


