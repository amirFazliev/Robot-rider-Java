import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    static int COUNT_TREAD = 1000;

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        final ExecutorService threadPool = Executors.newFixedThreadPool(COUNT_TREAD);

        for (int i = 0; i<COUNT_TREAD; i++) {
            Callable<Map<Integer, Integer>> mapCallable = new MyCallable(sizeToFreq);
            threadPool.submit(mapCallable);
        }
        threadPool.shutdown();

        int maxSizeValue = 0;
        int maxSizeKey = 0;
        Set<Integer> keySetMap = sizeToFreq.keySet();
        for (Integer integer : keySetMap) {
            if (sizeToFreq.get(integer) > maxSizeValue) {
                maxSizeValue = sizeToFreq.get(integer);
                maxSizeKey = integer;
            }
        }
        sizeToFreq.remove(maxSizeKey, maxSizeValue);

        System.out.println("Самое частое количество повторений " + maxSizeKey + " (встретилось " + maxSizeValue + " раз)");
        System.out.println("Другие размеры:");
        for (Integer integer : sizeToFreq.keySet()) {
            System.out.println("- " + integer + " (" + sizeToFreq.get(integer) + " раз)");
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}