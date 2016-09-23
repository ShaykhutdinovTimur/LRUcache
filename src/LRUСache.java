import java.util.*;

public class LRUСache<K, V> {
    LinkedList<K> timeList;
    HashMap<K, V> cache;
    int size;

    public LRUСache(int size) {
        this.size = size;
        timeList = new LinkedList<>();
        cache = new HashMap<>(size);
    }

    private void up(K key) {
        timeList.remove(key);
        timeList.addFirst(key);
    }

    public void put(K key, V value) {
        if (timeList.size() == size) {
            K deleteKey = timeList.peekLast();
            cache.remove(deleteKey);
        }
        cache.put(key, value);
        timeList.addFirst(key);
    }

    public Optional<V> get(K key) {
        if (cache.containsKey(key)) {
            up(key);
            return Optional.of(cache.get(key));
        } else {
            return Optional.empty();
        }
    }
}
