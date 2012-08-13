package performance.locking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import performance.AbstractBenchmark;

public final class SynchronizationBasedReaderWriterBenchmark extends AbstractBenchmark {
    private final int readers;
    private final int writers;
    private final int readerIterations;
    private final int writerIterations;
    private long waitBetweenIterations;
    private Collection<Integer> sharedData;
    private List<Thread> threads = new ArrayList<Thread>();
    private final CountDownLatch latch;

    public SynchronizationBasedReaderWriterBenchmark(int readers, int writers, int readerIterations, int writerIterations,
            Collection<Integer> sharedData, long waitBetweenIterations) {
        super();
        this.readers = readers;
        this.writers = writers;
        this.readerIterations = readerIterations;
        this.writerIterations = writerIterations;
        this.sharedData = sharedData;
        this.waitBetweenIterations = waitBetweenIterations;
        this.latch = new CountDownLatch(readers + writers);
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
        for (Thread t : threads) {
            t.start();
        }

        // Wait for all threads to finish.
        for (Thread t : threads) {
            t.join();
        }
    }

    private final class Reader implements Runnable {
        @Override
        public void run() {
            latch.countDown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 1; i <= readerIterations; i++) {
                // Acquire lock and iterate.
                synchronized (sharedData) {
                    Iterator<Integer> itr = sharedData.iterator();
                    while (itr.hasNext()) {
                        itr.next();

                        // This makes the benchmark more consistent
                        try {
                            Thread.sleep(waitBetweenIterations);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    }

    private final class Writer implements Runnable {
        @Override
        public void run() {
            latch.countDown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 1; i <= writerIterations; i++) {
                // Acquire lock and add.
                synchronized (sharedData) {
                    sharedData.add(i);

                    // This makes the benchmark more consistent
                    try {
                        Thread.sleep(waitBetweenIterations);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
