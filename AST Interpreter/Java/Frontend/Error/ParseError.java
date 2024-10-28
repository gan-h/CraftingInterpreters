package Java.Frontend.Error;

public class ParseError extends Error {
    public final int lineNumber;
    public ParseError() {
        super("A parsing error occurred");
        this.lineNumber = -1;
    }

    public ParseError(String message) {
        super(message);
        this.lineNumber = -1;
    }

    public ParseError(String message, int lineNumber) {
        super(message);
        this.lineNumber = lineNumber;
    }
}