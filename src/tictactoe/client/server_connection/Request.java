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

    private static Socket socket;

    private static DataOutputStream dos;
    public static DataInputStream dis;

    static {
        try {
            socket = new Socket(ServerIP.getIP(), ServerIP.getPort());
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            System.out.println("Error: can't connect to server.");
        }
    }

    public static void registration(String username, String hashedPassword) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("header", "register");
        map.put("username", username);
        map.put("password", hashedPassword);

        JSONObject jsonObject = new JSONObject(map);

        dos.writeUTF(jsonObject.toString());
    }
    
    
    public static JSONObject receve() throws IOException{        
        return new JSONObject(dis.readUTF());
    }    
}
