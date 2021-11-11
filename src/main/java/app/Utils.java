package app;

public class Utils {

    private static final String phoneRegex = "^\\+7[0-9]{10}$";

    public static boolean checkPhoneNumber(String phone) {
        return phone.matches(phoneRegex);
    }


}
