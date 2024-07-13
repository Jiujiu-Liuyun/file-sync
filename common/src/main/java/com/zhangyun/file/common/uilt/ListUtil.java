package com.zhangyun.file.common.uilt;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListUtil {

    public static <K,V> Map<K, V> diffMap(Map<K, V> map1, Map<K, V> map2) {
        if (MapUtils.isEmpty(map1) && MapUtils.isEmpty(map2)) {
            return new HashMap<>();
        }
        if (MapUtils.isEmpty(map1)) {
            return new HashMap<>();
        }
        if (MapUtils.isEmpty(map2)) {
            return new HashMap<>(map1);
        }
        Set<K> k1 = map1.keySet();
        Set<K> k2 = map2.keySet();
        HashSet<K> res = new HashSet<>(k1);
        res.removeAll(k2);
        return map1.entrySet().stream()
                .filter(entry -> res.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <K,V> Map<K, V> unionMap(Map<K, V> map1, Map<K, V> map2) {
        map1 = map1 == null ? new HashMap<>() : map1;
        map2 = map2 == null ? new HashMap<>() : map2;
        return Stream.concat(map1.entrySet().stream(), map2.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <K,V> Map<K, V> intersectionMap(Map<K, V> map1, Map<K, V> map2) {
        if (MapUtils.isEmpty(map1) || MapUtils.isEmpty(map2)) {
            return new HashMap<>();
        }
        Set<K> k1 = map1.keySet();
        Set<K> k2 = map2.keySet();
        HashSet<K> res = new HashSet<>(k1);
        res.retainAll(k2);
        return map1.entrySet().stream()
                .filter(entry -> res.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
