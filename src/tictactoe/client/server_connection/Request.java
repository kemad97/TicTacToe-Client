package tictactoe.client.server_connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
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

    public static Request getInstance() throws IOException {
        if (instance == null) {
            instance = new Request();
        }

        return instance;
    }
    
    public static void deleteInstance(){
        instance = null;
    }

    public void disconnectToServer() throws IOException {
        dos.close();
        dis.close();
        socket.close();
        instance = null;
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
    
    public String sendRequest(String request) throws IOException {
        dos.writeUTF(request);
        return dis.readUTF();
    }
    
    // New methods for game and match functionalities
    public void sendMatchRequest(String opponentUsername) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("header", "match_request");
        map.put("opponent", opponentUsername);

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
