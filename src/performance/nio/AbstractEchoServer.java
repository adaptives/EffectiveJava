package performance.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.Executor;

public abstract class AbstractEchoServer implements EchoServer, Runnable {
    protected Executor executor;
    protected ServerSocket server;
    protected final int port;
    protected final Thread thread = new Thread(this);
    protected boolean stopped = false;

    public AbstractEchoServer(Executor executor, int port) {
        this.executor = executor;
        this.port = port;
    }

    @Override
    public final void start() {
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress("localhost", port));

            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void stop() {
        stopped = true;
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
