package com.itt.newsaggregatorclient.validator;

import java.util.regex.Pattern;
import com.itt.newsaggregatorclient.constants.Constants;

public class InputValidator {
    public static boolean isEmailValid(String email) {
        String pattern = Constants.EMAIL_REGEX_PATTERN;
        return (!email.isBlank() && Pattern.matches(pattern, email));
    }

    public static  boolean isPasswordValid(String password) {
        String pattern = Constants.PASSWORD_REGEX;
        return (!password.isBlank() && Pattern.matches(pattern, password));
    }
}
