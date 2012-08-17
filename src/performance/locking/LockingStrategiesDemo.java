package performance.locking;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import performance.Benchmark;

public final class LockingStrategiesDemo {
    public static void main(String[] args) {
        int readers = 10;
        int writers = 3;
        int readIterations = 10;
        int writeIterations = 5;
        long waitBetweenIterations = 2;
        Collection<Integer> sharedData = new LinkedList<Integer>();
        
        Benchmark b = null;

        for (int i = 1; i <= 5; i++) {
            sharedData.clear();
            for (int j = 1; j<= 5; j++) {
                sharedData.add(j);
            }

            b = new SynchronizationBasedReaderWriterBenchmark(readers, writers, readIterations, writeIterations, sharedData, waitBetweenIterations);
            b.run();
            System.out.println(b.getClass().getSimpleName() + " took " + b.getRunDuration() + "ms");
        }

        for (int i = 1; i <= 5; i++) {
            sharedData.clear();
            for (int j = 1; j<= 5; j++) {
                sharedData.add(j);
            }

            b = new ReadWriteLockBasedReaderWriterBenchmark(readers, writers, readIterations, writeIterations, sharedData, waitBetweenIterations);
            b.run();
            System.out.println(b.getClass().getSimpleName() + " took " + b.getRunDuration() + "ms");
        }

        sharedData = new CopyOnWriteArrayList<Integer>();
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j<= 5; j++) {
                sharedData.add(j);
            }

            b = new LockFreeReaderWriterBenchmark(readers, writers, readIterations, writeIterations, sharedData, waitBetweenIterations);
            b.run();
            System.out.println(b.getClass().getSimpleName() + " took " + b.getRunDuration() + "ms");
        }

    }
}
