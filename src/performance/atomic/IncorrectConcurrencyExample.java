package performance.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public final class IncorrectConcurrencyExample {
    private int counter = 0;
    private final int workers;
    private final CountDownLatch latch;

    public IncorrectConcurrencyExample(int workers) {
        this.workers = workers;
        this.latch = new CountDownLatch(workers);
    }

    public void run() {
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 1; i <= workers; i++) {
            threads.add(new Thread(new IncrementWorker()));
        }

        for (Thread t : threads) {
            t.start();
        }

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
            latch.countDown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 1; i <= 100000; i++) {
                counter++;
            }
        }
    }
}
