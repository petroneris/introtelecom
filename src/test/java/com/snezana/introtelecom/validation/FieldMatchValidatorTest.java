package com.snezana.introtelecom.validation;

import com.snezana.introtelecom.dto.ClientChangePasswordDTO;
import com.snezana.introtelecom.dto.PhoneSaveDTO;
import com.snezana.introtelecom.dto.UserChangePasswordDTO;
import com.snezana.introtelecom.dto.UserSaveDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FieldMatchValidatorTest {

    @Mock
    private FieldMatch fieldMatch;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void testIsValid_PhoneSaveDTO_isValid() {
        when(fieldMatch.baseField()).thenReturn("phoneNumber");
        when(fieldMatch.matchField()).thenReturn("checkPhoneNumber");
        when(fieldMatch.message()).thenReturn("The phoneNumber and checkPhoneNumber fields must match");

        FieldMatchValidator fieldMatchValidator = new FieldMatchValidator();
        fieldMatchValidator.initialize(fieldMatch);

        String phoneNumber = "0732283499";
        String checkPhoneNumber = "0732283499";
        String packageCode = "01";
        PhoneSaveDTO phoneSaveDTO = new PhoneSaveDTO();
        phoneSaveDTO.setPhoneNumber(phoneNumber);
        phoneSaveDTO.setCheckPhoneNumber(checkPhoneNumber);
        phoneSaveDTO.setPackageCode(packageCode);
        Set<ConstraintViolation<PhoneSaveDTO>> violations = validator.validate(phoneSaveDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    public void testIsValid_PhoneSaveDTO_isNotValid() {
        when(fieldMatch.baseField()).thenReturn("phoneNumber");
        when(fieldMatch.matchField()).thenReturn("checkPhoneNumber");
        when(fieldMatch.message()).thenReturn("The phoneNumber and checkPhoneNumber fields must match");

        FieldMatchValidator fieldMatchValidator = new FieldMatchValidator();
        fieldMatchValidator.initialize(fieldMatch);

        String expectedMessage = "The phoneNumber and checkPhoneNumber fields must match";
        String actualMessage = "";
        String expectedPropertyPath = "checkPhoneNumber";
        String actualPropertyPath = "";
        String phoneNumber = "0732283488";
        String checkPhoneNumber = "0732283487";
        String packageCode = "01";
        PhoneSaveDTO phoneSaveDTO = new PhoneSaveDTO();
        phoneSaveDTO.setPhoneNumber(phoneNumber);
        phoneSaveDTO.setCheckPhoneNumber(checkPhoneNumber);
        phoneSaveDTO.setPackageCode(packageCode);
        Set<ConstraintViolation<PhoneSaveDTO>> violations = validator.validate(phoneSaveDTO);
        for(ConstraintViolation<PhoneSaveDTO> violation : violations) {
            actualPropertyPath = violation.getPropertyPath().toString();
            actualMessage = violation.getMessage();
        }

        assertThat(violations.size()).isEqualTo(1);
        assertThat(actualMessage).isEqualTo(expectedMessage);
        assertThat(actualPropertyPath).isEqualTo(expectedPropertyPath);
    }

    @Test
    void testIsValid_UserSaveDTO_isValid() {
        when(fieldMatch.baseField()).thenReturn("password");
        when(fieldMatch.matchField()).thenReturn("checkPassword");
        when(fieldMatch.message()).thenReturn("The password and checkPassword fields must match");

        FieldMatchValidator fieldMatchValidator = new FieldMatchValidator();
        fieldMatchValidator.initialize(fieldMatch);

        String phoneNumber = "0732283499";
        String username = "sneza3";
        String password = "sevenWonders";
        String checkPassword = "sevenWonders";
        String roleType = "CUSTOMER";
        UserSaveDTO userSaveDTO = new UserSaveDTO();
        userSaveDTO.setPhoneNumber(phoneNumber);
        userSaveDTO.setPassword(password);
        userSaveDTO.setCheckPassword(checkPassword);
        userSaveDTO.setUsername(username);
        userSaveDTO.setRoleType(roleType);
        Set<ConstraintViolation<UserSaveDTO>> violations = validator.validate(userSaveDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    public void testIsValid_UserSaveDTO_isNotValid() {
        when(fieldMatch.baseField()).thenReturn("password");
        when(fieldMatch.matchField()).thenReturn("checkPassword");
        when(fieldMatch.message()).thenReturn("The password and checkPassword fields must match");

        FieldMatchValidator fieldMatchValidator = new FieldMatchValidator();
        fieldMatchValidator.initialize(fieldMatch);

        String expectedMessage = "The password and checkPassword fields must match";
        String actualMessage = "";
        String expectedPropertyPath = "checkPassword";
        String actualPropertyPath = "";
        String phoneNumber = "0732283499";
        String username = "sneza3";
        String password = "sevenWonders";
        String checkPassword = "eightWonders";
        String roleType = "CUSTOMER";
        UserSaveDTO userSaveDTO = new UserSaveDTO();
        userSaveDTO.setPhoneNumber(phoneNumber);
        userSaveDTO.setPassword(password);
        userSaveDTO.setCheckPassword(checkPassword);
        userSaveDTO.setUsername(username);
        userSaveDTO.setRoleType(roleType);
        Set<ConstraintViolation<UserSaveDTO>> violations = validator.validate(userSaveDTO);
        for(ConstraintViolation<UserSaveDTO> violation : violations) {
            actualPropertyPath = violation.getPropertyPath().toString();
            actualMessage = violation.getMessage();
        }

        assertThat(violations.size()).isEqualTo(1);
        assertThat(actualMessage).isEqualTo(expectedMessage);
        assertThat(actualPropertyPath).isEqualTo(expectedPropertyPath);
    }

    @Test
    void testIsValid_UserChangePasswordDTO_isValid() {
        when(fieldMatch.baseField()).thenReturn("newPassword");
        when(fieldMatch.matchField()).thenReturn("checkNewPassword");
        when(fieldMatch.message()).thenReturn("The newPassword and checkNewPassword fields must match");

        FieldMatchValidator fieldMatchValidator = new FieldMatchValidator();
        fieldMatchValidator.initialize(fieldMatch);

        String username = "sneza3";
        String oldPassword = "oldWonders";
        String newPassword = "sevenWonders";
        String checkNewPassword = "sevenWonders";
        UserChangePasswordDTO userChangePasswordDTO = new UserChangePasswordDTO();
        userChangePasswordDTO.setUsername(username);
        userChangePasswordDTO.setOldPassword(oldPassword);
        userChangePasswordDTO.setNewPassword(newPassword);
        userChangePasswordDTO.setCheckNewPassword(checkNewPassword);
        Set<ConstraintViolation<UserChangePasswordDTO>> violations = validator.validate(userChangePasswordDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    void testIsValid_UserChangePasswordDTO_isNotValid() {
        when(fieldMatch.baseField()).thenReturn("newPassword");
        when(fieldMatch.matchField()).thenReturn("checkNewPassword");
        when(fieldMatch.message()).thenReturn("The newPassword and checkNewPassword fields must match");

        FieldMatchValidator fieldMatchValidator = new FieldMatchValidator();
        fieldMatchValidator.initialize(fieldMatch);

        String expectedMessage = "The newPassword and checkNewPassword fields must match";
        String actualMessage = "";
        String expectedPropertyPath = "checkNewPassword";
        String actualPropertyPath = "";
        String username = "sneza3";
        String oldPassword = "oldWonders";
        String newPassword = "sevenWonders";
        String checkNewPassword = "eightWonders";
        UserChangePasswordDTO userChangePasswordDTO = new UserChangePasswordDTO();
        userChangePasswordDTO.setUsername(username);
        userChangePasswordDTO.setOldPassword(oldPassword);
        userChangePasswordDTO.setNewPassword(newPassword);
        userChangePasswordDTO.setCheckNewPassword(checkNewPassword);
        Set<ConstraintViolation<UserChangePasswordDTO>> violations = validator.validate(userChangePasswordDTO);
        for(ConstraintViolation<UserChangePasswordDTO> violation : violations) {
            actualPropertyPath = violation.getPropertyPath().toString();
            actualMessage = violation.getMessage();
        }

        assertThat(violations.size()).isEqualTo(1);
        assertThat(actualMessage).isEqualTo(expectedMessage);
        assertThat(actualPropertyPath).isEqualTo(expectedPropertyPath);
    }

    @Test
    void testIsValid_ClientChangePasswordDTO_isValid() {
        when(fieldMatch.baseField()).thenReturn("newPassword");
        when(fieldMatch.matchField()).thenReturn("checkNewPassword");
        when(fieldMatch.message()).thenReturn("The newPassword and checkNewPassword fields must match");

        FieldMatchValidator fieldMatchValidator = new FieldMatchValidator();
        fieldMatchValidator.initialize(fieldMatch);

        String oldPassword = "oldWonders";
        String newPassword = "sevenWonders";
        String checkNewPassword = "sevenWonders";
        ClientChangePasswordDTO clientChangePasswordDTO = new ClientChangePasswordDTO();
        clientChangePasswordDTO.setOldPassword(oldPassword);
        clientChangePasswordDTO.setNewPassword(newPassword);
        clientChangePasswordDTO.setCheckNewPassword(checkNewPassword);
        Set<ConstraintViolation<ClientChangePasswordDTO>> violations = validator.validate(clientChangePasswordDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    void testIsValid_ClientChangePasswordDTO_isNotValid() {
        when(fieldMatch.baseField()).thenReturn("newPassword");
        when(fieldMatch.matchField()).thenReturn("checkNewPassword");
        when(fieldMatch.message()).thenReturn("The newPassword and checkNewPassword fields must match");

        FieldMatchValidator fieldMatchValidator = new FieldMatchValidator();
        fieldMatchValidator.initialize(fieldMatch);

        String expectedMessage = "The newPassword and checkNewPassword fields must match";
        String actualMessage = "";
        String expectedPropertyPath = "checkNewPassword";
        String actualPropertyPath = "";
        String oldPassword = "oldWonders";
        String newPassword = "sevenWonders";
        String checkNewPassword = "eightWonders";
        ClientChangePasswordDTO clientChangePasswordDTO = new ClientChangePasswordDTO();
        clientChangePasswordDTO.setOldPassword(oldPassword);
        clientChangePasswordDTO.setNewPassword(newPassword);
        clientChangePasswordDTO.setCheckNewPassword(checkNewPassword);
        Set<ConstraintViolation<ClientChangePasswordDTO>> violations = validator.validate(clientChangePasswordDTO);
        for(ConstraintViolation<ClientChangePasswordDTO> violation : violations) {
            actualPropertyPath = violation.getPropertyPath().toString();
            actualMessage = violation.getMessage();
        }

        assertThat(violations.size()).isEqualTo(1);
        assertThat(actualMessage).isEqualTo(expectedMessage);
        assertThat(actualPropertyPath).isEqualTo(expectedPropertyPath);
    }

}