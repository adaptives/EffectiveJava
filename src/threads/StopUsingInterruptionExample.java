package threads;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class StopUsingInterruptionExample {
    public static void main(String[] args) throws IOException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(5);
        QueueReadWorker worker = new QueueReadWorker(queue);

        System.out.println("Starting Worker.");
        worker.start();
        
        System.out.println("Adding items to queue.");
        for (int i = 1; i <= 5; i++) {
            queue.add(i);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Trying to stop the Worker.");
        worker.forceStop();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        if (worker.isAlive()) {
            System.out.println("Worker is still alive!");
        }

        System.out.println("Interrupting the Worker.");
        worker.interrupt();
        try {
            worker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (worker.isAlive()) {
            System.out.println("Worker is still alive!");
        } else {
            System.out.println("Worker has stopped.");
        }
    }

    private static final class QueueReadWorker extends Thread {
        private final BlockingQueue<Integer> queue;
        private boolean stopped = false;

        public QueueReadWorker(BlockingQueue<Integer> queue) {
            super();
            this.queue = queue;
        }

        public void forceStop() {
            this.stopped = true;
        }

        @Override
        public void run() {
            while (!stopped) {
                Integer i;
                try {
                    i = queue.take();
                    System.out.println("Dequeued: " + i);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
