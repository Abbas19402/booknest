package com.aad.booknest.javalab;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.*;

class LambdaDemo {
    public void run() {
        Predicate<String> isLong = s -> s.length() > 4;
        Function<String, Integer> len = String::length;
        Consumer<String> printer = s -> System.out.println(">> "+s);
        Supplier<List<String>> maker = ArrayList::new;
        
        System.out.println(isLong.test("spring"));
        System.out.println(len.apply("boot"));
        printer.accept("FooBar");
        List<String> fresh = maker.get();

        Runnable r = () -> System.out.println("runnable"); // this runnable is a functional interface 
        r.run();
    }
}

record Employee(String name, String dept, double salary) {}
class StreamDemo {
    public void run() {
        List<Employee> emps = List.of(
            new Employee("Asha", "IT", 90000),
            new Employee("Ravi","IT",120000),
            new Employee("Meera", "HR", 70000),
            new Employee("John","HR",65000),
            new Employee("Zoya", "Ops", 80000)
        );

        // Employees with salary above 75k
        List<String> empsAbove75 = emps.stream()
            .filter(e -> e.salary() > 70000)
            .map(Employee::name)
            .sorted()
            .collect(Collectors.toList());
        System.out.println(empsAbove75);

        // average salary
        double averageSalary = emps.stream()
            .mapToDouble(Employee::salary)
            .average()
            .orElse(0);
        System.out.println(averageSalary);

        // Group by department
        Map<String, List<Employee>> byDept = emps.stream()
            .collect(Collectors.groupingBy(Employee::dept));
        System.out.println(byDept.keySet());

        // Highest Salary Per Department
        Map<String, Optional<Employee>> top = emps.stream()
            .collect(
                Collectors.groupingBy(
                    Employee::dept, 
                    Collectors.maxBy(Comparator.comparingDouble(Employee::salary))
                ) 
            );
        top.forEach((d,e) -> System.out.println(d+" -> "+e.get().name()+", "+e.get().salary()));

        // second highest salary overall
        double secondHighestSalaryOverall = emps.stream()
            .map(Employee::salary) // returned salary
            .distinct() // unique values only
            .sorted(Comparator.reverseOrder()) // descending order
            .skip(1) // skipped first
            .findFirst() // picked up second after the first was skipped
            .orElseThrow();
        System.out.println(secondHighestSalaryOverall);

        // Flattened List
        List<List<Integer>> nested = List.of(List.of(1,2), List.of(3), List.of(4,5));
        List<Integer> flattenedList = nested.stream()
            .flatMap(List::stream)
            .map(n -> n * n)
            .toList();
        System.out.println(flattenedList);

        // laziness proof: nothing prints without a terminal operation
        emps.stream().filter(e -> { 
            System.out.println("checking "+e.name()); 
            return true; 
        }); // prints nothing

        // practice
        //Employee's count per department
        Map<String, Long> countPerDept = emps.stream()
            .collect(
                Collectors.groupingBy(
                    Employee::dept,
                    Collectors.counting()
                )
            );
        countPerDept.forEach((d,c) -> System.out.println("Department: "+d+", Count: "+c));

        // Total Salary Per Department
        Map<String, Double> totalSalaryPerDepartment = emps.stream()
            .collect(
                Collectors.groupingBy(
                    Employee::dept,
                    Collectors.summingDouble(Employee::salary)
                )
            );
        totalSalaryPerDepartment.forEach((d,s) -> System.out.println("Department: "+d+", Total Salary: "+s));
    }
}

class OptionalDemo {
    static Optional<String> findEmail(int id) {
        return id == 1 ? Optional.of("asha@mail.com") : Optional.empty();
    }

    public void run() {
        System.out.println(findEmail(1).map(String::toUpperCase).orElse("none")); // ASHA@MAIL.COM
        System.out.println(findEmail(2).orElse("none")); // none
        
        findEmail(1).ifPresent(e -> System.out.println("found " + e));
    }
}

public class Java8Plus {
    public static void main(String args[]) {
        // Q11. What are lambdas and functional interfaces?
        LambdaDemo LambdaDemo = new LambdaDemo();
        LambdaDemo.run();

        // Q12. Streams: map, filter, flatMap, collect, groupingBy. Intermediate vs terminal operations.
        StreamDemo streamDemo = new StreamDemo();
        streamDemo.run();

        // Q13. What is Optional? How to use it properly?
        OptionalDemo optionalDemo = new OptionalDemo();
        optionalDemo.run();
    }
}
