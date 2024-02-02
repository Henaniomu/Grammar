package types;

import main.MAIN;
import java.util.Random;


/**
 * The Cart class represents a delivery cart used for transporting orders.
 * It includes properties such as name, speed range, distance range, fix time, capacity, and current state.
 * The class also contains methods for cart movements, maintenance, and time management.
 */
public class Cart {
    // Properties
    public String name;
    double minSpeed, maxSpeed;
    public double minDistance, maxDistance;
    double fixTime;
    public double capacity;
    public double percent;
    public double remainingTimeToDeliver;
    public boolean isBusy = false;
    double avgSpeed;
    double deviation;
    Random random = new Random();
    public double avgDeviation;
    public final double avgDistanceToFix;
    public double remainDistBeforeFix;
    public Storage storageF;
    public double returnTime;
    public Order activeOrder;
    public int type;
    public double busyTill = 0;
    public double timeToGoBack = 0;
    private static int n = 1;


    // Constructors

    /**
     * Constructs a Cart object with the specified parameters.
     *
     * @param name          The name of the cart.
     * @param minSpeed      The minimum speed of the cart.
     * @param maxSpeed      The maximum speed of the cart.
     * @param minDistance   The minimum distance the cart can cover.
     * @param maxDistance   The maximum distance the cart can cover.
     * @param fixTime       The time required for cart maintenance.
     * @param capacity      The capacity of the cart.
     * @param percent       The percentage of capacity used.
     */
    public Cart(String name, double minSpeed, double maxSpeed, double minDistance, double maxDistance,
                double fixTime, double capacity, double percent) {
        this.name = name + " " + n;
        n++;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.fixTime = fixTime;
        this.capacity = capacity;
        this.percent = percent * 100;

        remainingTimeToDeliver = 0;
        isBusy = false;
        activeOrder = null;
        avgSpeed = (minSpeed + maxSpeed) / 2;
        deviation = (maxDistance - minDistance) / 4;
        if (deviation != 0) {
            avgDeviation = random.nextDouble(0, 2 * deviation) - deviation;
        } else {
            avgDeviation = 0;
        }

        avgDistanceToFix = (minDistance + maxDistance) / 2 + avgDeviation;
        remainDistBeforeFix = avgDistanceToFix;
    }

    // Methods

    /**
     * Initiates maintenance for the cart.
     *
     * @param customer The customer requesting maintenance.
     */
    void startFix(int customer){
        System.out.println("Cas: " + MAIN.time + ", Zakaznik: " + customer + ", " + name + " kolecko yzaduje udrzbu, pokracovani mozne v: " + (MAIN.time + fixTime));
        this.isBusy = true;
        this.remainingTimeToDeliver = fixTime;
        remainDistBeforeFix = avgDistanceToFix;
    }

    /**
     * Gets the distance before the cart requires maintenance.
     *
     * @return The average distance to maintenance.
     */
    double getDistanceBeforeFix(){
        return avgDistanceToFix;
    }

    /**
     * Initiates movement for the cart to deliver an order.
     *
     * @param order The order to be delivered.
     */
    public void startMoving(Order order){
        double odjezdV = MAIN.time + this.storageF.timeToRefillCart * order.bagsAmount;

        vypis1(odjezdV,order);
        vypis2(odjezdV,order);
        vypis3(odjezdV);
//                System.out.println("doruceni trva: " + activeCart.remainingTimeToDeliver);
        Cart minIncTimeCart = MAIN.minIncTimeCart();
        MAIN.incrementTime(minIncTimeCart.remainingTimeToDeliver + minIncTimeCart.timeToGoBack);

        //peresunut v true mesto
        MAIN.deliveredBags += order.bagsAmount;
        MAIN.acceptedRequests++;

    }


    // Additional private methods for output display

    /**
     * Displays information about the cart's return to the storage after delivery.
     *
     * @param odjezdV The time of departure for the cart.
     */
    private void vypis3(Double odjezdV) {
        System.out.println("Cas: "
//                        + (time + busyCart.returnTime) +
                + (odjezdV + this.remainingTimeToDeliver + this.timeToGoBack) // vrode verno, no bylo bez 2 *
                + ", Kolecko: " + this.name +
                ", Navrat do skladu: " + this.storageF.storageName);
        System.out.println("remTimeToDeliver = " + this.remainingTimeToDeliver);
        this.busyTill = (this.timeToGoBack);
    }


    /**
     * Displays information about the cart's delivery details.
     *
     * @param odjezdV The time of departure for the cart.
     * @param order   The order being delivered.
     */
    private void vypis2(Double odjezdV,Order order) {
        System.out.println("Cas: " + odjezdV +
                ", Kolecko: " + this.name +
                ", Zakaznik: " + order.customerId +
                ",  Vylozeno pytlu: " + order.bagsAmount +
                ", vylozeno v: " + (odjezdV + this.remainingTimeToDeliver) +
                ", Casova rezerva: " + (order.deadline - (odjezdV + this.remainingTimeToDeliver)));
    }

    /**
     * Displays information about the cart's departure and loading of bags.
     *
     * @param odjezdV The time of departure for the cart.
     * @param order   The order being delivered.
     */
    private void vypis1(double odjezdV, Order order) {
        System.out.println("Cas: " + MAIN.time +
                        " Kolecko: " + this.name +
                        ", Sklad: " + this.storageF.storageName +
                        ", Nalozeno pytlu: " + order.bagsAmount +
                        ", Odjezd: "
//                        + ("time = " + time + " + activeStorage.getTimeToRefill = " + activeStorage.timeToRefillCart + " * " + bagsToDeliver + " = "
                        + odjezdV

        );
    }



//    /**
//     * Increments the simulation time and updates the state of carts and orders.
//     *
//     * @param t The time increment.
//     */
//    public static void incrementTime(double t){
//        MAIN.time += t;
//        for(Storage storage: MAIN.input.storages){
//            for(Cart cart : storage.storageCarts){
//                if(cart.returnTime > 0){
//                    cart.returnTime -= t;
//                }
//
////                if(cart.returnTime == 0){
//                cart.activeOrder = null;
//                cart.isBusy = false;
////                }
//            }
//        }
//
//    }


//    /**
//     * Finds the cart with the minimum return time.
//     *
//     * @return The cart with the minimum return time.
//     */
//    static Cart minIncTimeCart() {
//        double minTime = Double.MAX_VALUE;
//        Cart tempCart = null;
//
//        for(Storage storage: MAIN.input.storages){
//            for(Cart cart : storage.storageCarts){
//                if(cart.returnTime < minTime && cart.isBusy){
////                    if(cart.returnTime < 0){
////                        for(Storage storage1: input.storages){
////                            for(Cart cart1 : storage1.storageCarts){
////                                System.out.print(cart1.returnTime + " ");
////                            }
////                        }
////                        throw new Exception();
////                    }
//                    minTime = cart.returnTime;
//                    tempCart = cart;
//                }
//            }
//        }
//        tempCart.isBusy = false;
//        return tempCart;
//    }

}
