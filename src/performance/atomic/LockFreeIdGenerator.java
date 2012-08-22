package performance.atomic;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of {@link IdGenerator} using {@link AtomicLong}
 * 
 */
public final class LockFreeIdGenerator implements IdGenerator {
    private final AtomicLong id = new AtomicLong();

    @Override
    public long nextId() {
        // The getAndIncrement() method first gets the existing value,
        // increments it and then tries to set the incremented value to the
        // underlying variable, but before setting it, it checks whether the
        // existing value is i - 1 or not. If the existing value is not i - 1,
        // it may have changed, in that case it repeats the same operation
        // again. Effectively, the atomic data type will attempt to perform this
        // operation until it is sure that it has modified the value atomically.
        return id.getAndIncrement();
    }

}
