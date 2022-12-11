import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    static int COUNT_TREAD = 1000;

    public static void main(String[] args) throws ExecutionException, InterruptedException, Exception {

        Thread thread = new Thread(() -> {
            try {
                synchronized (sizeToFreq){

                    while (!Thread.interrupted()) {
                        sizeToFreq.wait();
                        int maxSizeValue = 0;
                        int maxSizeKey = 0;
                        Set<Integer> keySetMap = sizeToFreq.keySet();
                        for (Integer integer : keySetMap) {
                            if (sizeToFreq.get(integer) > maxSizeValue) {
                                maxSizeValue = sizeToFreq.get(integer);
                                maxSizeKey = integer;
                            }
                        }
                        System.out.println("Самое частое промежуточное количество повторений " + maxSizeKey + " (встретилось " + maxSizeValue + " раз)");
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();


        for (int i = 0; i < COUNT_TREAD; i++) {
            Thread threadPool = new Thread(() -> {
                synchronized (Main.sizeToFreq) {
                    String text = Main.generateRoute("RLRFR", 100);
                    int valueTotal = 0;
                    for (int k = 0; k < text.length(); k++) {
                        if ("R".equals(text.substring(k, k + 1))) {
                            valueTotal++;
                        }
                    }

                    if (Main.sizeToFreq.containsKey(valueTotal)) {
                        Main.sizeToFreq.put(valueTotal, Main.sizeToFreq.get(valueTotal) + 1);
                    } else {
                        Main.sizeToFreq.put(valueTotal, 1);
                    }
                    Main.sizeToFreq.notify();
                }
            });
            threadPool.start();
            threadPool.join();
            threadPool.interrupt();
        }
        thread.interrupt();

        int maxSizeValue = 0;
        int maxSizeKey = 0;
        Set<Integer> keySetMap = sizeToFreq.keySet();
        for (Integer integer : keySetMap) {
            if (sizeToFreq.get(integer) > maxSizeValue) {
                maxSizeValue = sizeToFreq.get(integer);
                maxSizeKey = integer;
            }
        }

        System.out.println("Самое частое количество повторений " + maxSizeKey + " (встретилось " + maxSizeValue + " раз)");
        System.out.println("Другие размеры:");
        for (Integer integer : sizeToFreq.keySet()) {
            if(maxSizeKey != integer) {
                System.out.println("- " + integer + " (" + sizeToFreq.get(integer) + " раз)");
            }
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