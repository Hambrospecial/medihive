package com.hamidspecial.medihive.auth.service;

import com.hamidspecial.medihive.auth.model.ApplicationParameter;
import com.hamidspecial.medihive.exception.BadRequestException;
import com.hamidspecial.medihive.exception.NotFoundException;
import com.hamidspecial.medihive.util.ConstantUtils;
import com.hamidspecial.medihive.util.ObjectSerializer;
import com.hamidspecial.medihive.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PasswordPolicyService {

    // Regex patterns
    private static final String UPPERCASE_REGEX = "[A-Z]";
    private static final String LOWERCASE_REGEX = "[a-z]";
    private static final String DIGIT_REGEX = "\\d";
    private static final String SPECIAL_CHAR_REGEX = "[!@#$%^&*(),.?\":{}|<>]";

    private final ApplicationParameterService parameterService;

    public void validatePassword(String password) {
        List<String> validationErrors = new ArrayList<>();
        Result<ApplicationParameter> result = parameterService.getApplicationParameterByKey(ConstantUtils.PASSWORD_POLICY);
        if (result.getData().getValue() == null) {
            throw new NotFoundException("404", "Password strength configuration not found");
        }
        PasswordCheckList passwordCheckList = ObjectSerializer.deserializeFromJson(result.getData().getValue(), PasswordCheckList.class);
        validatePasswordLength(password, passwordCheckList.getMinLength(), validationErrors);
        validateCharacterType(password, UPPERCASE_REGEX, passwordCheckList.isRequireUppercase(), "uppercase", validationErrors);
        validateCharacterType(password, LOWERCASE_REGEX, passwordCheckList.isRequireLowercase(), "lowercase", validationErrors);
        validateCharacterType(password, DIGIT_REGEX, passwordCheckList.isRequireDigit(), "digit", validationErrors);
        validateCharacterType(password, SPECIAL_CHAR_REGEX, passwordCheckList.isRequireSpecial(), "special", validationErrors);
        validateUniqueChars(password, passwordCheckList.getMinUniqueChars(), validationErrors);

        if (!validationErrors.isEmpty()) {
            throw new BadRequestException("400", formatErrorMessage(validationErrors));
        }
    }

    private void validatePasswordLength(String password, int minLength, List<String> errors) {
        if (password.length() < minLength) {
            errors.add(String.format("%d characters long", minLength));
        }
    }

    private void validateCharacterType(String password, String regex,
                                       boolean required, String errorMessage,
                                       List<String> errors) {
        if (required && !Pattern.compile(regex).matcher(password).find()) {
            errors.add("one " + errorMessage + " character");
        }
    }

    private void validateUniqueChars(String password, int minUniqueChars, List<String> errors) {
        if (password.chars().distinct().count() < minUniqueChars) {
            errors.add(String.format("%d unique characters", minUniqueChars));
        }
    }

    private String formatErrorMessage(List<String> errors) {
        StringBuilder errorMessage = new StringBuilder("Password must contain at least ");
        for (int i = 0; i < errors.size(); i++) {
            if (i == 0) {
                errorMessage.append(errors.get(i));
            } else if (i < errors.size() - 1) {
                errorMessage.append(", ").append(errors.get(i));
            } else {
                errorMessage.append(", and ").append(errors.get(i));
            }
        }
        return errorMessage.toString();
    }

}
