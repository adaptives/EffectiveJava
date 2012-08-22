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

/**
 * An implementation of Echo Server using Java NIO.
 */
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
            // Note we are using ServerSocketChannel instead of simple
            // ServerSocket.
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
            // Keep waiting for a client connection and register with selector
            // to be able to handle this clients requests.
            while (!stopped) {
                // Again, note we are using SocketChannel instead of simple
                // Socket.
                SocketChannel client = server.accept();

                // By default the SocketChannel is configured as blocking mode,
                // this operations ensures that we make the socket channel as
                // non-blocking. This is essential for us as we are using NIO.
                client.configureBlocking(false);

                // Prematurely wake up the selector as we want to register a new
                // clients, socket channel with it.
                selector.wakeup();

                // Synchronizing, to avoid SelectionWorker to go into select()
                // as we register this clients SocketChannel.
                synchronized (selector) {
                    // This registers the current channel with the selector, so
                    // that it can become part of the selection process of the
                    // selector.
                    client.register(selector, SelectionKey.OP_READ, new ClientContext(client));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This thread uses the selector to wait for the clients which has some
     * request to be read from, when it sees that some clients have sent a
     * request (Selector.select() call returning some keys being selected) the
     * thread will try to respond to the request of the client.
     */
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
                    // Synchronize to ensure that acceptor thread is not
                    // registering any channels.
                    synchronized (selector) {
                        // The Selector.select() call is the heart of the NIO.
                        // This call allows you to wait on a multiple channels
                        // where we may have something to read or write to. The
                        // select call blocks until it detects that it has at
                        // least one channel that can be operated upon.
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

                                    // The request processor should remove this
                                    // key when its done.
                                    processing.add(key);
                                    executor.execute(new RequestProcessor(context, processing, key));
                                } else {
                                    // Unlikely, but we will cancel the key in
                                    // case such an envent occurs.
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

    /**
     * Represents the session for the client. Keeps the partialy read requests
     * in the buffer until we can respond to them.
     */
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

        /**
         * Tries to send a response if at least one request has been read
         * completely. If it can send the response, then it removes that request
         * and slides the buffer in such a fashion that next request starts from
         * offset 0
         */
        public boolean sendResponse() {
            int i = 0;
            // This is the tricky part with NIO, since we are using non-blocking
            // I/O, read from the channel may or may not have read the complete
            // request from the client. We have to ensure that the request has
            // been read completely before we can respond to it. Although this
            // is not the most optimized way to do it, the following code tries
            // to find occurrence of a new line character and then read the
            // whole request until new line character and write the response for
            // this request. If there are no new line characters, the method
            // returns false to indicate no response was sent.
            //
            // presence of 0 is treated as end of buffer, i.e. no more data has
            // been read into the buffer.
            while (data[i] != 0 && i < data.length) {
                if ((char) data[i++] == '\n') {
                    ByteBuffer response = ByteBuffer.wrap(data, 0, i);

                    try {
                        channel.write(ByteBuffer.wrap(prefix));

                        // Even the write calls are non-blocking. If some client
                        // is slower, then we may not have actually written all
                        // the data to the channel. The write() method returns
                        // an integer indicating how many bytes were written. In
                        // case it could not write all the data, we may have to
                        // re attempt to write. We are not ensuring that all the
                        // data is written for the sake of simplicity.
                        channel.write(response);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }

                    slide(i);

                    // Sending true indicates that there was some response sent,
                    // which also implies that we may have already read few more
                    // requests.
                    return true;
                }
            }

            return false;
        }

        /**
         *  
         */
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
                // If we have read something from the client,
                if (context.read() > 0) {
                    // Then try sending response as long as there are pending
                    // requests in our buffer.
                    while (context.sendResponse())
                        ;
                } else {
                    // Cancel the key and remove from the selector if the client
                    // has terminated the connection.
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
