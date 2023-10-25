
// Main class to test the thread pool
public class Main {
    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool(7);  // Create a thread pool with 7 threads

        // Add 5 tasks to the thread pool
        for (int i = 0; i < 5; i++) {
            Task task = new Task(i);
            pool.add(task);
        }

        pool.shutdown();  // Shut down the thread pool
    }
}
