package threads;

import java.util.ArrayList;
import java.util.List;

/**
 * This example demonstrates the use and need of Thread.join() method with use
 * of a hypothetical Parking Lot.
 * 
 */
public final class ImportanceOfJoinExample {

    public static void main(String[] args) {
        ParkingLot parkingLot = new ParkingLot(2);

        System.out.println("Opening Parking Lot.");
        parkingLot.open();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Closing Parking Lot.");
        parkingLot.close();
    }

    /**
     * A hypothetical Parking Lot which has multiple entrances represented by
     * {@link ParkingLotEntrance}. Each parking lot entrance is a thread which
     * will wait for a car and then park the car in the parking lot. The parking
     * lot can be opened and closed. Opening the parking lot subsequently opens
     * all the entrances i.e. starts the entrance threads. Closing the parking
     * lot sets the state of the parking lot as closed and no more cars can be
     * parked after the parking lot is closed.
     */
    private static final class ParkingLot {
        private int currentlyParked;
        private boolean closed = false;
        private final List<ParkingLotEntrance> entrances;

        public ParkingLot(int totalEntrances) {
            super();
            this.entrances = new ArrayList<ParkingLotEntrance>(totalEntrances);

            for (int i = 1; i <= totalEntrances; i++) {
                entrances.add(new ParkingLotEntrance(this, "Entrance-" + i));
            }
        }

        public void open() {
            for (ParkingLotEntrance entrance : entrances) {
                entrance.open();
            }
        }

        public void close() {
            for (ParkingLotEntrance entrance : entrances) {
                entrance.close();
            }

            this.closed = true;
        }

        public void park() {
            if (closed) {
                // Parking lot is closed, throw an exception.
                throw new IllegalStateException("Parking Lot is CLOSED!");
            }

            // Park a car.
            currentlyParked++;
        }
    }

    /**
     * Represents the Entrance of the Parking Lot.
     */
    private static final class ParkingLotEntrance implements Runnable {
        private final ParkingLot parkingLot;
        private boolean closed = false;

        private final Thread thread;

        public ParkingLotEntrance(ParkingLot parkingLot, String name) {
            super();
            this.parkingLot = parkingLot;
            thread = new Thread(this, name);
        }

        @Override
        public void run() {
            while (!closed) {
                // Wait for a car.
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                parkingLot.park();
                System.out.println("[" + Thread.currentThread().getName() + "] Parked a Car.");
            }
        }

        public void open() {
            thread.start();
        }

        public void close() {
            closed = true;
        }
    }
}
