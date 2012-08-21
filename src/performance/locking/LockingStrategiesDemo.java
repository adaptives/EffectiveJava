package performance.locking;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import performance.Benchmark;

/**
 * An example which benchmarks Synchronization vs Read-Write Locks vs Lock Free
 * Collections.
 * 
 */
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
            for (int j = 1; j <= 5; j++) {
                sharedData.add(j);
            }

            b = new SynchronizationBasedReaderWriterBenchmark(readers, writers, readIterations, writeIterations, sharedData,
                    waitBetweenIterations);
            b.run();
            System.out.println(b.getClass().getSimpleName() + " took " + b.getRunDuration() + "ms");
        }

        for (int i = 1; i <= 5; i++) {
            sharedData.clear();
            for (int j = 1; j <= 5; j++) {
                sharedData.add(j);
            }

            b = new ReadWriteLockBasedReaderWriterBenchmark(readers, writers, readIterations, writeIterations, sharedData,
                    waitBetweenIterations);
            b.run();
            System.out.println(b.getClass().getSimpleName() + " took " + b.getRunDuration() + "ms");
        }

        // CopyOnWriteArrayList, as name suggest, creates a new underlying data
        // array when any item is added, removed or set. This in effect, lets
        // existing iterators to safely iterate on the snapshot that they
        // acquired iterator on without worrying about concurrent modification.
        //
        // Beware, this also means that any modification happening to the
        // collection will not be visible in the iterator currently active. You
        // cannot perform remove() operation on the iterator. This collection is
        // good choice when number of readers vastly outperform the writers.
        sharedData = new CopyOnWriteArrayList<Integer>();

        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                sharedData.add(j);
            }

            b = new LockFreeReaderWriterBenchmark(readers, writers, readIterations, writeIterations, sharedData, waitBetweenIterations);
            b.run();
            System.out.println(b.getClass().getSimpleName() + " took " + b.getRunDuration() + "ms");
        }

    }
}
