package types;

/**
 * The Customer class represents a point in a two-dimensional space for a customer.
 * It extends the APoint class and inherits x and y coordinates.
 */
public class Customer extends APoint {

    /**
     * Creates a new customer point with the specified x and y coordinates.
     *
     * @param x The x-coordinate of the customer point.
     * @param y The y-coordinate of the customer point.
     */
    public Customer(double x, double y) {
        super(x, y);
        // Note: The super(x, y) call initializes the x and y coordinates in the base class (APoint).
        // No need to explicitly reassign them here.
    }
}