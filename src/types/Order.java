package types;

import main.MAIN;
import util.Dijkstra;

import java.util.ArrayList;
import java.util.List;

public class Order {

    public double orderGetTime;
    public int customerId;
    public double bagsAmount;
    public double deadline;

    public Order(double orderGetTime, double customerId, double bagsAmount, double deadline) {
        this.orderGetTime = orderGetTime;
        this.customerId = (int) customerId;
        this.bagsAmount = bagsAmount;
        this.deadline = deadline;
    }

    /**
     * checks if it is possible to process oan order
     *
     * @return
     */
    public Cart isAcceptRequest() {
        double[] lengths = new double[3];
        Storage[] closestStorages;
        List<Double> lengthsToVertex = new ArrayList<>();
        closestStorages = findClosestStorages((int) customerId + MAIN.input.getStorageCount(), lengths, lengthsToVertex);
//        Storage closestStorage = closestStorages[0];

//        System.out.println("xxx "+closestStorages.length);

        for (int i = 0; i < closestStorages.length; i++) {
            for (Cart cart : closestStorages[i].storageCarts) {
                //TODO
                if (cart.isBusy) {
                    continue;
                }

                if(isDeliveryPossible(cart,closestStorages[i])){
                    return cart;
                }
//                else{
//                    MAIN.incrementTime(MAIN.minIncTimeCart().returnTime);
//                }

            }

//            System.out.println("yyy "+closestStorages[0].storageCarts.size());
            return giveLastChance(closestStorages,i);


        }

        System.out.println("problem 4");
        System.out.println("Cas: " + MAIN.time + ", Zakaznik: " + customerId + ", umrzl zimou, protoze jezdit s koleckem je hloupost, konec");
        return null;
    }

    /**
     *
     * @param closestStorages
     * @param i
     * @return
     */
    private Cart giveLastChance(Storage[] closestStorages, int i) {
//        Storage closestStorage = null;
//        if(closestStorage == null){
           Storage closestStorage = closestStorages[i];
            for (int z = 0; z < 1000; z++){
                Cart lastChanceCart = closestStorage.tryCart();
                if (isDeliveryPossible(lastChanceCart, closestStorage)) {
                    closestStorage.addCart(lastChanceCart);
                    lastChanceCart.isBusy = true;
                    lastChanceCart.activeOrder = this;
                    System.out.println("t true");
                    return lastChanceCart;
                }
            }
//        }
        return null;
    }

    /**
     * checks if we can make a delivery
     *
     * @param cart    cart we shall test
     * @param storage storage we shal test
     * @return returns: if we can make a delivery
     */
    public boolean isDeliveryPossible(Cart cart, Storage storage) {
        double[] lengths = new double[3];
        findClosestStorages((int) customerId + MAIN.input.getStorageCount(), lengths, new ArrayList<>());
        double dist = Math.abs(lengths[0]);           //
        double timeToGoBack = Math.ceil(dist / cart.avgSpeed);

        double fullDist = Math.abs(2 * dist * (bagsAmount / cart.capacity) - dist);
        double FullTimeToFix;
        double waitStorageTillRefill = 0;


        if (dist < cart.remainDistBeforeFix) {
            FullTimeToFix = Math.abs((fullDist / cart.remainDistBeforeFix) * cart.fixTime);
            cart.remainDistBeforeFix -= fullDist;
        } else {
            if (cart.getDistanceBeforeFix() != cart.remainDistBeforeFix) {
                cart.startFix(customerId);
            }
//            System.out.println("problem 11");
            return false;
        }
        

        if (MAIN.time > deadline || cart.maxDistance < dist) {
            System.out.println("Cas: " + MAIN.time + ", Zakaznik: " + customerId + ", umrzl zimou, protoze jezdit s koleckem je hloupost, konec");
            return false;
        }

        if (this.orderGetTime > this.deadline) {
            System.out.println("Cas: " + MAIN.time + ", Zakaznik: " + customerId + ", umrzl zimou, protoze jezdit s koleckem je hloupost, konec");
            return false;
        }

        if(storage.bagCount < bagsAmount){
            System.out.println("Cas: " + MAIN.time + ", Zakaznik: " + customerId + ", umrzl zimou, protoze jezdit s koleckem je hloupost, konec");
            System.out.println("Sklad je prazdny a nemuze se naplnit");
            return false;
        }else if(storage.remainingBags < bagsAmount){
            waitStorageTillRefill = storage.refillStorage();
        }


        return startSimulMoving(cart, storage, dist, timeToGoBack, FullTimeToFix, waitStorageTillRefill);

    }

