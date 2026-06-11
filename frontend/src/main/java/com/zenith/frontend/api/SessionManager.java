package com.zenith.frontend.api;

public class SessionManager {

    private static String token;
    private static Long userId;
    private static String fullName;
    private static String email;
    private static String resetEmail;

    private SessionManager() {}

    public static void setSession(String token, Long userId, String fullName, String email) {
        SessionManager.token = token;
        SessionManager.userId = userId;
        SessionManager.fullName = fullName;
        SessionManager.email = email;
    }

    public static void clear() {
        token = null;
        userId = null;
        fullName = null;
        email = null;
        resetEmail = null;
    }

    public static boolean isLoggedIn() {
        return token != null && !token.isBlank();
    }

    public static String getToken() {
        return token;
    }

    public static Long getUserId() {
        return userId;
    }

    public static String getFullName() {
        return fullName;
    }

    public static String getEmail() {
        return email;
    }

    public static void setResetEmail(String resetEmail) {
        SessionManager.resetEmail = resetEmail;
    }

    public static void clearResetEmail() {
        resetEmail = null;
    }

    public static String getResetEmail() {
        return resetEmail;
    }
}
