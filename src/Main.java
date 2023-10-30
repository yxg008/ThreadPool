public class Main {
    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(10);  // Initialize a thread pool with 10 threads

        // Add 100 tasks to the thread pool
        for (int i = 0; i < 100; i++) {
            Task task = new Task(i);
            threadPool.add(task);
        }

        Thread.sleep(5000);  // Wait for 5 seconds before shutting down the thread pool
        threadPool.shutdown();  // Shut down the thread pool
    }
    /*
    This Main class serves as a simple demonstration of how to use the ThreadPool class to execute a batch of tasks asynchronously.
    It initializes a thread pool, adds 100 tasks to it, waits for 5 seconds to let some tasks get processed,
    and then shuts down the thread pool.
     */
}
