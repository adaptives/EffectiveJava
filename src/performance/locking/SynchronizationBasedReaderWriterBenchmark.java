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
        // Acquire lock and iterate. This will lock other readers as well as
        // writers.
        synchronized (sharedData) {
            Iterator<Integer> itr = sharedData.iterator();
            while (itr.hasNext()) {
                itr.next();

                // This will magnify the effect of contention as we are sleeping
                // within synchronized block.
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
        // Acquire lock and add. This will lock other readers as well as
        // writers.
        synchronized (sharedData) {
            sharedData.add(i);

            try {
                Thread.sleep(waitBetweenIterations);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
