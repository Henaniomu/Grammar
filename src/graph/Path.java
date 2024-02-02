package graph;

import types.APoint;
/**
 * The Path class represents an interval between two measurements.
 * It calculates and stores the start point, end point, and length of the interval.
 */
public class Path {

    /**
     * The starting point of the interval.
     */
    private final APoint start;

    /**
     * The ending point of the interval.
     */
    private final APoint end;

    /**
     * The length of the interval.
     */
    private final double length;

    /**
     * Creates a new interval between two measurements.
     *
     * @param start The starting point of the interval.
     * @param end   The ending point of the interval.
     */
    public Path(APoint start, APoint end) {
        this.start = start;
        this.end = end;
        length = calcLength();
    }

    /**
     * Calculates the length of the interval using the Euclidean distance formula.
     *
     * @return The length of the interval.
     */
    private double calcLength() {
        return Math.sqrt(
                Math.abs(start.getX() - end.getX()) * Math.abs(start.getX() - end.getX()) +
                        Math.abs(start.getY() - end.getY()) * Math.abs(start.getY() - end.getY())
        );
    }

    /**
     * Gets the starting point of the interval.
     *
     * @return The starting point.
     */
    public APoint getStart() {
        return start;
    }

    /**
     * Gets the ending point of the interval.
     *
     * @return The ending point.
     */
    public APoint getEnd() {
        return end;
    }

    /**
     * Gets the length of the interval.
     *
     * @return The length of the interval.
     */
    public double getLength() {
        return length;
    }
}