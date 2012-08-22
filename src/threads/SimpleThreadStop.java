package threads;

/**
 * An example for stopping a thread using a simple flag.
 */
public final class SimpleThreadStop {

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

    /**
     * Worker thread that keeps working until it is stopped using the stop
     * method.
     */
    private static final class Worker {
        private boolean stopped = false;

        // Its not a good practice to extend your classes from Thread class. If
        // you implement a thread by extending the Thread class, you expose
        // Thread methods to the clients of your API. This can allow people to
        // start the thread prematurely (depends on design) or change the name
        // or priority of the thread, or worst interrupt the thread.
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
