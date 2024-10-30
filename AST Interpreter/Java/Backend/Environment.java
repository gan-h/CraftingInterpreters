package Java.Backend;

import java.util.HashMap;
import java.util.Map;

import Java.Backend.Error.InterpretError;

public class Environment {
    final Environment enclosing;
    public Map<String, Object> map;
    public Environment(Environment enclosing) {
        this.map = new HashMap<String, Object>();
        this.enclosing = enclosing;
    }
    public Environment() {
        this.map = new HashMap<String, Object>();
        this.enclosing = null;
    }

    Object get(String key) {
        if (map.containsKey(key)) {
            return this.map.get(key);
        } else {
            if(enclosing != null) return enclosing.get(key);
            throw new InterpretError("Undefined key: " + key);
        }
    }

    void define(String key, Object val) {
        this.map.put(key, val);
    }
    void assign(String name, Object value) {
        if (map.containsKey(name)) {
            map.put(name, value);
            return;
        }
        if(enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        throw new InterpretError("Undefined variable '" + name + "'.");
    }

    
}
