class SleepingRunnable implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " is going to sleep...");
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + " woke up.");
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }
    }
}

public class threadSleep {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new SleepingRunnable());
        Thread thread2 = new Thread(new SleepingRunnable());
        thread1.start();
        thread2.start();
    }
}
