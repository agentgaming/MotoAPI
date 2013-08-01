package com.mike724.motoapi.persistence;

import java.util.HashMap;

public class DataClassManager {

    public HashMap<Integer, DataClass> dataClasses;

    public DataClassManager() {
        dataClasses = new HashMap<>();
    }

    public int registerDataClass(DataClass dataClass) {
        if(dataClasses.containsValue(dataClass)) {
            return -1;
        }
        int size = dataClasses.size();
        dataClasses.put(size, dataClass);
        return size;
    }

}
