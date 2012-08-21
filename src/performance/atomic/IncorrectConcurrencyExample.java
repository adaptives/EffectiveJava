package performance.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * This example demonstrates how a simple operation like i++ is not thread safe.
 * 
 * We have a class {@link IncorrectConcurrencyExample} and a shared long
 * attribute counter. This example spawns a number of threads to concurrently
 * increment the shared counter.
 * 
 */
public final class IncorrectConcurrencyExample {
    private long counter = 0;
    private final int workers;
    private final CyclicBarrier barrier;

    public IncorrectConcurrencyExample(int workers) {
        this.workers = workers;
        this.barrier = new CyclicBarrier(workers);
    }

    public void run() {
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 1; i <= workers; i++) {
            threads.add(new Thread(new IncrementWorker()));
        }

        for (Thread t : threads) {
            t.start();
        }

        // Wait for all threads to finish.
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Counter: " + counter);
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            IncorrectConcurrencyExample demo = new IncorrectConcurrencyExample(4);
            demo.run();
        }
    }

    private final class IncrementWorker implements Runnable {
        @Override
        public void run() {
            // Ensures that all the worker threads start and wait at the barrier
            // and only proceed when all the other workers have reached this
            // barrier.
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            for (int i = 1; i <= 100000; i++) {
                counter++;
            }
        }
    }
}
