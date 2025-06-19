package com.itt.newsaggregatorclient.constants;

public class Constants {
    public static final String MOBILE_REGEX_PATTERN = "^[0-9]{10}$";
    public static final String EMAIL_REGEX_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=_()])(?=\\S+$).{8,15}$";
    public static final String LOGIN = "login";
    public static final String SIGNUP = "signup";
    public static final String EXIT = "Exit";
    public static final String BASE_URL = "http://localhost:8080/news-aggregator";
}
