package com.reactlibrary.printer.printing;

public class FinalVarContainer<T> {

    private T value;
    public T get(){
        return value;
    }
    public synchronized void set(T value){
        this.value = value;
    }

    public FinalVarContainer(T value){
        this.value = value;
    }
}
