package com.aad.booknest.javalab;

import java.util.*;

class GenericsDemo {
    static double sum(List<? extends Number> list) {
        double total = 0;
        for(Number n : list) total += n.doubleValue();
        // list.add(1); // compile error: the list might really be List<Double>
        return total;
    }

    
}

public class AdditionalJava {
    
}
