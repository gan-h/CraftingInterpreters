package Java.Backend;

import java.util.HashMap;
import java.util.Map;

import Java.Backend.Error.InterpretError;

public class Environment {
    public Map<String, Object> map;
    public Environment() {
        this.map = new HashMap<String, Object>();
    }

    Object get(String key) {
        return this.map.get(key);
    }

    void define(String key, Object val) {
        this.map.put(key, val);
    }
}
