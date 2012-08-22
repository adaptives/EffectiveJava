package performance.locking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import performance.AbstractBenchmark;

/**
 * Abstraction for the locking benchmark. Provides the boiler plate code for reader / writer threads.
 */
public abstract class AbstractLockingBenchmark extends AbstractBenchmark {
    // Number of Reader threads to create.
    protected final int readers;

    // Number of Writer threads to create.
    protected final int writers;

    // Number of times each reader iterates through the shared collection.
    protected final int readerIterations;

    // Number of times each writer writes to the shared collection. 
    protected final int writerIterations;

    // Amount of milliseconds to wait between read / write operations.
    protected long waitBetweenIterations;

    // Collection that will be used by Readers / Writers to Read / Write to.
    protected Collection<Integer> sharedData;
    
    protected List<Thread> threads = new ArrayList<Thread>();
    protected final CyclicBarrier barrier;

    public AbstractLockingBenchmark(int readers, int writers, int readerIterations, int writerIterations, Collection<Integer> sharedData,
            long waitBetweenIterations) {
        super();
        this.readers = readers;
        this.writers = writers;
        this.readerIterations = readerIterations;
        this.writerIterations = writerIterations;
        this.sharedData = sharedData;
        this.waitBetweenIterations = waitBetweenIterations;
        this.barrier = new CyclicBarrier(readers + writers);
    }

    @Override
    public void init() {
        for (int i = 1; i <= readers; i++) {
            threads.add(new Thread(new Reader(), "Reader-" + i));
        }

        for (int i = 1; i <= writers; i++) {
            threads.add(new Thread(new Writer(), "Writer-" + i));
        }
    }

    @Override
    public void cleanup() {
        threads.clear();
        sharedData.clear();
    }

    @Override
    protected void runBenchmark() throws Exception {
        // Start all threads.
        for (Thread t : threads) {
            t.start();
        }

        // Wait for all threads to finish.
        for (Thread t : threads) {
            t.join();
        }
    }

    /**
     * The implementor should iterate the given collection with this callaback.
     */
    protected abstract void iterate();

    /**
     * The implementor should add the given item to the collection with this callback.
     */
    protected abstract void write(int i);

    /**
     * Implements a "Reader"
     */
    private final class Reader implements Runnable {
        @Override
        public void run() {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
                return;
            }

            for (int i = 1; i <= readerIterations; i++) {
                iterate();
            }
        }
    }

    /**
     * Implements a "Writer" 
     */
    private final class Writer implements Runnable {
        @Override
        public void run() {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
                return;
            }

            for (int i = 1; i <= writerIterations; i++) {
                write(i);
            }
        }
    }
}
