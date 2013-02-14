package com.wixpress.testapp.dao;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * Created by : doron
 * Since: 7/1/12
 */

public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private int capacity; // Maximum number of items in the cache.

    public LRUCache(int capacity) {
        super(capacity + 1, 1.0f, true); // Pass 'true' for accessOrder.
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Entry entry) {
        return (size() > this.capacity);
    }
}