package exceptions;

public class fileDivideYazPaymentException extends Exception {

    @Override
    public String getMessage() {
        return "One of the loan's total yaz not dividing in yaz per payment";
    }
}
