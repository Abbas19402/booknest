package com.aad.booknest.javalab;

import java.util.*;

class BadKey {
    final int id;
    BadKey(int id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        return o instanceof BadKey k && k.id == id;
    }

    @Override
    public int hashCode() { return 1; }
}

class GoodKey {
    final int id;
    GoodKey(int id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        return o instanceof GoodKey k && k.id == id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }
}

class HashMapDemo {
    static long time(java.util.function.IntFunction<Object> keyMaker) {
        Map<Object, Integer> m = new HashMap<>();
        long start = System.nanoTime();
        for(int i = 0; i<20_000; i++) { m.put(keyMaker.apply(i),i); }
        for(int i = 0; i<200_000; i++) { m.get(keyMaker.apply(i)); }
        return (System.nanoTime()-start) / 1_000_000;
    }

    public void run() {
        System.out.println("good: "+time(GoodKey::new)+" ms");
        System.out.println("bad: "+time(BadKey::new)+" ms");
    }
}
public class CollectionsGenerics {
    public static void main(String args[]) {
        // Q7. How does HashMap work internally? (The most asked question.)
        HashMapDemo hashMapDemo = new HashMapDemo();
        hashMapDemo.run();
    }
}
