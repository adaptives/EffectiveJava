package performance.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import performance.Benchmark;

public final class BufferedIoPerformanceDemo {
    public static void main(String[] args) throws IOException {
        int iterations = 1000000;
        File file = File.createTempFile("iobenchmark1", "");
        file.deleteOnExit();

        OutputStream out = new FileOutputStream(file);

        for (int i = 1; i <= 5; i++) {
            Benchmark b = new IoBenchmark(out, iterations);
            b.run();
            System.out.println(out.getClass().getSimpleName() + " took " + b.getRunDuration() + "ms");
        }

        out.close();

        file = File.createTempFile("iobenchmark2", "");
        file.deleteOnExit();
        out = new BufferedOutputStream(new FileOutputStream(file), 1000);

        for (int i = 1; i <= 5; i++) {
            Benchmark b = new IoBenchmark(out, iterations);
            b.run();
            System.out.println(out.getClass().getSimpleName() + " took " + b.getRunDuration() + "ms");
        }

    }
}
