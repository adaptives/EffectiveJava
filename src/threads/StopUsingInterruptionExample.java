package threads;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * This example demonstrates how to stop a thread by interrupting it. Threads
 * can be stopped by calling Thread.interrupt() method when the thread is either
 * stuck in Object.wait() or Thread.sleep() In such circumstances, ordinary
 * flags do not work as the thread my not proceed execution to check the
 * condition until wakes from the blocking call. To wake up the thread
 * forcefully from the wait, you can call interrupt which causes the thread to
 * encounter an InterruptedException which the thread needs to respond to by
 * terminating its execution.
 */
public final class StopUsingInterruptionExample {
    public static void main(String[] args) throws IOException {
        // Blocking queues can be used in situations where you have a typical
        // producer consumer pattern. The blocking queue has a size, which if
        // exceeds (queue is full), it will block any producer from adding
        // anything into the queue. If the queue is empty, the consumers are
        // blocked until someone adds an item to the queue.
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(5);
        QueueReadWorker worker = new QueueReadWorker(queue);

        System.out.println("Starting Worker.");
        worker.start();

        // We will 5 elements to the queue.
        System.out.println("Adding items to queue.");
        for (int i = 1; i <= 5; i++) {
            queue.add(i);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // We try to stop the worker by settings its stopped flag.
        System.out.println("Trying to stop the Worker.");
        worker.forceStop();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Thread is actually not stopped as its blocked in the call of
        // queue.take()
        if (worker.isAlive()) {
            System.out.println("Worker is still alive!");
        }

        // We interrupt the thread, this will cause the thread to encounter an
        // InterruptedException.
        System.out.println("Interrupting the Worker.");
        worker.interrupt();

        // Join to ensure that thread has exited its run() method.
        try {
            worker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the thread has actually terminated.
        if (worker.isAlive()) {
            System.out.println("Worker is still alive!");
        } else {
            System.out.println("Worker has stopped.");
        }
    }

    /**
     * This worker thread dequeues items from the shared queue and prints them.
     */
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
                    // Queue.take() blocks the thread until it can dequeue an
                    // item. There are other methods available to try dequeuing
                    // an item with a timeout, if the timeout is specified, the
                    // queue will either dequeue an item or return null after
                    // the timeout has reached.
                    i = queue.take();
                    System.out.println("Dequeued: " + i);
                } catch (InterruptedException e) {
                    // Ensure that we respond to the InterruptedException by
                    // ensuring that we are terminating the run() method, in
                    // turn terminating the thread. In this particular case we
                    // do not need to necessarily return, it is just here for
                    // the sake of clarity.
                    return;
                }
            }
        }
    }
}
