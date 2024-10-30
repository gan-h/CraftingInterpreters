package Java.Backend;

import java.util.List;

import Java.Backend.Error.InterpretError;
import Java.Backend.Error.Return;
import Java.Frontend.Statement;

public class LoxFunction implements LoxCallable {
    private final Statement.FuncDecl decl;
    private final Environment closure;
    @Override
    public int arity() {
        return decl.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> args) {
        if (args.size() != arity()) throw new InterpretError("Function was called with " + args.size() + " arguments, but expected " + arity());
        Environment newEnvironment = new Environment(closure);
        for (int i = 0; i < args.size(); i++) {
            newEnvironment.define(decl.params.get(i).lexeme, args.get(i));
        }
        Object returnValue = null;
        Environment original = interpreter.environment;
        interpreter.environment = newEnvironment;
        try {
            interpreter.evaluate(decl.body);
        } catch (Return r) {
            returnValue = r.returnValue;
        } finally {
            interpreter.environment = original;
        }
        return returnValue;
    }

    @Override
    public String toString() {
        return "<function " + decl.name.lexeme + ">";
    }


    public LoxFunction(Statement.FuncDecl decl, Environment outer) {
        this.decl = decl;
        this.closure = new Environment(outer);
    }

}