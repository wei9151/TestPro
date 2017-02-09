package com.testpro.domain;

/**人*/
public class Person {
    private String name;
    private double salary;

    public Person(String n, double s) {
        name = n;
        salary = s;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }
}
