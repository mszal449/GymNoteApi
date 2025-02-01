package gymnote.gymnoteapi.config;

public class ValidationConstants {
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9]{3,20}$";
}
