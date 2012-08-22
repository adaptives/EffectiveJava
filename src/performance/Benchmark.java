package performance;

/**
 *
 * Abstracts a benchmark to measure performance.
 *
 */
public interface Benchmark {
    /**
     * Runs the benchmark.
     */
    void run();

    /**
     * Initializes the benchmark if needed.
     */
    void init();

    /**
     * Performs any cleanup if required after the benchmark is run.
     */
    void cleanup();

    /**
     * Get the duration in milliseconds, the benchmark took to run.
     * 
     * @return long
     */
    long getRunDuration();
} 
