package threads;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates the dead-locking Reader and Writer by means of incorrect synchronization. 
 */
public final class DeadLockExample {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1);
        Reader reader = new Reader(queue);
        Writer writer = new Writer(queue);

        System.out.println("Starting Reader and Writer.");

        reader.start();
        writer.start();

        Thread.sleep(2000);

        System.out.println("Stopping Reader and Writer.");
        
        synchronized (queue) {
            reader.stop();
            writer.stop();
        }


        System.out.println("Reader and Writer stopped.");
    }

    private static final class Reader implements Runnable {
        private final BlockingQueue<String> queue;
        private final Thread t = new Thread(this, "Reader");
        private boolean stopped = false;

        public Reader(BlockingQueue<String> queue) {
            super();
            this.queue = queue;
        }

        @Override
        public void run() {
            while (!stopped) {
                synchronized (queue) {
                    try {
                        String s = queue.take();
                        System.out.println("Read: " + s);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }

        public void start() {
            t.start();
        }

        public void stop() {
            stopped = true;
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static final class Writer implements Runnable {
        private final BlockingQueue<String> queue;
        private final Thread t = new Thread(this, "Reader");
        private boolean stopped = false;

        public Writer(BlockingQueue<String> queue) {
            super();
            this.queue = queue;
        }

        @Override
        public void run() {
            while (!stopped) {
                synchronized (queue) {
                    try {
                        queue.put("foo");
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }

        public void start() {
            t.start();
        }

        public void stop() {
            stopped = true;
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

