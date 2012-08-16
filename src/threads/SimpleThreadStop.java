package threads;

import java.io.PrintStream;

public final class SimpleThreadStop {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Worker worker = new Worker();

        System.out.println("Starting worker.");
        worker.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Stopping worker.");
        worker.stop();
        System.out.println("Worker stopped.");
    }

    private static final class Worker {
        private boolean stopped = false;
        private final Thread t = new Thread() {
            @Override
            public void run() {
                while (!stopped) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Working..");
                }
            }
        };

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
