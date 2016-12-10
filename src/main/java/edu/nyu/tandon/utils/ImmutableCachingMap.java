package edu.nyu.tandon.utils;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

/**
 * @author michal.siedlaczek@nyu.edu
 */
public abstract class ImmutableCachingMap<K, V> {

    protected HashMap<K, Integer> impacts;
    protected HashMap<K, V> cachedValues;

    protected Queue<K> keyQ;
    protected int capacity;

    public ImmutableCachingMap(int capacity) {
        impacts = new HashMap<K, Integer>();
        cachedValues = new HashMap<K, V>();
        keyQ = new ArrayDeque<K>(capacity);
        this.capacity = capacity;

    }

    protected abstract V read(K key);

    protected void que(K key) {
        keyQ.add(key);
        capacity--;
        if (capacity < 0) {
            overflow();
        }
    }

    protected void overflow() {
        K k = keyQ.poll();
        Integer impact = impacts.getOrDefault(k, 0) - 1;
        if (impact < 0) {
            impacts.remove(k);
            cachedValues.remove(k);
        }
        else {
            impacts.put(k, impact);
            keyQ.add(k);
        }
        capacity++;
    }

    public V get(K key) {
        V v = cachedValues.get(key);
        if (v != null) {
            impacts.put(key, impacts.getOrDefault(key, 0) + 1);
        }
        else {
            v = read(key);
            if (v == null) return null;
            cachedValues.put(key, v);
        }
        que(key);
        return v;
    }

}
