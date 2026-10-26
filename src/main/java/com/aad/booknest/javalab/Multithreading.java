package com.aad.booknest.javalab;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

class RaceDemo {
    static int safe = 0;
    static int unsafe = 0;
    static final AtomicInteger atomic = new AtomicInteger();
    static final Object lock = new Object();

    public void run() throws InterruptedException {
        Runnable task = () -> {
            for(int i = 0; i < 100_00; i++) {
                unsafe++;
                synchronized(lock) { safe++; }
                atomic.incrementAndGet();
            }
        };
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("unsafe: "+unsafe);
        System.out.println("synced: "+safe);
        System.out.println("atomic: "+atomic);
    }  
}

class ExecutorDemo {
    public void run() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(3);
       
        Callable<Integer> square = () -> { Thread.sleep(200); return 7*7; };
        Future<Integer> future = pool.submit(square);
        System.out.println("doing other work!");
        System.out.println("result: "+future.get()); // waits for the answer

        List<Callable<String>> jobs = List.of(
            () -> "A on " + Thread.currentThread().getName(),
            () -> "B on " + Thread.currentThread().getName()
        );

        for(Future<String> f : pool.invokeAll(jobs)) {
            System.out.println(f.get());
            pool.shutdown(); // // forget this and the program never exits
        }

        // Java 21 Virtual Threads
        try( var exec = Executors.newVirtualThreadPerTaskExecutor() ) {
            exec.submit(() -> System.out.println("hello from virtual thread"));
        }
    }

}

public class Multithreading {
    public static void main(String args[]) throws Exception {
        // Q14. What is a race condition? synchronized, volatile, atomics.
        RaceDemo raceDemo = new RaceDemo();
        raceDemo.run();

        // Q15. Thread vs Runnable vs Callable; ExecutorService; virtual threads
        ExecutorDemo executorDemo = new ExecutorDemo();
        executorDemo.run();
    }
}
