import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class SumTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 1000;
    private final int[] array;
    private final int start, end;

    public SumTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            int mid = (start + end) / 2;
            SumTask leftTask = new SumTask(array, start, mid);
            SumTask rightTask = new SumTask(array, mid, end);
            leftTask.fork();
            return rightTask.compute() + leftTask.join();
        }
    }
}

public class forkJoinExample {
    public static void main(String[] args) {
        int[] array = new int[1000000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }

        ForkJoinPool pool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();
        long forkJoinResult = pool.invoke(new SumTask(array, 0, array.length));
        long endTime = System.currentTimeMillis();
        System.out.println("Fork-Join sum: " + forkJoinResult + " in " + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        long singleThreadResult = 0;
        for (int num : array) {
            singleThreadResult += num;
        }
        endTime = System.currentTimeMillis();
        System.out.println("Single-threaded  sum: " + singleThreadResult + " in " + (endTime - startTime) + " ms");
    }
}
