import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class ConcurrentMapTask implements Runnable {
    private final Map<String, Integer> map;

    public ConcurrentMapTask(Map<String, Integer> map) {
        this.map = map;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            String key = "Key" + i;
            map.merge(key, 1, Integer::sum);
            System.out.println(Thread.currentThread().getName() + " updated " + key + " to " + map.get(key));
        }
    }
}

public class concurrentDataStructuresExample {
    public static void main(String[] args) {
        Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();

        Thread t1 = new Thread(new ConcurrentMapTask(concurrentMap), "Thread1");
        Thread t2 = new Thread(new ConcurrentMapTask(concurrentMap), "Thread2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(" Final map: " + concurrentMap);
    }
}
