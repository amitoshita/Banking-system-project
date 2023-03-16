package exceptions;

public class fileExtentionException extends Exception {

    String extention;
    public fileExtentionException(String extention){
        this.extention = extention;
    }

    @Override
    public String getMessage() {
        return "Exception: The file extention is " + extention + "\n";
    }

}
