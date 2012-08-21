package performance.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

public final class NioBasedEchoServer implements Runnable, EchoServer {
    protected Executor executor;
    protected ServerSocketChannel server;
    protected final int port;
    protected final Thread clientAcceptor = new Thread(this);
    protected volatile boolean stopped = false;
    private final SelectionWorker selectionWorker;
    private final Selector selector;

    public final void start() {
        try {
            server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress("localhost", port));

            clientAcceptor.start();
            selectionWorker.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final void stop() {
        stopped = true;
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            clientAcceptor.join();
            selectionWorker.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public NioBasedEchoServer(Executor executor, int port) throws IOException {
        this.executor = executor;
        this.port = port;
        this.selector = Selector.open();
        this.selectionWorker = new SelectionWorker(selector, executor);
    }

    @Override
    public void run() {
        try {
            while (!stopped) {
                SocketChannel client = server.accept();
                client.configureBlocking(false);

                selector.wakeup();
                synchronized (selector) {
                    client.register(selector, SelectionKey.OP_READ, new ClientContext(client));
                }
            }
        } catch (IOException e) {

        }
    }

    private static final class SelectionWorker implements Runnable {
        private final Selector selector;
        private volatile boolean stopped = false;
        private final Executor executor;
        private final Set<SelectionKey> processing = Collections.synchronizedSet(new HashSet<SelectionKey>());
        private final Thread thread = new Thread(this);

        public SelectionWorker(Selector selector, Executor executor) {
            super();
            this.selector = selector;
            this.executor = executor;
        }

        public void start() {
            thread.start();
        }

        public void stop() {
            stopped = true;
            selector.wakeup();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (!stopped) {
                try {
                    int selectionCount = 0;
                    synchronized (selector) {
                        selectionCount = selector.select();
                    }

                    if (selectionCount > 0) {
                        Iterator<SelectionKey> itr = selector.selectedKeys().iterator();
                        while (itr.hasNext()) {
                            SelectionKey key = itr.next();
                            if (!processing.contains(key)) {
                                Object attachment = key.attachment();
                                if (attachment != null && attachment instanceof ClientContext) {
                                    ClientContext context = (ClientContext) attachment;

                                    // The request processor should remove this key when its done.
                                    processing.add(key);
                                    executor.execute(new RequestProcessor(context, processing, key));
                                } else {
                                    // Unlikely, but we will cancel the key in case such an envent occurs.
                                    key.cancel();
                                }
                            }

                            itr.remove();
                        }
                    } else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

    }

    private static final class ClientContext {
        private final byte data[] = new byte[512];
        private final ByteBuffer buffer = ByteBuffer.wrap(data);
        private SocketChannel channel;
        private final byte prefix[] = "Server: ".getBytes();

        public ClientContext(SocketChannel channel) {
            super();
            this.channel = channel;
        }

        public int read() throws IOException {
            return channel.read(buffer);
        }

        public boolean sendResponse() {
            int i = 0;
            while (data[i] != 0 && i < data.length) {
                if ((char) data[i++] == '\n') {
                    ByteBuffer response = ByteBuffer.wrap(data, 0, i);

                    try {
                        channel.write(ByteBuffer.wrap(prefix));
                        channel.write(response);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }

                    //System.out.println("Before slide: " + new String(data));
                    slide(i);
                    //System.out.println("After slide: " + new String(data));
                    return true;
                }
            }

            return false;
        }

        private void slide(int index) {
            int i = index;
            int j = 0;
            while (i < data.length && data[i] > 0) {
                data[j++] = data[i++];
            }

            int position = j;

            // Erase rest of the data
            while (j < data.length) {
                data[j++] = (byte) 0;
            }

            buffer.position(position);
        }
    }

    private static final class RequestProcessor implements Runnable {
        private final ClientContext context;
        private final Set<SelectionKey> processing;
        private final SelectionKey key;

        public RequestProcessor(ClientContext context, Set<SelectionKey> processing, SelectionKey key) {
            super();
            this.context = context;
            this.processing = processing;
            this.key = key;
        }

        @Override
        public void run() {
            try {
                if (context.read() > 0) {
                    while (context.sendResponse())
                        ;
                } else {
                    key.cancel();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                processing.remove(key);
            }
        }
    }
}
