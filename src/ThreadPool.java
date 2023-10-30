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

    // if (!isStopped) : Checks if the thread pool is still running. If it's stopped, the method does nothing.
    // This is essential because multiple threads might try to add tasks to the queue simultaneously.
    //queue.add(task);: Adds the Runnable task to the LinkedBlockingQueue.
    // queue.notify();: Wakes up one of the waiting worker threads to indicate that a new task is available for execution.
    //The method is thread-safe due to the synchronized block. It adds a task to the queue and notifies a potentially waiting worker
    // thread that a task is available.

    // Method to stop the thread pool
    public void shutdown() {
        isStopped = true;  // Set the flag to true
        for (PoolWorker workerThread : threads) {
            workerThread.interrupt();  // Interrupt each worker thread
        }
    }
    /*
    This flag is checked by the worker threads in their main loop, and if it is true, they exit the loop and effectively finish their execution.
    for (PoolWorker workerThread : threads) : Iterates through each worker thread in the thread array.
    workerThread.interrupt();: Sends an interrupt signal to each worker thread. This helps to break any blocking operations
    that might be in progress, such as queue.wait().
    It tells worker threads to stop after completing any task they are currently running.
    It also interrupts all worker threads to force them to stop immediately.
     */

    // Inner class for worker threads
    private class PoolWorker extends Thread {
        public void run() {
            Runnable task;  // hold the task fetched from the queue that the worker thread will execute.

            // Main worker loop
            while (!isStopped) {  // Keep running until the pool is stopped
                synchronized (queue) {  // prevent race conditions.
                    // This is essential because multiple threads might try to fetch or add tasks to the queue at the same time.

                    // if the queue is empty and the pool has not been stopped, the thread goes into a waiting state
                    while (queue.isEmpty() && !isStopped) {
                        try {
                            queue.wait();  // thread waits until it's notified that a new task has been added to the queue.
                        } catch (InterruptedException e) {
                            if (isStopped) {
                                return;  // Catches interruptions, which might be caused when shutdown() is called
                                // Exit if the pool is stopped
                            }
                        }
                    }
                    // Fetch a task from the queue
                    task = queue.poll();
                }

                // Execute the task
                // Checks if a task was successfully fetched from the queue.
                if (task != null) {
                    try {
                        task.run();
                    } catch (RuntimeException e) {
                        System.out.println("Thread pool is interrupted due to an issue: " + e.getMessage());
                        // Any exceptions that are thrown during the execution of the task are caught here, and an error message is printed to the console
                    }
                }
                /*
                The run() method is designed to continuously take tasks from the queue and execute them.
                The method carefully synchronizes access to the shared task queue and properly handles interruptions and exceptions.
                 */
            }
        }
    }
}

