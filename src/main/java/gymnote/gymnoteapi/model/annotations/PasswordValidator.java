package gymnote.gymnoteapi.model.annotations;


import gymnote.gymnoteapi.config.ValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        return password.matches(ValidationConstants.PASSWORD_PATTERN);
    }
}


