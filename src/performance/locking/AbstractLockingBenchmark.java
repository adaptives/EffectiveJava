package performance.locking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import performance.AbstractBenchmark;

public abstract class AbstractLockingBenchmark extends AbstractBenchmark {
    protected final int readers;
    protected final int writers;
    protected final int readerIterations;
    protected final int writerIterations;
    protected long waitBetweenIterations;
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

    protected abstract void iterate();
    protected abstract void write(int i);

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
