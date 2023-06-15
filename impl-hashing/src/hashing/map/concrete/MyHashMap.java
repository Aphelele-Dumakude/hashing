package hashing.map.concrete;

import hashing.map.interfaces.MyMap;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author Aphelele Dumakude
 * @param <K>
 * @param <V>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    /**
     * Default hash-table size, must be a power of 2.
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 4;
    /**
     * Maximum hash-table size. 1 << 30 is same as 2^30
     */
    private static final int  MAXIMUM_CAPACITY = 1 << 30;
    /**
     * Default load factor
     */
    private static final float DEFAULT_MAX_LOAD_FACTOR = 0.75f;
    /**
     * Current hash-table capacity. Capacity is a power of 2.
     */
    private int capacity;
    /**
     * Specify a load factor used in the hash table
     */
    private final float loadFactorThreshold;
    /**
     * The number of entries in the map
     */
    private int size;
    /**
     * Hash table is an array with each cell being a linked list
     */
    private LinkedList<MyMap.Entry<K, V>>[] hashTable;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_MAX_LOAD_FACTOR);
    }
    public MyHashMap(int initialCapacity, float loadFactorThreshold) {
        if (initialCapacity > MAXIMUM_CAPACITY) {
            this.capacity = MAXIMUM_CAPACITY;
        }
        else {
            this.capacity = trimToPowerOf2(initialCapacity);
        }
        this.loadFactorThreshold = loadFactorThreshold;
        hashTable = new LinkedList[capacity];
    }

    /**
     * Removes all entries from this map
     */
    @Override
    public void clear() {
        size = 0;
        removeEntries();
    }

    /**
     * @param key
     * @return true if the specified key is in the map
     */
    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * @param value
     * @return true if this map contains the specified value
     */
    @Override
    public boolean containsValue(V value) {
        for (LinkedList<Entry<K, V>> entries : hashTable) {
            if (entries != null) {
                for (Entry<K, V> entry : entries) {
                    if (entry.getValue().equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @return a set of entries in the map
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new HashSet<>();
        for (LinkedList<Entry<K, V>> entries : hashTable) {
            if (entries != null) {
                set.addAll(entries);
            }
        }
        return set;
    }

    /**
     * @param key
     * @return the value that matches the specified key
     */
    @Override
    public V get(K key) {
        int bucketIndex = hash(key.hashCode());
        if (hashTable[bucketIndex] != null) {
            LinkedList<Entry<K, V>> bucket = hashTable[bucketIndex];
            for (Entry<K, V> entry: bucket) {
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    /**
     * @return true if this map does not contain any entries
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return a set consisting of the keys in this map
     */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (LinkedList<Entry<K, V>> entries : hashTable) {
            if (entries != null) {
                for (Entry<K, V> entry: entries) {
                    set.add(entry.getKey());
                }
            }
        }
        return set;
    }

    /**
     * Add an entry (K, V) into the map
     *
     * @param key
     * @param value
     * @return the value of the newly added entry
     */
    @Override
    public V put(K key, V value) {
        if (get(key) != null) {
            int bucketIndex = hash(key.hashCode());
            LinkedList<Entry<K, V>> bucket = hashTable[bucketIndex];
            for (Entry<K, V> entry: bucket) {
                if (entry.getKey().equals(key)) {
                    V oldValue = entry.getValue();
                    entry.setValue(value);
                    return oldValue;
                }
            }
        }

        if (size >= capacity * loadFactorThreshold) {
            if (capacity == MAXIMUM_CAPACITY) {
                throw new RuntimeException("Exceeding maximum capacity");
            }
            rehash();
        }
        int bucketIndex = hash(key.hashCode());

        if (hashTable[bucketIndex] == null) {
            hashTable[bucketIndex] = new LinkedList<>();
        }
        hashTable[bucketIndex].add(new MyMap.Entry<>(key, value));
        size++;

        return value;
    }

    private void rehash() {
        Set<Entry<K, V>> set = entrySet();
        capacity <<=1;
        hashTable = new LinkedList[capacity];
        size = 0;

        for (Entry<K, V> entry: set) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Remove an entry for the specified key
     *
     * @param key
     */
    @Override
    public void remove(K key) {
        int bucketIndex = hash(keySet().hashCode());
        if (hashTable[bucketIndex] != null) {
            LinkedList<Entry<K, V>> bucket = hashTable[bucketIndex];
            for (Entry<K, V> entry: bucket) {
                if (entry.getKey().equals(key)) {
                    bucket.remove(entry);
                    size--;
                    break;
                }
            }
        }
    }

    /**
     * @return the number of mappings in this map
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * @return a set consisting of the values in this map
     */
    @Override
    public Set<V> values() {
        Set<V> set = new HashSet<>();
        for (LinkedList<Entry<K, V>> entries : hashTable) {
            if (entries != null) {
                for (Entry<K, V> entry: entries) {
                    set.add(entry.getValue());
                }
            }
        }
        return set;
    }

    /**
     *
     * @param hashCode
     * @return Hash function
     */
    private int hash(int hashCode) {
        return supplementalHash(hashCode) & (capacity - 1);
    }

    /**
     *
     * @param h
     * @return Ensure the hashing is evenly distributed
     */
    private static int supplementalHash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     *
     * @param initialCapacity
     * @return a power of 2 for initialCapacity
     */
    private int trimToPowerOf2(int initialCapacity) {
        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }
        return capacity;
    }

    /**
     * Remove all entries from each bucket
     */
    private void removeEntries() {
        for (LinkedList<Entry<K, V>> entries: hashTable) {
            if (entries != null) {
                entries.clear();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");

        for (int i=0; i < capacity; i++) {
            if (hashTable[i] != null && hashTable[i].size() > 0) {
                for (Entry<K, V> entry: hashTable[i]) {
                    builder.append(entry);
                }
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
