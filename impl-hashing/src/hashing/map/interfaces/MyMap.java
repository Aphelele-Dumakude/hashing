package hashing.map.interfaces;

import java.util.Set;

/**
 * @author Aphelele Dumakude
 * @param <K>
 * @param <V>
 */
public interface MyMap<K, V> {
    /**
     * Removes all entries from this map
     */
    void clear();

    /**
     * @param key
     * @return true if the specified key is in the map
     */
    boolean containsKey(K key);

    /**
     *
     * @param value
     * @return true if this map contains the specified value
     */
    boolean containsValue(V value);

    /**
     *
     * @return a set of entries in the map
     */
    Set<Entry<K, V>> entrySet();

    /**
     *
     * @param key
     * @return the value that matches the specified key
     */
    V get(K key);

    /**
     *
     * @return true if this map does not contain any entries
     */
    boolean isEmpty();

    /**
     *
     * @return a set consisting of the keys in this map
     */
    Set<K> keySet();

    /**
     * Add an entry (K, V) into the map
     * @param key
     * @param value
     * @return the value of the newly added entry
     */
    V put(K key, V value);

    /**
     * Remove an entry for the specified key
     * @param key
     */
    void remove(K key);

    /**
     *
     * @return the number of mappings in this map
     */
    int size();

    /**
     *
     * @return a set consisting of the values in this map
     */
    Set<V> values();

    /**
     * Define an inner class for Entry
     * @param <K>
     * @param <V>
     */

    class Entry<K, V> {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        public K getKey() {
            return key;
        }
        public V getValue() {
            return value;
        }

        public void setValue(V newValue) {
            value = newValue;
        }
        @Override
        public String toString() {
            return "[" + key + ", " + value + "]";
        }
    }
}
