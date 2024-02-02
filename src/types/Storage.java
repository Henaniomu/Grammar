package types;

import main.MAIN;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * The Storage class represents a storage location with properties like bag count, refill times, and associated carts.
 * It extends the APoint class to inherit x and y coordinates.
 */
public class Storage extends APoint {
    // Properties
    double bagCount;
    public double timeToRefillCart;
    public String storageName;
    private static int n = 0;
    public double timeToReFillStorage;
    public List<Cart> storageCarts = new ArrayList<>();

    public int remainingBags;

    /**
     * Constructs a Storage object with the specified parameters.
     *
     * @param x                  The x-coordinate of the storage location.
     * @param y                  The y-coordinate of the storage location.
     * @param bagCount           The count of bags in the storage.
     * @param timeToReFillStorage The time required to refill the entire storage.
     * @param timeToRefillCart   The time required to refill a cart.
     */
    public Storage(double x, double y, double bagCount, double timeToReFillStorage, double timeToRefillCart) {
        super(x, y);
        this.timeToReFillStorage = timeToReFillStorage;
        this.bagCount = bagCount;
        this.timeToRefillCart = timeToRefillCart;
        this.remainingBags = (int)bagCount;
        this.storageName = "storage " + n;
        n++;
    }

    /**
     * Adds a cart to the storage based on the specified cart type.
     *
     * @param cartType The index of the cart type.
     * @return The newly added cart.
     */
    public Cart addCart(int cartType) {
//        try {
            Cart newCart = MAIN.input.getCarts()[cartType];     //clone!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            newCart.storageF = this;
//            if(newCart.storageF == null){
//                throw new ArithmeticException();
//            }
            this.storageCarts.add(newCart);
//            MAIN.cartCount += 1;
            newCart.isBusy = false;
            return newCart;
//        } catch (Exception ignored){
//            return MAIN.input.getCarts()[0];
//        }
        //TODO
    }

    /**
     * Adds a specified cart to the storage.
     *
     * @param cart The cart to be added to the storage.
     */
    public void addCart(Cart cart) {        //mozhno uluchsit
        cart.storageF = this;
        this.storageCarts.add(cart);
//        MAIN.cartCount += 1;

        //TODO
    }

    public Cart tryCart() {
        return MAIN.input.getCarts()[generateCartIndex()];
    }

    public static int generateCartIndex() {
        int cartTypeChoice = -1;
        Random random = new Random();
        double casino = random.nextDouble(0, 100);
        double prePercent = 0;
        double postPercent = 0;


        for (int j = 1; j <= MAIN.input.getCartTypesCount(); j++) {
//            double a = (MAIN.input.ranges[j-1] + prePercent);
//            double b = (MAIN.input.ranges[j] + postPercent);
            if (casino >= (MAIN.input.ranges[j-1] + prePercent ) && casino <= MAIN.input.ranges[j] + postPercent) {
                cartTypeChoice = j-1;
            }
            //0,90,99,100
            //0,90,9,1
            prePercent += MAIN.input.ranges[j-1];
            postPercent += MAIN.input.ranges[j];
        }
        return cartTypeChoice;
    }

//////////

    /**
     * Attempts to get a free cart from the storage.
     *
     * @return A free cart if available; otherwise, null.
     */
    public Cart freeStorageCart() {
        Cart temp = null;
        for (Cart storageCart : storageCarts) {
            if (storageCart.activeOrder == null && !storageCart.isBusy) {
                if (temp == null) {
                    temp = storageCart;
                }
                if (temp.percent < storageCart.percent) {
                    temp = storageCart;
                }
            }
        }

        return temp;
    }


    /**
     * Attempts to get a busy cart from the storage.
     *
     * @return A busy cart if available; otherwise, null.
     */
    public Cart busyStorageCart() {
        Cart temp = null;
        for (Cart storageCart : this.storageCarts) {

            if (storageCart.activeOrder != null && storageCart.isBusy ) {
                if (temp == null) {
                    temp = storageCart;
                }
                if (temp.percent < storageCart.percent) {
                    temp = storageCart;
                }
            }
        }
        return temp;
    }

    /**
     * Removes all carts from the storage.
     */
    public void removeAllCarts(){
        for (int i = 0; i < storageCarts.size(); i++) {
            storageCarts.remove(i);
        }
    }

    public double refillStorage(){
        this.remainingBags = (int)this.bagCount;
        System.out.println("Storage: " + this.storageName + " is refilling....");
        return Math.ceil(timeToReFillStorage);
    }
}
