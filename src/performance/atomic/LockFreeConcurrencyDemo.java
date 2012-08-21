package performance.atomic;

import performance.Benchmark;

/**
 * This example demonstrates the use of Atomic types available in java to take
 * achieve lock-free concurrency.
 * 
 * Atomic types provide APIs for atomically changing values of the underlying
 * primitive value without any locking or synchronization. Atomic data types are
 * a good choice when the operation thats needs to be performed atomically to
 * the underlying data is trivial (like incrementing the underlying primitive)
 * Atomic data types may not scale very well in case where the operations are
 * computation intensive.
 * 
 */
public final class LockFreeConcurrencyDemo {

    public static void main(String[] args) {
        int workers = 50;
        int iterations = 500000;

        for (int i = 1; i <= 5; i++) {
            IdGenerator idgen = new ThreadSafeIdGenerator();
            Benchmark b = new IdGeneratorBenchmark(idgen, workers, iterations);
            b.run();
            System.out.println(idgen.getClass().getSimpleName() + " took " + b.getRunDuration() + "ms");
        }

        for (int i = 1; i <= 5; i++) {
            IdGenerator idgen = new LockFreeIdGenerator();
            Benchmark b = new IdGeneratorBenchmark(idgen, workers, iterations);
            b.run();
            System.out.println(idgen.getClass().getSimpleName() + " took " + b.getRunDuration() + "ms");
        }
    }
}
