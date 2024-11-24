import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class CustomThreadPool {
    private final List<Worker> workers;
    private final Queue<Runnable> taskQueue;
    private boolean isShutdown = false;

    public CustomThreadPool(int initialPoolSize) {
        workers = new LinkedList<>();
        taskQueue = new LinkedList<>();

        for (int i = 0; i < initialPoolSize; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            worker.start();
        }
    }

    public synchronized void submit(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("Thread pool is shutdown.");
        }
        taskQueue.add(task);
        notify();
    }

    public synchronized void resize(int newSize) {
        int currentSize = workers.size();

        if (newSize > currentSize) {
            for (int i = 0; i < newSize - currentSize; i++) {
                Worker worker = new Worker();
                workers.add(worker);
                worker.start();
            }
        } else {
            for (int i = 0; i < currentSize - newSize; i++) {
                Worker worker = workers.remove(0);
                worker.interrupt();
            }
        }
    }

    public synchronized void shutdown() {
        isShutdown = true;
        for (Worker worker : workers) {
            worker.interrupt();
        }
    }

    private class Worker extends Thread {
        public void run() {
            while (true) {
                Runnable task;
                synchronized (CustomThreadPool.this) {
                    while (taskQueue.isEmpty() && !isShutdown) {
                        try {
                            CustomThreadPool.this.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    if (isShutdown && taskQueue.isEmpty()) return;
                    task = taskQueue.poll();
                }
                try {
                    task.run();
                } catch (RuntimeException e) {
                    System.out.println("Task execution failed: " + e.getMessage());
                }
            }
        }
    }
}

public class CustomThreadPoolExample {
    public static void main(String[] args) {
        CustomThreadPool pool = new CustomThreadPool(3);

        for (int i = 0; i < 10; i++) {
            int taskNumber = i;
            pool.submit(() -> System.out.println("Executing task " + taskNumber));
        }

        pool.resize(5);
        pool.shutdown();
    }
}
