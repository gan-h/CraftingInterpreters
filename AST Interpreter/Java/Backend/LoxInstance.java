package Java.Backend;

import java.util.HashMap;
import java.util.Map;

public class LoxInstance {
    private LoxClass klass;
    private final Map<String, Object> fields = new HashMap<>();

    LoxInstance(LoxClass klass) {
        this.klass = klass;
    } 

    public Object get(String name) {
        if (fields.containsKey(name)) {
            return fields.get(name);
        } 

        LoxFunction method = klass.findMethod(name);
        if (method != null) return method;

        return null;
    } 

    @Override
    public String toString() {
        return klass.name + " instance";
    }

    public void set(String lexeme, Object value) {
        fields.put(lexeme, value);
    }

}
