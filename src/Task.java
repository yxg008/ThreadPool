// Task class implementing the Runnable interface
public class Task implements Runnable {
    private int num;  // Task number

    public Task(int n) {
        num = n;  // Initialize the task number
    }

    // The run method contains the code that constitutes the task
    public void run() {
        System.out.println("Task " + num + " is running.");
    }
}
