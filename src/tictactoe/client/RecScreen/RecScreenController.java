/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.RecScreen;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import tictactoe.client.gameBoardWithFriend.GameBoardWithFriendController;


/**
 * FXML Controller class
 *
 * @author Kerolos
 */
public class RecScreenController implements Initializable {

    public String logFileName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
       // Call this method at the start of each new match 
        public void initializeLogFile() 
        {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmms").format(new Date());
            logFileName = "game_log_" + timestamp + ".txt";
            System.out.println("New match log file: " + logFileName);
        }
        
        public void logButtonClick(String buttonId, String symbol) 
        {
            
            String logEntry = "Button " + buttonId + " clicked with symbol " + symbol + "\n";
            try (FileWriter writer = new FileWriter("gamelogs/"+logFileName, true)) 
            { // Append mode
                writer.write(logEntry);
                System.out.println("Log file saved at: " + new java.io.File(logFileName).getAbsolutePath());

            } 
            catch (IOException e) 
            {
                System.err.println("Error writing to log file: " + e.getMessage());
            }
        }
    
}
