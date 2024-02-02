package util;

import graph.Graph;
import graph.Path;
import types.Storage;
import types.Customer;
import types.Order;
import types.APoint;
import types.Cart;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class Input {
    private final Graph graph;
    Path[] paths;
    int storageCount;
    int customerCount;
    int waysCount;
    public Storage[] storages;
    Customer[] customers;
    int cartTypesCount;
    int orderCount;
    Cart[] carts;

    public double[] ranges;

    public Order[] orders;

    /**
     * creates instance of input to manipulate
     * @param file file to read
     */
    public Input(File file) {

        List<String> ar;
        InputFilter a = new InputFilter(file);
        ar = a.ar;

        //initialize storages
        Iterator<String> sc = ar.iterator();
        storageCount = Integer.parseInt(sc.next());
        storages = new Storage[storageCount];
        initializeStorages(sc);

        //initialize storages
        customerCount = Integer.parseInt(sc.next());
        customers = new Customer[customerCount];
        initializeCustomers(sc);

        //initialize points between Storages and customers
        int pointsCount = customerCount + storageCount + 1;
        APoint[] points = new APoint[pointsCount];
        initializePoints(points);

        //initialize Paths
        waysCount = Integer.parseInt(sc.next());
        this.paths = new Path[waysCount];
        graph = new Graph(pointsCount);
        initializePath(sc,points);

        //initialize carts types
        cartTypesCount = Integer.parseInt(sc.next());            //cart's type count
        carts = new Cart[cartTypesCount];
        ranges = new double[cartTypesCount + 1]; // +1 for additional "0" element
        initializeCartTypes(sc);

        //initialize orders
        orderCount = Integer.parseInt(sc.next());
        orders = new Order[orderCount];
        initializeOrders(sc);
    }

    /**
     * initialize points
     * @param points array of points
     */
    private void initializePoints(APoint[] points) {
        System.arraycopy(storages, 0, points, 1, storages.length + 1 - 1);

        for (int i = 0; i < customers.length; i++) {
            points[storageCount + i + 1] = customers[i];
        }
        for (int i = 1; i < points.length; i++) {
            System.out.println("X = " + points[i].getX() + ", Y = " + points[i].getY());
        }
        System.out.println("////////////////////////////////////////////");
    }

    /**
     * initialize customers
     * @param sc Scanner
     */
    private void initializeCustomers(Iterator<String> sc) {
        for (int i = 0; i < customerCount; i++) {
            customers[i] = new Customer(
                    Double.parseDouble(sc.next()),   //x-position of Customer
                    Double.parseDouble(sc.next())    //y-position of Customer
            );
        }
    }

    /**
     * initialize storages
     * @param sc Scanner
     */
    private void initializeStorages(Iterator<String> sc) {
        for (int i = 0; i < storageCount; i++) {
            storages[i] = new Storage(
                    Double.parseDouble(sc.next()),    //x-position of Storage
                    Double.parseDouble(sc.next()),    //y-position of Storage
                    Integer.parseInt(sc.next()),      //time to refill the storage Ts
                    Integer.parseInt(sc.next()),      //bag count Ks
                    Integer.parseInt(sc.next())       //manipulate time with bag Tn
            );
        }
    }

    /**
     * initialize orders
     * @param sc Scanner
     */
    private void initializeOrders(Iterator<String> sc) {
        for (int i = 0; i < orderCount; i++) {
            orders[i] = new Order(
                    Double.parseDouble(sc.next()),
                    Double.parseDouble(sc.next()),
                    Double.parseDouble(sc.next()),
                    Double.parseDouble(sc.next())
            );
        }
    }

    /**
     *
     * @param sc Scanner
     */
    private void initializeCartTypes(Iterator<String> sc) {
        for (int i = 0; i < cartTypesCount; i++) {
            carts[i] = new Cart(
                    String.valueOf(sc.next()),
                    Double.parseDouble(sc.next()),
                    Double.parseDouble(sc.next()),
                    Double.parseDouble(sc.next()),
                    Double.parseDouble(sc.next()),
                    Integer.parseInt(sc.next()),
                    Integer.parseInt(sc.next()),
                    Double.parseDouble(sc.next())
            );
            ranges[i + 1] = carts[i].percent;
            carts[i].type = i;
        }
    }

    /**
     * initialize Paths
     * @param sc Scanner
     * @param points Points[]
     */
    private void initializePath(Iterator<String> sc, APoint[] points) {
        for (int i = 0; i < paths.length; i++) {
            int start = Integer.parseInt(sc.next());
            int end = Integer.parseInt(sc.next());
            paths[i] = new Path(points[start], points[end]);
            graph.addEdge(start, end, paths[i].getLength());
            graph.addEdge(end, start, paths[i].getLength());
        }
    }


    // getters
    public Storage[] getStorages() {

        return storages;
    }

    public Customer[] getCustomers() {
        return customers;
    }

    public Order[] getOrders() {
        return orders;
    }

    public Cart[] getCarts() {
        return carts;
    }

    public int getStorageCount() {
        return storageCount;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public int getWaysCount() {
        return waysCount;
    }

    public int getCartTypesCount() {
        return cartTypesCount;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public Path[] getPaths() {
        return paths;
    }

    public Graph getGraph() {
        return graph;
    }

    void testCartsStopWorking(Cart[] carts) {
        for (Cart cart : carts) {
            cart.isBusy = false;
            cart.activeOrder = null;
        }
    }
}

