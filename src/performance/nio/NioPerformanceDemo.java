package performance.nio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This benchmark compares the conventional I/O vs the new Java NIO performance
 * using an EchoServer implemented using both conventional Java I/O and NIO.
 * 
 */
public final class NioPerformanceDemo {

    public static void main(String[] args) throws Exception {
        int port = 5000;
        int serverRequestHandlerThreads = 2;
        int clientThreads = 100;
        int clientIterations = 10;
        boolean waitBetweenIterations = true;

        // Wait duration in nanoseconds.
        long waitDuration = 10000000;
        ExecutorService executor = Executors.newFixedThreadPool(serverRequestHandlerThreads);

        for (int i = 1; i <= 5; i++) {
            EchoServer server = new SimpleEchoServer(executor, port);
            EchoServerBenchmark b = new EchoServerBenchmark(server, clientThreads, clientIterations, port, waitBetweenIterations,
                    waitDuration);
            b.run();
            System.out.println(server.getClass().getSimpleName() + " took " + b.getRunDuration() + "ms");
        }

        executor.shutdown();

        executor = Executors.newFixedThreadPool(serverRequestHandlerThreads);

        for (int i = 1; i <= 5; i++) {
            EchoServer server = new NioBasedEchoServer(executor, port);
            EchoServerBenchmark b = new EchoServerBenchmark(server, clientThreads, clientIterations, port, waitBetweenIterations,
                    waitDuration);
            b.run();
            System.out.println(server.getClass().getSimpleName() + " took " + b.getRunDuration() + "ms");
        }

        executor.shutdown();
    }
}
