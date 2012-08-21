package performance.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import performance.AbstractBenchmark;

public class IdGeneratorBenchmark extends AbstractBenchmark {
    private final int workers;
    private final int iterations;
    private final List<Thread> threads = new ArrayList<Thread>();
    private final CyclicBarrier barrier;
    private final IdGenerator idgen;
    
    public IdGeneratorBenchmark(IdGenerator idgen, int workers, int iterations) {
        super();
        this.idgen = idgen;
        this.workers = workers;
        this.barrier = new CyclicBarrier(workers);
        this.iterations = iterations;
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
            // Ensures that all the worker threads start and wait at the barrier
            // and only proceed when all the other workers have reached this
            // barrier.
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
                return;
            }

            for (int i = 1; i <= iterations; i++) {
                idgen.nextId();
            }
        }
    }
}
