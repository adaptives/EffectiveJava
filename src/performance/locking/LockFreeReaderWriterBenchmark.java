package performance.locking;

import java.util.Collection;
import java.util.Iterator;

public final class LockFreeReaderWriterBenchmark extends AbstractLockingBenchmark {

    public LockFreeReaderWriterBenchmark(int readers, int writers, int readerIterations, int writerIterations,
            Collection<Integer> sharedData, long waitBetweenIterations) {
        super(readers, writers, readerIterations, writerIterations, sharedData, waitBetweenIterations);
    }

    @Override
    protected void iterate() {
        // Note: No synchronization or read-write locks.
        Iterator<Integer> itr = sharedData.iterator();
        while (itr.hasNext()) {
            itr.next();

            try {
                Thread.sleep(waitBetweenIterations);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void write(int i) {
        sharedData.add(i);

        try {
            Thread.sleep(waitBetweenIterations);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
