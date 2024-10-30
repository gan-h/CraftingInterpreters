package Java.Backend.Error;

public class Return extends Error {
    public final Object returnValue;
    public Return(Object returnValue) {
        super(null, null, false, false);
        this.returnValue = returnValue;
    }
}
