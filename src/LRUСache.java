import java.util.*;

public class LRUСache<K, V> {
    HashMap<K, Node> cache;
    private Node fakeHead, fakeTail;
    private int size, maxSize;

    public LRUСache(int size) {
        this.maxSize = size;
        this.size = 0;
        fakeTail = new Node(null, null, null, null);
        fakeHead = new Node(null, null, null, fakeTail);
        cache = new HashMap<>(size);
    }

    private void up(K key) {
        Node node = cache.get(key);
        node.delete();
        node.setNeighbors(fakeHead, fakeHead.getNext());
    }

    public void put(K key, V value) {
        size++;
        if (size > maxSize) {
            cutTail();
        }
        cache.put(key, new Node(key, value, fakeHead, fakeHead.getNext()));
    }

    public Optional<V> get(K key) {
        if (cache.containsKey(key)) {
            up(key);
            return Optional.of(cache.get(key).getValue());
        } else {
            return Optional.empty();
        }
    }

    private void remove(K key) {
        cache.get(key).delete();
        cache.remove(key);
    }

    private void cutTail() {
        remove(fakeTail.getPrev().getKey());
    }

    private class Node {
        private K key;
        private V value;
        private Node next, prev;

        public Node(K key, V value, Node prev, Node next) {
            this.key = key;
            this.value = value;
            setNeighbors(prev, next);
        }

        public void setNeighbors(Node prev, Node next) {
            this.next = next;
            this.prev = prev;
            if (this.prev != null) {
                this.prev.next = this;
            }
            if (this.next != null) {
                this.next.prev = this;
            }
        }

        public V getValue() {
            return value;
        }

        public Node getNext() {
            return next;
        }

        public Node getPrev() {
            return prev;
        }

        public K getKey() {
            return key;
        }

        public void delete() {
            if (this.prev != null) {
                this.prev.next = this.next;
            }
            if (this.next != null) {
                this.next.prev = this.next;
            }
        }
    }

}
