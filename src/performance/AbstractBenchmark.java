package performance;

public abstract class AbstractBenchmark implements Benchmark {
    private long runDuration;

    @Override
    public synchronized void run() {
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
    public synchronized long getRunDuration() {
        return runDuration;
    }

    protected abstract void runBenchmark() throws Exception;
}
