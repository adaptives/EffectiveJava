package performance.atomic;

/**
 * Generates an unique id with every invocation of {@link IdGenerator#nextId()}
 *
 */
public interface IdGenerator {
    /**
     * Generates a unique id.
     *
     * @return long
     */
    long nextId();
}
