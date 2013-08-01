package com.mike724.motoapi.persistence;

import java.util.ArrayList;
import java.util.HashMap;

/** Wrapper for DataStorage that enables caching and helper methods */
public class Persistence {

    //String is the key, for example the player's name
    //The Object array list is all the cached objects for that key
    private HashMap<String, ArrayList<Object>> cache;

    private DataClassManager dataClassManager;

    public Persistence() {
        cache = new HashMap<>();
        dataClassManager = new DataClassManager();
    }

    public DataClassManager getDataClassManager() {
        return dataClassManager;
    }

}
