package performance.nio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.LockSupport;

import performance.AbstractBenchmark;

public final class EchoServerBenchmark extends AbstractBenchmark {
    private final EchoServer server;
    private final int workers;
    private final int iterations;
    private final int port;
    private final List<Thread> threads = new ArrayList<Thread>();
    private final CyclicBarrier barrier;
    private boolean waitBetweenIterations;
    private long waitDuration;

    public EchoServerBenchmark(EchoServer server, int workers, int iterations, int port, boolean waitBetweenIterations, long waitDuration) {
        super();
        this.server = server;
        this.workers = workers;
        this.iterations = iterations;
        this.port = port;
        this.barrier = new CyclicBarrier(this.workers);
        this.waitBetweenIterations = waitBetweenIterations;
        this.waitDuration = waitDuration;
    }

    @Override
    public void init() {
        for (int i = 1; i <= workers; i++) {
            threads.add(new Thread(new ClientSimulator()));
        }

        server.start();
    }

    @Override
    public void cleanup() {
        server.stop();
    }

    @Override
    protected void runBenchmark() throws Exception {
        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
    }

    private final class ClientSimulator implements Runnable {
        @Override
        public void run() {
            Socket s = new Socket();
            try {
                s.connect(new InetSocketAddress("localhost", port));

                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

                for (int i = 1; i <= iterations; i++) {
                    writer.write("Hello " + i + "\n");
                    writer.flush();
                    reader.readLine();

                    if (waitBetweenIterations) {
                        LockSupport.parkNanos(waitDuration);
                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (s != null && s.isConnected()) {
                    try {
                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
