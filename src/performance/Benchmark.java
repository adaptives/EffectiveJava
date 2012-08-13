package performance;

public interface Benchmark {
    void run();
    void init();
    void cleanup();
    long getRunDuration();
} 
