package performance.atomic;

/**
 * 
 * An {@link IdGenerator} implementation which is thread-safe. The thread safety
 * is implemented using simple synchronization.
 */
public final class ThreadSafeIdGenerator implements IdGenerator {
    private long id;

    @Override
    public synchronized long nextId() {
        return id++;
    }
}
