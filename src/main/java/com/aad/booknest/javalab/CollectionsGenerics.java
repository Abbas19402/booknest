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
        for(int i = 0; i<2_000; i++) { m.put(keyMaker.apply(i),i); } // storing the value at key
        for(int i = 0; i<2_000; i++) { m.get(keyMaker.apply(i)); } // retrieving the value by providing the key
        return (System.nanoTime()-start) / 1_000_000;
    }

    public void run() {
        System.out.println("good: "+time(GoodKey::new)+" ms");
        System.out.println("bad: "+time(BadKey::new)+" ms");
    }
}

class CollectionsDemo {
    public void run() {
        Set<String> hash = new HashSet<String>(List.of("apple", "pear", "fig"));
        Set<String> tree = new TreeSet<String>(List.of("apple", "pear", "fig"));
        System.out.println(hash);
        System.out.println(tree);

        Map<String, Integer> map = new HashMap<>();
        map.put(null, 1);
        System.out.println(map);
    }
}

class Book implements Comparable<Book> {
    String title;
    double price;

    Book(String title, double price) {
        this.title = title;
        this.price = price;
    }

    @Override 
    public int compareTo(Book o) {
        return this.title.compareTo(o.title);
    }

    @Override 
    public String toString() {
        return title + " ( " + price + " ) ";
    }
}

class SortDemo {
    public void run() {
        List<Book> books = new ArrayList<Book>(
            List.of(
                new Book("Zen", 300),
                new Book("Alchemist", 450)
            )
        );

        // this uses compareTo which we have override in book class
        Collections.sort(books);
        System.out.println(books);

        // this is the external comparator
        books.sort(Comparator
            .comparingDouble((Book b) -> b.price) // ascending order
            .thenComparing(b -> b.title)
        );
        System.out.println(books);

        books.sort(Comparator
            .comparingDouble((Book b) -> b.price)
            .reversed() // descending order
        );
        System.out.println(books);
    }
}

class FailFastDemo {
    public void run() {
        List<Integer> nums = new ArrayList<Integer>(List.of(1,2,3,4,5));

        // Wrong: throws ConcurrentModificationException
        // for (Integer n : nums) if ( n % 2 == 0 ) { nums.remove(n); }

        // Right: removeIf
        nums.removeIf(n -> n % 2 == 0);
        System.out.println(nums);

        // Right: explicit iterator
        Iterator<Integer> it = nums.iterator();
        while(it.hasNext()) if(it.next() == 3) it.remove();
        System.out.println(nums);
    }
}

public class CollectionsGenerics {
    public static void main(String args[]) {
        // Q7. How does HashMap work internally? (The most asked question.)
        HashMapDemo hashMapDemo = new HashMapDemo();
        hashMapDemo.run();

        // Q8. ArrayList vs LinkedList ; HashMap vs ConcurrentHashMap vs Hashtable ; HashSet vs TreeSet
        CollectionsDemo collectionsDemo = new CollectionsDemo();
        collectionsDemo.run();

        // Q9. Comparable vs Comparator
        SortDemo sortDemo = new SortDemo();
        sortDemo.run();

        // Q10. Fail-fast vs fail-safe iterators; ConcurrentModificationException
        FailFastDemo failFastDemo = new FailFastDemo();
        failFastDemo.run();
    }
}
