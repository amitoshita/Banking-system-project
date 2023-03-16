package exceptions;

public class fileCategoriesException extends Exception{

    @Override
    public String getMessage() {
        return "Exception: The file's categories is not according the format.";
    }
}
