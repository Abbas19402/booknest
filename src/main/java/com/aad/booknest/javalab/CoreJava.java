package com.aad.booknest.javalab;

import java.util.*;

class Student {
    int id;
    String name;

    Student( int id, String name ) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Student s)) return false;
        
        return id == s.id && Objects.equals(name, s.name);
    }

    @Override
    public int hashCode() { return Objects.hash(id,name); }
}

class EqualsDemo {
    public void run() {
        System.out.println("Equals Demo:");
        Student a = new Student(1, "Aisha");
        Student b = new Student(1, "Aisha");
        System.out.println(a == b); //false
        System.out.println(a.equals(b)); // without overriding the equals, it will return false else after overriding it returns true

        Set<Student> set = new HashSet<>();
        set.add(a); set.add(b);
        System.out.println(set.size()); // 1
    }
}

class StringDemo {
    public void run() {
        System.out.println("\nString Demo:");
        String s1 = "hello";
        String s2 = "hello";
        String s3 = new String("hello");

        System.out.println(s1 == s2); // false, Incorrect: its true because these are simple values and not objects. if it would be an object then false because it would be from a different instance
        System.out.println(s1 == s3); // false, Correct!
        System.out.println(s1.equals(s3)); // false, Incorrect: so comparing a variable from a Object's value returns true, this means that in this case if it is compared with a variable instead of a different Object, then it resides in same drawer?
        System.out.println(s1 == s3.intern()); // maybe true, idk what an intern is. Correct! need to findout what is an intern here

        String t = "abc";
        t.toUpperCase();
        System.out.println(t); // abc, Correct!

        StringBuilder sb = new StringBuilder();
        for(int i=0; i < 5; i++) {
            sb.append(i).append(", "); 
        }
        System.out.println(sb); // 0, 1, 2, 3, 4, Correct! 
    }
}

class Box {
    public int value;
}

class PassDemo {
    static void change(int x) { x = 99; }
    static void mutate(Box b) { b.value = 99; }
    static void reassign(Box b) { b = new Box(); b.value = 99; }

    public void run() {
        System.out.println("\nPass-by-value Demo:");
        int n = 1;
        change(n);
        System.out.println(n); // 1

        Box box = new Box();
        mutate(box);
        System.out.println(box.value); // 99

        box = new Box();
        reassign(box);
        System.out.println(box.value); // 99 Incorrect! because it created new object. and default un assigned variable is given 0 as its value
    }
}

interface Drawable {
    void draw();
    default void info() { System.out.println("I can be drawn!"); }
}

abstract class Shape {
    protected final String name;
    Shape(String name) { this.name = name; }
    abstract double area(); // Needs to be implemented on subclasses
    void describe() { System.out.println(name+" Area: "+area()); }
}

class Circle extends Shape implements Drawable {
    private final double r;
    Circle(double r) {
        super("Circle");
        this.r = r;
    }

    @Override
    double area() {
        return Math.PI * r * r;
    }

    @Override
    public void draw() {
        System.out.println("Drawing circle");
    }
}

class OopDemo {
    public void run() {
        System.out.println("\nOOPS Demo:");
        Shape s = new Circle(2);
        s.describe(); // Circe Area: 12.5(approximate)
        ((Drawable) s).draw(); // what is this happening here with parenthesis? It could have been called like this "s.draw() and have same output like here. Why double parenthesis?", Output: Drawing Circle
        // Solution: This is type casting which tells that this is of type Drawable therefore, it executes perfectly. Otherwise, type Shape does not have a draw method. So, "s.draw()" would have threw an error.
    }
}

class Resource implements AutoCloseable {
    final String name;
    Resource(String name) {
        this.name = name;
        System.out.println("open "+name);
    }
    void use() { throw new IllegalStateException("boom"); }
    @Override 
    public void close() {
        System.out.println("close "+name);
    }
}

class InsufficientStockException extends Exception {
    InsufficientStockException( String m ) { super(m); }
}
class BookNotFoundException extends RuntimeException {
    BookNotFoundException(String m) { super(m); }
}

class ExceptionDemo {
    static void borrow(int copies) throws InsufficientStockException {
        if(copies == 0) throw new InsufficientStockException("no copies left"); 
    }

    public void run() {
        try( Resource a = new Resource("A"); Resource b = new Resource("B") ) {
            // here at this point resource A is created so prints "open A" followed by "open B" as both are created one after another. Remember the sequence a -> b
            b.use(); // This will throw the error which will then go to catch block. but as the Resource class is AutoClosable, so our overrided close method will show the sequence of clean up. by first printing "close B" then "close A". 
            // The order of closing ( or cleanup ) is in reverse order of creation. so it first closes B because it was created in last then closes A as it was created before B.
        } catch (IllegalStateException e) {
            // then here it prints "boom" as it is the error which is being thrown
            System.out.println("Caught "+e.getMessage());
        } finally {
            // then prints finally
            System.out.println("finally");
        }

        try { borrow(0); } catch (InsufficientStockException e) {
            // and then prints "no copies left"
            System.out.println(e.getMessage());
        }
    }
}

class MemoryDemo {
    static int depth = 0;
    static void recurse() { depth++; recurse(); }

    public void run() {
        // this recursion call keeps calling itself again and again therefore filling the stack. As each call is performed by one thread and every single call is pushed into the stack
        try { recurse(); }
        catch (StackOverflowError e) { System.out.println("stack overflow at depth "+depth); }

        // TODO: needs further and detailed understanding of this HEAP OutOfMemeory exception call
        List<byte[]> hog = new ArrayList<>();
        try { while(true) hog.add(new byte[1_000_0000]); }
        catch (OutOfMemoryError e) { System.out.println("heap full after "+hog.size()+" MB"); }
    }
}   


public class CoreJava {
    public static void main(String args[]) {
        // Q1. What is the difference between == and .equals()? Why override equals() and hashCode() together?
        EqualsDemo equalsDemo = new EqualsDemo();
        equalsDemo.run();

        // Q2. Why is String immutable? What is the String pool? StringBuilder vs StringBuffer?
        StringDemo stringDemo = new StringDemo();
        stringDemo.run();

        // Q3. Is Java pass-by-value or pass-by-reference?
        PassDemo passDemo = new PassDemo();
        passDemo.run();

        // Q4. Abstract class vs interface. Four pillars of OOP.
        OopDemo oopDemo = new OopDemo();
        oopDemo.run();

        // Q5. Checked vs unchecked exceptions. What is try-with-resources ? final vs finally vs finalize ?
        ExceptionDemo exceptionDemo = new ExceptionDemo();
        exceptionDemo.run();

        //Q6. Stack vs heap. What are StackOverflowError and OutOfMemoryError ? 
        MemoryDemo memoryDemo = new MemoryDemo();
        memoryDemo.run();

    }
}
