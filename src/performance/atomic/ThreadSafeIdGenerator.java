package performance.atomic;

public final class ThreadSafeIdGenerator implements IdGenerator {
    private long id;

    @Override
    public synchronized long nextId() {
        return id++;
    }
}
