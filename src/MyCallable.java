import java.util.Map;
import java.util.concurrent.Callable;

public class MyCallable implements Callable<Map<Integer, Integer>> {
    private final Map<Integer, Integer> map;

    public MyCallable(Map<Integer, Integer> map) {
        this.map = map;
    }

    @Override
    public Map<Integer, Integer> call() throws Exception {
        String text = Main.generateRoute("RLRFR", 100);
        synchronized (map) {
            int valueTotal = 0;
            for (int i = 0; i < text.length(); i++) {
                if ("R".equals(text.substring(i, i + 1))) {
                    valueTotal++;
                }
            }

            if (map.containsKey(valueTotal)) {
                map.put(valueTotal, map.get(valueTotal) + 1);
            } else {
                map.put(valueTotal, 1);
            }

            return map;
        }
    }
}
