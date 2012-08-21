package performance.io;

import java.io.OutputStream;

import performance.AbstractBenchmark;

public final class IoBenchmark extends AbstractBenchmark {
    private final OutputStream out;
    private final int iterations;
    private final byte data[] = "Quick brown fox jumped over a lazy dog!\n".getBytes();

    public IoBenchmark(OutputStream out, int iterations) {
        super();
        this.out = out;
        this.iterations = iterations;
    }

    @Override
    public void init() {

    }

    @Override
    public void cleanup() {

    }

    @Override
    protected void runBenchmark() throws Exception {
        for (int i = 1; i <= iterations; i++) {
            out.write(data);
        }

        // Note: in case of buffered streams, the data written may still be in
        // buffer and may not have been written to disk. If you need to ensure
        // that data written to the stream actually is written to the underlying
        // device / socket, then its necessary to call the flush method.
        out.flush();
    }
}
