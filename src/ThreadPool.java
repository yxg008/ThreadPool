import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
    private final int nThreads;  // Number of threads in the thread pool
    private final PoolWorker[] threads;  // Array to hold the worker threads
    private final LinkedBlockingQueue<Runnable> queue;  // Task queue
    private volatile boolean isStopped = false;  // Flag to indicate if the thread pool is stopped

    // Default constructor
    public ThreadPool() {
        this(10);  // Default pool size is 10
    }

    // Parameterized constructor
    public ThreadPool(int nThreads) {
        this.nThreads = nThreads;  // Set the number of threads
        this.queue = new LinkedBlockingQueue<>();  // Initialize the task queue
        this.threads = new PoolWorker[nThreads];  // Initialize the worker thread array

        // Create and start the worker threads
        for (int i = 0; i < nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }

    // Method to add a task to the thread pool
    public void add(Runnable task) {
        if (!isStopped) {  // Check if the pool is stopped
            synchronized (queue) {  // Lock the task queue
                queue.add(task);  // Add the task
                queue.notify();  // Notify a waiting worker thread
            }
        }
    }

    // Method to stop the thread pool
    public void shutdown() {
        isStopped = true;  // Set the flag to true
        for (PoolWorker workerThread : threads) {
            workerThread.interrupt();  // Interrupt each worker thread
        }
    }

    // Inner class for worker threads
    private class PoolWorker extends Thread {
        public void run() {
            Runnable task;  // To hold the current task

            // Main worker loop
            while (!isStopped) {  // Keep running until the pool is stopped
                synchronized (queue) {  // Lock the task queue

                    // Wait if the queue is empty
                    while (queue.isEmpty() && !isStopped) {
                        try {
                            queue.wait();  // Wait for a task
                        } catch (InterruptedException e) {
                            if (isStopped) {
                                return;  // Exit if the pool is stopped
                            }
                        }
                    }
                    // Fetch a task from the queue
                    task = queue.poll();
                }

                // Execute the task
                if (task != null) {
                    try {
                        task.run();
                    } catch (RuntimeException e) {
                        System.out.println("Thread pool is interrupted due to an issue: " + e.getMessage());
                    }
                }
            }
        }
    }
}

