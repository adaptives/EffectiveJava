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
        // Acquire lock and iterate. This will block anyone willing to acquire
        // the write lock, but allows anyone to create a read lock.
        lock.readLock().lock();
        try {
            Iterator<Integer> itr = sharedData.iterator();
            while (itr.hasNext()) {
                itr.next();

                // This will magnify the effect of contention as we are sleeping
                // after acquiring the lock.
                try {
                    Thread.sleep(waitBetweenIterations);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            // Always release the lock in a finally block to ensure you do not
            // run into deadlock situation in event of an exception.
            lock.readLock().unlock();
        }
    }

    @Override
    protected void write(int i) {
        // Acquire lock and add. This will block both threads trying to acquire a read or write lock.
        lock.writeLock().lock();
        try {
            sharedData.add(i);

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
