import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

class Resource {
    private final String name;

    public Resource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class DeadlockTask implements Runnable {
    private final Resource resource1;
    private final Resource resource2;

    public DeadlockTask(Resource resource1, Resource resource2) {
        this.resource1 = resource1;
        this.resource2 = resource2;
    }

    @Override
    public void run() {
        synchronized (resource1) {
            System.out.println(Thread.currentThread().getName() + " locked " + resource1.getName());
            try {
                Thread.sleep(50);
                synchronized (resource2) {
                    System.out.println(Thread.currentThread().getName() + " locked " + resource2.getName());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class deadlockDetectionExample {
    public static void main(String[] args) {
        Resource resource1 = new Resource("Resource1");
        Resource resource2 = new Resource("Resource2");

        Thread t1 = new Thread(new DeadlockTask(resource1, resource2), "Thread1");
        Thread t2 = new Thread(new DeadlockTask(resource2, resource1), "Thread2");

        t1.start();
        t2.start();

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        new Thread(() -> {
            while (true) {
                long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
                if (deadlockedThreads != null) {
                    System.out.println("Deadlock detected! Resolving.....");
                    for (long id : deadlockedThreads) {
                        System.out.println("Deadlocked thread ID: " + id);
                    }
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
