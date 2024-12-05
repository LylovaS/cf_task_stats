package ru.nsu.fit.backend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandleChecker {
    private static final String HANDLE_REGEXP = "^[a-zA-Z0-9_-]+$";

    public static boolean check_handle(String handle) {
        Pattern pattern = Pattern.compile(HANDLE_REGEXP);
        Matcher matcher = pattern.matcher(handle);
        return matcher.matches();
    }
}
