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

    public JSONObject receve() throws IOException {
        return new JSONObject(dis.readUTF());
    }

}