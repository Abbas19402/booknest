package com.aad.booknest.javalab;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class GenericsDemo {
    static double sum(List<? extends Number> list) {
        double total = 0;
        for(Number n : list) total += n.doubleValue();
        // list.add(1); // compile error: the list might really be List<Double>
        return total;
    }

    // Consumer: we WRITE integers into the list => super
    static void fillWithInts(List<? super Integer> list) {
        for(int i=1; i<=3; i++) list.add(i);
    }

    // PECS in one method (this is how Collections.copy is designed)
    static <T> void copy(List<? super T> dest, List<? extends T> src) {
        for (T item : src) dest.add(item);
    }

    // Bounded type parameter: T must be comparable to itself
    static <T extends Comparable<T>> T max(List<T> list) {
        T best = list.get(0);
        for(T item : list) {
            if(item.compareTo(best) > 0) {
                best = item;
            }
        }
        return best;
    }

    public void run() {
        System.out.println(sum(List.of(1,2,3)));
        System.out.println(sum(List.of(1.5,2.5,3.025)));

        List<Number> numbers = new ArrayList<>();
        fillWithInts(numbers); // works: Number is a supertype of Integer
        System.out.println(numbers);

        List<Integer> source = List.of(1,2,3);
        List<Integer> destination = new ArrayList<>();
        copy(destination, source);
        System.out.println(destination);

        System.out.println(max(List.of(3,4,1,9)));
        System.out.println(max(List.of("pear","apple")));
    }
}

class ConcurrentMapDemo {
    static void hammer(Map<String, Integer> map) throws InterruptedException {
        Runnable task = () -> {
            for(int i = 0; i < 100_000; i++) {
                map.merge("hits",1,Integer::sum);
            }
        };
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start(); t2.start();
        t1.join(); t2.join();
    }

    public void run() throws Exception {
        Map<String, Integer> plain = new HashMap<>();
        Map<String, Integer> concurrent = new ConcurrentHashMap<>();

        hammer(plain);
        hammer(concurrent);

        System.out.println("Hash Map:       "+plain.get("hits")); // usually < 200000, may even misbehave
        System.out.println("Concurrent Hash Map:        "+concurrent.get("hits")); // always 200000

        // null handling
        // new HashMap<>().put(null, null); // works
        // new ConcurrentHashMap<>().put("k", null); // throws NullPointerException
    }
}

public class AdditionalJava {
    public static void main(String args[]) throws Exception {
        // Q30. Q30. Generics: wildcards, PECS, bounded types, generic methods, type erasure
        GenericsDemo genericsDemo = new GenericsDemo();
        genericsDemo.run();

        // Q31. HashMap vs Hashtable vs ConcurrentHashMap: how does ConcurrentHashMap avoid a full lock?
        ConcurrentMapDemo concurrentMapDemo = new ConcurrentMapDemo();
        concurrentMapDemo.run();
    }
}
