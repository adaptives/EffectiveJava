package performance.locking;

import java.util.Collection;
import java.util.Iterator;

public final class SynchronizationBasedReaderWriterBenchmark extends AbstractLockingBenchmark {
    public SynchronizationBasedReaderWriterBenchmark(int readers, int writers, int readerIterations, int writerIterations,
            Collection<Integer> sharedData, long waitBetweenIterations) {
        super(readers, writers, readerIterations, writerIterations, sharedData, waitBetweenIterations);
    }

    @Override
    protected void iterate() {
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

    @Override
    protected void write(int i) {
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
