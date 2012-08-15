package performance.atomic;

import java.util.concurrent.atomic.AtomicLong;

public final class LockFreeIdGenerator implements IdGenerator {
    private final AtomicLong id = new AtomicLong();

    @Override
    public long nextId() {
        return id.getAndIncrement();
    }

}
