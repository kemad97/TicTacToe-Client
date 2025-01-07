package tictactoe.client.server_ip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerIP {

    private static DataOutputStream dos;

    public static String getIP(String ipFilePath) {
        File ipFile = new File(ipFilePath);

        String ip = "";

        try (FileInputStream inputStream = new FileInputStream(ipFile);
                DataInputStream dis = new DataInputStream(inputStream);) {

            ip = dis.readUTF();

        } catch (FileNotFoundException ex) {
            saveIP(ipFilePath, "");
        } catch (IOException ex) {
            Logger.getLogger(ServerIP.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ip;
    }

    public static void saveIP(String ipFilePath, String ip) {
        File ipFile = new File(ipFilePath);

        try (FileOutputStream outputStream = new FileOutputStream(ipFile);
                DataOutputStream dos = new DataOutputStream(outputStream);) {

            dos.writeUTF(ip);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerIP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerIP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
