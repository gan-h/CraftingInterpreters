package Java.Backend;

import java.util.List;
import java.util.Map;
class LoxClass implements LoxCallable {
    final String name;
    LoxClass(String name) {
        this.name = name;
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
        // TODO Auto-generated method stub
        LoxInstance instance = new LoxInstance(this);
        return instance;
    }
}

