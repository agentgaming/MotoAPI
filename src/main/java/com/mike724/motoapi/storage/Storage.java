package com.mike724.motoapi.storage;

import java.util.HashMap;

public class Storage {

    private DataStorage rawStorage;
    private HashMap<String, HashMap<String, Object>> cache;

    public Storage(String ba_user, String ba_pass, String apiKey) throws Exception {
        rawStorage = new DataStorage(ba_user, ba_pass, apiKey);
        cache = new HashMap<>();
    }

    @SuppressWarnings("unused")
    public Object getObject(String key, Class c) {
        String cName = c.getName();
        if(cacheContains(key, c)) {
            return cache.get(key).get(cName);
        } else {
            Object obj = rawStorage.getObject(c, key);
            if(obj == null) {
                return null;
            } else if(!c.isInstance(obj)) {
                return null;
            }
            cacheObject(key, obj);
            return obj;
        }
    }

    @SuppressWarnings("unused")
    public void saveObject(String key, Class c) {
        saveObject(key, c, true);
    }

    public void saveObject(String key, Class c, boolean keepCache) {
        if(!cacheContains(key, c)) {
            return;
        }
        Object obj = cache.get(key).get(c.getName());
        rawStorage.writeObject(obj, key);
        if(!keepCache) {
            removeFromCache(key, c);
        }
    }

    public void removeFromCache(String key, Class c) {
        if(cacheContains(key, c)) {
            cache.get(key).remove(c.getName());
        }
    }

    public boolean cacheContains(String key, Class c) {
        return(cache.containsKey(key) && cache.get(key).containsKey(c.getName()));
    }

    public void cacheObject(String key, Object obj) {
        if(!cache.containsKey(key)) {
            cache.put(key, new HashMap<String, Object>());
        }
        cache.get(key).put(obj.getClass().getName(), obj);
    }

}
