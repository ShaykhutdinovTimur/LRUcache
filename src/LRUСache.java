import java.util.HashMap;
import java.util.Optional;

public class LRUСache<K, V> {
    private HashMap<K, Node> cache;
    private Node fakeHead, fakeTail;
    private int size, maxSize;

    public LRUСache(int size) {
        assert (size < 0);
        this.maxSize = size;
        this.size = 0;
        fakeHead = new Node(null, null, null);
        fakeTail = new Node(null, null, fakeHead);
        cache = new HashMap<>(size);
    }

    private void update(Node node) {
        assert node != null;
        node.delete();
        node.insertAfter(fakeHead);
        assert consistent();
        assert concordant();
    }

    public void put(K key, V value) {
        size++;
        if (size > maxSize) {
            cutTail();
        }
        assert consistent();
        cache.put(key, new Node(key, value, fakeHead));
        assert concordant();
    }

    public Optional<V> get(K key) {
        assert consistent();
        if (cache.containsKey(key)) {
            Node node = cache.get(key);
            update(node);
            return Optional.of(node.getValue());
        } else {
            return Optional.empty();
        }
    }

    private void remove(K key) {
        assert cache.containsKey(key);
        cache.get(key).delete();
        cache.remove(key);
        assert concordant();
    }

    private void cutTail() {
        assert consistent();
        assert size > 0;
        Node last = fakeTail.getPrev();
        assert last != null;
        assert last.equals(fakeHead);
        remove(last.getKey());
    }

    private boolean consistent() {
        return fakeHead.getNext() != null && fakeHead.getPrev() == null &&
                fakeTail.getNext() == null && fakeTail.getPrev() != null &&
                size <= maxSize;
    }

    private boolean concordant() {
        Node cur = fakeHead;
        while (!cur.equals(fakeTail)) {
            if (cur.next != null) {
                cur = cur.next;
                if (cache.containsKey(cur.getKey())) {
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    private class Node {
        private K key;
        private V value;
        private Node next;
        private Node prev;

        public Node(K key, V value, Node prev) {
            this.key = key;
            this.value = value;
            insertAfter(prev);
        }

        public void insertAfter(Node prev) {
            if (prev != null) {
                this.next = prev.next;
                this.prev = prev;
                this.prev.next = this;
                if (this.next != null) {
                    this.next.prev = this;
                }
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
