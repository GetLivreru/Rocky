package com.cache;

public class Main {
    public static void main(String[] args) {
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("a", "1");
        cache.put("b", "2");
        System.out.println(cache.get("a"));
        cache.close();
    }
}