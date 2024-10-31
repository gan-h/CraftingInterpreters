package Java.Backend;

import java.util.List;
import java.util.Map;
class LoxClass implements LoxCallable {
    final String name;
    private final Map<String, LoxFunction> methods;
    LoxClass(String name, Map<String, LoxFunction> methods) {
        this.name = name;
        this.methods = methods;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int arity() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> args) {
        LoxInstance instance = new LoxInstance(this);
        return instance;
    }

    public LoxFunction findMethod(String functionName) {
        return methods.get(functionName);
    }
}

