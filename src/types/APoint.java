package types;

/**
 * The APoint class represents an abstract point in a two-dimensional space.
 * It has x and y coordinates and serves as a base class for specific point implementations.
 */
public abstract class APoint {

    /**
     * The x-coordinate of the point.
     */
    double x;

    /**
     * The y-coordinate of the point.
     */
    double y;

    /**
     * Creates a new abstract point with the specified x and y coordinates.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     */
    public APoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate of the point.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the point.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }
}

