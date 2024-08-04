package com.harsh.redis.app.store;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DataStore {
    private final ConcurrentMap<String, String> concurrentMap;

    public DataStore() {
        concurrentMap = new ConcurrentHashMap<>();
    }

    public String get(String key) {
        return concurrentMap.get(key);
    }

    public void put(String key, String value) {
        concurrentMap.put(key, value);
    }

}
