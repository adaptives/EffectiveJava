package performance;

/**
 * Provides a skeleton implementation for the Benchmark. 
 *
 */
public abstract class AbstractBenchmark implements Benchmark {
    private long runDuration;

    @Override
    public final synchronized void run() {
        init();
        try {
            long start = System.currentTimeMillis();
            runBenchmark();
            runDuration = System.currentTimeMillis() - start;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    @Override
    public final synchronized long getRunDuration() {
        return runDuration;
    }

    /**
     * Should be implemented by classes extending this class to put the logic of running the benchmark.
     * @throws Exception
     */
    protected abstract void runBenchmark() throws Exception;
}
