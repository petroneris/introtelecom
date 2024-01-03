package com.snezana.introtelecom.validation;

import com.snezana.introtelecom.dto.PhoneSaveDTO;
import com.snezana.introtelecom.dto.UserChangePasswordDTO;
import com.snezana.introtelecom.dto.UserSaveDTO;
import org.slf4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/* used for password and phone number validations */
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FieldMatchValidator.class);


    private String baseField;
    private String matchField;
    private String message;

    @Override
    public void initialize(FieldMatch constraint) {
        baseField = constraint.baseField();
        matchField = constraint.matchField();
        message = constraint.message();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        boolean toReturn = false;
        if (obj instanceof PhoneSaveDTO) {
            PhoneSaveDTO phoneSaveDTO = (PhoneSaveDTO) obj;
            toReturn = phoneSaveDTO.getPhoneNumber().equals(phoneSaveDTO.getCheckPhoneNumber());
        }
        if (obj instanceof UserSaveDTO) {
            UserSaveDTO userSaveDTO = (UserSaveDTO) obj;
            toReturn = userSaveDTO.getPassword().equals(userSaveDTO.getCheckPassword());
        }
        if (obj instanceof UserChangePasswordDTO) {
            UserChangePasswordDTO userChangePasswordDTO = (UserChangePasswordDTO) obj;
            toReturn = userChangePasswordDTO.getNewPassword().equals(userChangePasswordDTO.getCheckNewPassword());
        }
        if (!toReturn) {
            context.disableDefaultConstraintViolation();
            // In the initialiaze method you get the errorMessage:
            // constraintAnnotation.message();
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(matchField)
                    .addConstraintViolation();
        }
        return toReturn;
    }

}
