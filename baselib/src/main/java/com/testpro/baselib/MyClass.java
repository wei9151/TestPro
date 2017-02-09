package com.testpro.baselib;

import java.util.Arrays;

public class MyClass {

    public static void test1() {
        String[] arr = {"program", "creek", "program", "creek", "java", "web", "program"};

        System.out.println(Arrays.toString(arr));
//
//        Stream<String> stream = Stream.of(arr);
//        long countA = stream.count();
//        System.out.println("countA == " + countA);
    }

}
