package main;

import graph.Path;
import types.Cart;
import types.Storage;
import util.Input;
import graph.Graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MAIN {
    public static double time = 0;
    public static final BufferedWriter writer;

    static {
        try {
            writer = new BufferedWriter(new FileWriter("output.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Input input;
    /**
     * Path's between the customers and Storages
     */
    public static Path[] paths;
    /**
     * Graph of customers and Storages connection
     */
    public static Graph g;
    /**
     * number of all delivered bags during the work day
     */
    public static int deliveredBags = 0;
    /**
     * number of all accepted requests during work day
     */
    public static int acceptedRequests = 0;
    /**
     * statistics will tell us how many carts we need
     */

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {

        File file = new File("data/sparse3M.txt");
        input = new Input(file);
//        input.getStorages();
        int orderCount = input.getOrderCount();

        paths = input.getPaths();
        g = input.getGraph();


//        for (Path path : paths) {
//            System.out.println(path.getStart() + " " + path.getEnd() + " " + path.getLength());
//        }

        //gaining statistics
        makeSomeTests(input.storages);


        //now to the main part
        for (int i = 0; i < orderCount; i++) {     //++ ili orderIncrement
            System.out.println("i = " + i);
            Cart busyCart = getBusyCart();
            if (busyCart != null) { //
                System.out.println("go");
//                double tt = time;
//                if(tt < 0){
//                    throw new ArithmeticException();
//                }
                //go cart, go
                busyCart.startMoving(input.orders[i]);
                System.out.println();
                //TODO
                //nado ucest refill skladov i vremya prihoda next ordera
            } else {
                //if there is unused carts, let's make them busy
                Cart freeCart = input.orders[i].isAcceptRequest();                      //takes first free cart
                int innerIteration = 0;

                while (freeCart != null && !freeCart.isBusy) {      //if all carts busy, returning from the block
                    if (input.orders[i].isAcceptRequest() != null) {          //make sure we can accept order
//                        freeCart.activeOrder = input.orders[i];     //now cart have own order to complete
//                        freeCart.isBusy = true;
                        freeCart = input.orders[i].isAcceptRequest();              //after that we will check another catrs
                        if(innerIteration == 1){
                            incrementTime(minIncTimeCart().returnTime);
//                            System.exit(0);
                            break;
                        }
                        innerIteration++;
                    }else{
                        i++;
                    }

                }

                if(freeCart == null){
                    i = input.orders.length;
                    break;
                }
                i--;
            }
        }

        System.out.println("dorucene pytle: " + deliveredBags);
        System.out.println("splnenych pozadavku: " + acceptedRequests);

//        int plz = 0;
//        for (Storage storage : input.storages){
//            for (Cart cart : storage.storageCarts){
//                plz++;
//            }
//        }
//        System.out.println("plzplzplz: " + plz);
    }


    static Cart getFreeCart() {
        Cart tempCart = null; //null
        Cart best = MAIN.input.storages[0].freeStorageCart();
        for (Storage storage : input.storages) {
            tempCart = storage.freeStorageCart();
            if (tempCart != null && best != null && tempCart.percent < best.percent) {
//                if(tempCart.percent < best.percent){
                    best = tempCart;
//                }
            }

        }
        if(best != null){
            System.out.println(best.name);
        }

        return best;
    }

    static Cart getBusyCart() {
        Cart tempCart = null;
        for (Storage storage : input.storages) {
            tempCart = storage.busyStorageCart();
            if (tempCart != null) {
                return tempCart;
            }
        }
        return null;
    }

    /**
     * @param storages
     */
    static void makeSomeTests(Storage[] storages) {
        int maxIteration = Math.min(input.getOrderCount(), 100);
        fewCartsCase(storages,maxIteration);
    }

    private static void fewCartsCase(Storage[] storages, double maxIteration) {
        double[] returnStat = new double[input.getCartTypesCount()];
        double tSR = 0;
        for(int t = 0; t < input.getCartTypesCount(); t++){
            storages[0 % storages.length].addCart(t);
            for (int i = 0; i < maxIteration; i++) {
                Cart testCart = getFreeCart(); //Generacija 100 cartov v 1 sklade
                if(testCart != null && input.orders[i].isDeliveryPossible(testCart, storages[i % storages.length])){
                        returnStat[t] += testCart.returnTime;
                }


            }

            Cart testCart = storages[0].storageCarts.get(0);
            if(testCart != null){
                System.out.println("percent = " + testCart.percent);
                tSR += ((testCart.percent/100) * returnStat[t]);
                System.out.println("tsr = " + tSR);
            }
            System.out.println(t);
            for (Storage storage : storages){
                storage.refillStorage();
                storage.removeAllCarts();
            }
        }

        int weNeedCarts = (int)Math.ceil(input.getOrderCount()/tSR);
        System.out.println("we need carts = " + weNeedCarts);

        for(int i = 0; i < weNeedCarts; i++){
            input.storages[i % input.storages.length].addCart(Storage.generateCartIndex());
        }
    }



    /**
     * Increments the simulation time and updates the state of carts and orders.
     *
     * @param t The time increment.
     */
    public static void incrementTime(double t){
        MAIN.time += t;
        for(Storage storage: MAIN.input.storages){
            for(Cart cart : storage.storageCarts){
                if(cart.returnTime > 0){
                    cart.returnTime -= t;
                }

                if(cart.returnTime == 0){
                    cart.activeOrder = null;
                    cart.isBusy = false;
                }
            }
        }

    }

    /**
     * Finds the cart with the minimum return time.
     *
     * @return The cart with the minimum return time.
     */
    public static Cart minIncTimeCart() {
        double minTime = Double.MAX_VALUE;
        Cart tempCart = null;

        for(Storage storage: MAIN.input.storages){
            for(Cart cart : storage.storageCarts){
                if(cart.returnTime < minTime && cart.isBusy){
                    minTime = cart.returnTime;
                    tempCart = cart;
                }
            }
        }
        tempCart.isBusy = false;
        return tempCart;
    }
}




