package tictactoe.client.session_data;

public class SessionData {

    private static boolean authenticated;
    private static String username;
    private static int score;

    static {
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

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        SessionData.score = score;
    }

    public static void deleteDate() {
        authenticated = false;
        username = "";
        score = 0;
    }
}
