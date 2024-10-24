package Java;

public class InterpretError extends Error {
    InterpretError(String message) {
        super(message);
    }

    InterpretError() {
        super();    
    }
}
