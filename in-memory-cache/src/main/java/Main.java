import com.cache.LRUCache;

public class Main {
    public static void main(String[] args) {
        try (LRUCache<String, String> cache = new LRUCache<>(3)) {
            cache.put("a", "1");
            cache.put("b", "2");
            cache.put("c", "3");

            System.out.println("get(a) = " + cache.get("a"));

            cache.put("d", "4");

            System.out.println("get(b) after LRU eviction = " + cache.get("b"));
            System.out.println("cache size = " + cache.size());
            System.out.println("hits = " + cache.getHits());
            System.out.println("misses = " + cache.getMisses());
            System.out.println("hitRate = " + cache.hitRate());
        }
    }
}
