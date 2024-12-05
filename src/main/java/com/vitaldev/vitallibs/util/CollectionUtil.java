package com.vitaldev.vitallibs.util;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionUtil {
    public static <K, V extends Comparable<V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.<K, V>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    public static <K, V extends Comparable<V>> K getKeyWithHighestValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static <K, V extends Comparable<V>> K getKeyWithLowestValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static <K, V extends Comparable<V>> Map<K, V> filterByValueRange(Map<K, V> map, V min, V max) {
        return map.entrySet()
                .stream()
                .filter(entry -> entry.getValue().compareTo(min) >= 0 && entry.getValue().compareTo(max) <= 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <K, V> Map<V, K> invert(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    public static <K> Map<K, Integer> mergeMaps(Map<K, Integer> map1, Map<K, Integer> map2) {
        Map<K, Integer> result = new HashMap<>(map1);
        map2.forEach((key, value) -> result.merge(key, value, Integer::sum));
        return result;
    }

    public static <K, V> List<K> findKeysByValue(Map<K, V> map, V value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static <K, V> boolean hasDuplicateValues(Map<K, V> map) {
        return map.values().size() != new HashSet<>(map.values()).size();
    }

    public static <K extends Comparable<K>, V> Map<K, V> sortByKey(Map<K, V> map, boolean ascending) {
        return map.entrySet()
                .stream()
                .sorted(ascending ? Map.Entry.comparingByKey() : Map.Entry.<K, V>comparingByKey().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    public static <T> Set<T> findDuplicates(Collection<T> collection) {
        Set<T> seen = new HashSet<>();
        return collection.stream()
                .filter(item -> !seen.add(item))
                .collect(Collectors.toSet());
    }

    public static <T> List<T> shuffleList(List<T> list) {
        List<T> shuffled = new ArrayList<>(list);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    public static <T> T getMostFrequentElement(Collection<T> collection) {
        return collection.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static <T> String join(Collection<T> collection, String delimiter) {
        return collection.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(delimiter));
    }
}
