package performance.atomic;

import performance.Benchmark;

public final class LockFreeConcurrencyDemo {

    public static void main(String[] args) {
        int workers = 50;
        int iterations = 500000;

        for (int i = 1; i <= 5; i++) {
            Benchmark b = new SynchronizationBasedIdGeneratorBenchmark(workers, iterations);
            b.run();
            System.out.println(b.getClass().getSimpleName() + " took " + b.getRunDuration() + "ms");
        }

        for (int i = 1; i <= 5; i++) {
            Benchmark b = new AtomicLongBasedIdGeneratorBenchmark(workers, iterations);
            b.run();
            System.out.println(b.getClass().getSimpleName() + " took " + b.getRunDuration() + "ms");
        }
    }
}
