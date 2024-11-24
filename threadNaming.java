class NamedRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Thread name: " + Thread.currentThread().getName());
    }
}

public class threadNaming {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new NamedRunnable(), "FirstThread ");
        Thread thread2 = new Thread(new NamedRunnable(), "SecondThread ");
        thread1.start();
        thread2.start();
    }
}
