package session_data;

public class SessionData {

    private static boolean authenticated;
    private static String username;
    
    static{
        authenticated = false;
        username = null;
    }

    public static boolean isAuthenticated() {
        return authenticated;
    }

    public static void setAuthenticated(boolean authenticated) {
        SessionData.authenticated = authenticated;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        SessionData.username = username;
    }

}