    private boolean startSimulMoving(Cart cart, Storage storage, double dist, double timeToGoBack, double FullTimeToFix, double waitStorageTillRefill) {
        double TimeTODeliver = Math.abs(dist / cart.avgSpeed +
                FullTimeToFix +                 //pod kommentvo bylo  closestStorages[0].
                (bagsAmount / cart.capacity) * storage.timeToRefillCart
                + bagsAmount * storage.timeToRefillCart
        );
        //test, mozzhet neverno

        if ((MAIN.time + TimeTODeliver) < this.deadline) {
            cart.remainingTimeToDeliver = Math.ceil(TimeTODeliver);
            cart.remainingTimeToDeliver += waitStorageTillRefill;
            cart.isBusy = true;
            cart.timeToGoBack = timeToGoBack;
            if (TimeTODeliver < 0) {
                throw new RuntimeException();
            }
            cart.returnTime = Math.ceil(Math.abs(TimeTODeliver + dist / cart.avgSpeed));
            cart.remainDistBeforeFix += waitStorageTillRefill;

            if (cart.storageF != null) {
                cart.storageF.remainingBags -= bagsAmount;
            } else {
                storage.remainingBags -= bagsAmount;
            }
            return true;
        } else {
            System.out.println("problem 3");
            return false;
        }
    }
//======================================================

//    private double shortestPathLength(double customerId) {
//        double length = Double.MAX_VALUE;
//        double[] arr = Dijkstra.dijkstra(MAIN.g, (int) customerId);
//        for (int i = 0; i < MAIN.input.getStorageCount() + 1; i++) {
//            if (i == customerId)
//                continue;
//            if (length > arr[i]) {
//                length = arr[i];
//            }
//        }
//        return length;
//    }

    /**
     * finds top-3 of the closest storages
     *
     * @param customerId
     * @param lengths
     * @param lengthsToVertex
     * @return
     */
    public Storage[] findClosestStorages(int customerId, double[] lengths, List<Double> lengthsToVertex) {
        List<Integer> vertexes = new ArrayList<>();
        lengthsToVertex.clear();
        double[] arr = Dijkstra.dijkstra(MAIN.g, customerId, vertexes);
        Storage[] storages = MAIN.input.getStorages();
        int storageCount = MAIN.input.getStorageCount();


        for (Integer vertex : vertexes) {
            lengthsToVertex.add(arr[vertex]);
        }

        if (storageCount == 1) {
            findClosestStorage(customerId, lengths, arr);
            return storages;
        }

        int[] indices = new int[3];
        double[] values = new double[3];

        processStorages(customerId, arr, storageCount, indices, values);

        System.arraycopy(values, 0, lengths, 0, 3);

        Storage[] closest = new Storage[3];
        for (int i = 0; i < 3; i++) {
            closest[i] = storages[indices[i]];
        }

        return closest;

    }

    /**
     * @param customerId   customer
     * @param arr
     * @param storageCount count of the closest storages
     * @param indices      indices
     * @param values       lengths array to index
     */
    private static void processStorages(int customerId, double[] arr, int storageCount, int[] indices, double[] values) {
        for (int i = 0; i < 3; i++) {
            indices[i] = -1;
            values[i] = Double.MAX_VALUE;
        }

        for (int i = 0; i < storageCount + 1; i++) {
            if (i == customerId) {
                continue;
            }

            for (int j = 0; j < 3; j++) {
                if (values[j] > arr[i]) {
                    // Update values and indices
                    for (int k = 2; k > j; k--) {
                        values[k] = values[k - 1];
                        indices[k] = indices[k - 1];
                    }
                    values[j] = arr[i];
                    indices[j] = i;
                    break;
                }
            }
        }
    }

    /**
     * finds top-1 the closest Storage
     *
     * @param customerId customer
     * @param lengths    length between customer and found Storage
     * @param arr        ways
     */
    private static void findClosestStorage(int customerId, double[] lengths, double[] arr) {
        double length = Double.MAX_VALUE;
        for (int i = 0; i < MAIN.input.getStorageCount() + 1; i++) {
            if (i == customerId) {
                continue;
            }
            if (length > arr[i]) {
                length = arr[i];
            }
        }
        lengths[0] = length;
    }

}
