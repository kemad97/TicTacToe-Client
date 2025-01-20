package tictactoe.client.server_ip;

import java.io.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerIP {

    private static final Logger LOGGER = Logger.getLogger(ServerIP.class.getName());
    private static final String FILE_PATH = "./src/media/server_ip/server_ip.txt";
    private static final int DEFAULT_PORT = 8080;

    /*
     * Retrieves the server IP address from the file.
     * If the file does not exist, it creates one with an empty IP address.
     */
    
    public static String getIP() {
        File ipFile = new File(FILE_PATH);
        String ip = "";

        
        if (!ipFile.exists()) {
            saveIP("");
            LOGGER.info("IP file created at: " + FILE_PATH);
        }

        try (FileInputStream inputStream = new FileInputStream(ipFile);
             DataInputStream dis = new DataInputStream(inputStream)) {

            ip = dis.readUTF();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error reading IP file", ex);
        }

        return ip;
    }

    
    public static int getPort() {
        return DEFAULT_PORT;
    }

    
    public static void saveIP(String ip) {
        File ipFile = new File(FILE_PATH);
        File parentDir = ipFile.getParentFile();

        try {
            // Ensure parent directory exists
            if (!parentDir.exists()) {
                parentDir.mkdirs();
                LOGGER.info("Created directories for IP file: " + parentDir.getAbsolutePath());
            }

            // Write IP to file
            try (FileOutputStream outputStream = new FileOutputStream(ipFile);
                 DataOutputStream dos = new DataOutputStream(outputStream)) {
                dos.writeUTF(ip.trim());
            }

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error saving IP to file", ex);
        }
    }

   
    public static boolean isValidIP(String ip) {
        StringTokenizer tokenizer = new StringTokenizer(ip, ".");

        if (tokenizer.countTokens() != 4) {
            return false;
        }

        while (tokenizer.hasMoreTokens()) {
            try {
                int num = Integer.parseInt(tokenizer.nextToken());
                if (num < 0 || num > 255) {
                    return false;
                }
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        return true;
    }
}

/*
package tictactoe.client.server_ip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerIP {

    private static DataOutputStream dos;
    private static final String filePath = "./src/media/server_ip/server_ip.txt";

    public static String getIP() {
        File ipFile = new File(filePath);

        String ip = "";

        try (FileInputStream inputStream = new FileInputStream(ipFile);
                DataInputStream dis = new DataInputStream(inputStream);) {

            ip = dis.readUTF();

        } catch (FileNotFoundException ex) {
            saveIP("");
        } catch (IOException ex) {
            Logger.getLogger(ServerIP.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ip;
    }
    
    public static int getPort(){
        return 8080;
    }

    public static void saveIP(String ip) {
        File ipFile = new File(filePath);

        try (FileOutputStream outputStream = new FileOutputStream(ipFile);
                DataOutputStream dos = new DataOutputStream(outputStream);) {

            dos.writeUTF(ip.trim());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerIP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerIP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean isValidIP(String ip) {
        StringTokenizer arr = new StringTokenizer(ip, ".");
        
        if (arr.countTokens() != 4) {
            return false;
        }

        while (arr.hasMoreTokens()) {
            try {
                Integer num = Integer.parseInt(arr.nextToken());
                if (num < 0 || num > 256) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}
*/
