package com.mike724.motoapi.persistence;

public abstract class DataClass {

    private Class c;

    public DataClass(Class c) {
        this.c = c;
    }

    public abstract Object getNewInstance(String key);

}
