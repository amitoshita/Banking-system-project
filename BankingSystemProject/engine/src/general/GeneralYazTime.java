package general;

public class GeneralYazTime {

    private static int currYaz = 1;


    public static void addCurrYaz() {
        currYaz++;
    }

    public static int getCurrYaz() {
        return currYaz;
    }
}
