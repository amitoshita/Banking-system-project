package exceptions;

public class fileCustomersNamesException extends Exception{

    @Override
    public String getMessage() {
        return "Exception: There is a name that returns a few times. " + "\n";
    }
}
