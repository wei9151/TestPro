package com.testpro.domain;

/**测试主体doman*/
public class Model <T>{
    private T first;
    private T second;

    public Model(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public void setFirst(T newValue) {
        first = newValue;
    }

    public void setSecond(T newValue) {
        second = newValue;
    }

}
