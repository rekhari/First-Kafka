package com.kafka;

import java.io.PrintStream;
import java.util.stream.IntStream;

public class Test {
    public static void main(String[] args) {
        PrintStream o = System.out;

        IntStream.range(0, 10).forEach(o::println);
    }
}
