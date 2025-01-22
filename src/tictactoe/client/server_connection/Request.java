package tictactoe.client.server_connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import tictactoe.client.server_ip.ServerIP;

public class Request {

    private static Request instance;

    private Socket socket;
    private DataOutputStream dos;
    public DataInputStream dis;

    private Request() throws IOException {
            socket = new Socket(ServerIP.getIP(), ServerIP.getPort());
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
    }

    public static synchronized Request getInstance() throws IOException {
    if (instance == null) {
        instance = new Request();
    }
    return instance;
}

    
    public static void deleteInstance(){
        instance = null;
    }

   public void disconnectToServer() throws IOException {
    try {
        dos.close();
        dis.close();
        socket.close();
    } catch (IOException ex) {
        Logger.getLogger(Request.class.getName()).log(Level.SEVERE, "Error while closing connection", ex);
    } finally {
        instance = null;  // Ensure singleton instance is reset after disconnection.
    }
}

    public void registration(String username, String hashedPassword) throws IOException {

        Map<String, String> map = new HashMap<>();
        map.put("header", "register");
        map.put("username", username);
        map.put("password", hashedPassword);

        JSONObject jsonObject = new JSONObject(map);

        System.out.println(jsonObject);

        dos.writeUTF(jsonObject.toString());
    }

    public void login(String username, String password) throws IOException {

        Map<String, String> map = new HashMap<>();
        map.put("header", "login");
        map.put("username", username);
        map.put("password", password);

        JSONObject jsonObject = new JSONObject(map);

        System.out.println(jsonObject);
        dos.writeUTF(jsonObject.toString());
    }

    public JSONObject recieve() throws IOException {
        return new JSONObject(dis.readUTF());
    }
    
    public void sendRequest(String request) throws IOException {
        dos.writeUTF(request);
    }
    
    // New methods for game and match functionalities
    public void sendMatchRequest(String opponentUsername) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("header", "request_start_match");
        map.put("targetPlayer", opponentUsername);

        JSONObject jsonObject = new JSONObject(map);
        dos.writeUTF(jsonObject.toString());
    }
    
    
    public void sendMatchResponse(String opponentUsername, boolean isAccepted) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("header", "match_response");
        map.put("opponent", opponentUsername);
        map.put("response", isAccepted ? "accepted" : "declined");

        JSONObject jsonObject = new JSONObject(map);
        dos.writeUTF(jsonObject.toString());
    }

      public void sendMove(String opponentUsername, String move) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("header", "move");
        map.put("opponent", opponentUsername);
        map.put("move", move);

        JSONObject jsonObject = new JSONObject(map);
        dos.writeUTF(jsonObject.toString());
    }

}
