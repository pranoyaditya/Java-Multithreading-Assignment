class SimpleRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Hello from SimpleRunnable thread!!");
    }
}
public class basicThreadCreation {
    public static void main(String[] args) {
        SimpleRunnable runnable = new SimpleRunnable();
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
