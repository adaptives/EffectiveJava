package performance.atomic;

import performance.Benchmark;

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
