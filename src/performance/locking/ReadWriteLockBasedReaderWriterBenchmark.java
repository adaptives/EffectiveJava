package performance.locking;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class ReadWriteLockBasedReaderWriterBenchmark extends AbstractLockingBenchmark {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public ReadWriteLockBasedReaderWriterBenchmark(int readers, int writers, int readerIterations, int writerIterations,
            Collection<Integer> sharedData, long waitBetweenIterations) {
        super(readers, writers, readerIterations, writerIterations, sharedData, waitBetweenIterations);
    }

    @Override
    protected void iterate() {
        // Acquire lock and iterate.
        lock.readLock().lock();
        try {
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
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    protected void write(int i) {
        // Acquire lock and add.
        lock.writeLock().lock();
        try {
            sharedData.add(i);

            // This makes the benchmark more consistent
            try {
                Thread.sleep(waitBetweenIterations);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
