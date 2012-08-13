package performance.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import performance.AbstractBenchmark;

public final class SynchronizationBasedIdGeneratorBenchmark extends AbstractBenchmark {
    private long id = 0;
    private final Object mutex = new Object();
    private final CountDownLatch latch;
    private final int workers;
    private final int iterations;
    private final List<Thread> threads = new ArrayList<Thread>();

    
    public SynchronizationBasedIdGeneratorBenchmark(int workers, int iterations) {
        super();
        this.workers = workers;
        this.latch = new CountDownLatch(workers);
        this.iterations = iterations;
    }

    public long nextId() {
        synchronized (mutex) {
            return id++;
        }
    }

    @Override
    public void init() {
        for (int i = 1; i <= workers; i++) {
            threads.add(new Thread(new Worker()));
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    protected void runBenchmark() throws Exception {
        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
    }

    private final class Worker implements Runnable {
        @Override
        public void run() {
            latch.countDown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 1; i <= iterations; i++) {
                nextId();
            }
        }
    }
}
