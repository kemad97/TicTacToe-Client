/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.RecScreen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tictactoe.client.main_screen.FXMLMainScreenController;
import tictactoe.client.replay.ReplayController;
import tictactoe.client.register.FXMLRegisterationScreenController;
import session_data.SessionData;

/**
 * FXML Controller class
 *
 * @author Kerolos
 */
public class RecScreenController implements Initializable {
    
    private static final Logger LOGGER = Logger.getLogger(RecScreenController.class.getName());


    public String logFileName;
    
    private  String logDirectory ;

    @FXML
    private ListView<String> fileListView;
    @FXML
    private ImageView backBtn;
    
    private ReplayController replayController;
    
   // FXMLRegisterationScreenController regObj = new FXMLRegisterationScreenController();
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
       // loadRecordedFiles ();
        
    }    
    
       // Call this method at the start of each new match 
        public void initializeLogFile() 
        {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmms").format(new Date());
            logFileName = "game_log_" + timestamp ;
            System.out.println("New match log file: " + logFileName);
        }
     
        
        public void logButtonClick(String buttonId, String symbol) {
            String logEntry = buttonId + "," + symbol + "\n";
            File userFolder;
            
            // Create user folder 
            if(SessionData.isAuthenticated()) 
            {
                String username = SessionData.getUsername();
                 userFolder = new File( "gamelogs/online/" + username);

                 if (!userFolder.exists()) {
                    userFolder.mkdirs(); // Create the directory if it doesn't exist
                    System.out.println("New directory created for user: " + username);
                }
             
            }          
            else
            {
                
                 userFolder = new File( "gamelogs/offline/" );
            
            }
           

            // Create log file within user's folder
            File logFile = new File(userFolder, logFileName + ".txt");
            try (FileWriter writer = new FileWriter(logFile, true)) { // Append mode
                writer.write(logEntry);
                System.out.println("Log entry saved in: " + logFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error writing to log file: " + e.getMessage());
            }
        }
        
        

        
        public void loadRecordedFiles (String directoryPath)
        {
            File directory= new File (directoryPath);
             if (!directory.exists()) 
             {
                directory.mkdirs(); // Create the directory if it doesn't exist
                System.out.println("new dir created");
             }
             
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
            ObservableList<String> fileNames = FXCollections.observableArrayList();

            if (files != null) {
                for (File file : files) {
                    fileNames.add(file.getName());
                }
            }

            if (fileListView != null) {
                fileListView.setItems(fileNames);
            }
        
        
        
        }

    @FXML
    private void openGameLogFile(MouseEvent event) 
    {
        if (event.getClickCount() == 2) 
        {
            String selectedFile = fileListView.getSelectionModel().getSelectedItem(); // Get the selected file name
            if (selectedFile != null) 
            {
                if(OnlineOfflineUIController.atOnlineFolder)
                {
                    
                    logDirectory= "gamelogs/online/";
                }
                else
                {
                     logDirectory= "gamelogs/offline/";
                }
                
                openFile(logDirectory + selectedFile, event); 
            }
        }
    }

    private void openFile(String filePath, MouseEvent event) 
    {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tictactoe/client/replay/Replay.fxml"));
            Parent root = loader.load();
            replayController = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            
            System.out.println("Replay match");
            File file = new File(filePath);
            if (file.exists()){
                replayController.replay(filePath);
            }
            else 
            {
                System.err.println("File does not exist: " + filePath);
            }
            
            } catch (IOException ex) {
            Logger.getLogger(RecScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    

    @FXML
    private void goBackMainMenu(MouseEvent event) 
    {
        
           try {

            System.out.println("Back To Main Screen");

            Parent root = FXMLLoader.load(getClass().getResource("/tictactoe/client/main_screen/FXMLMainScreen.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } 
        
        catch (IOException ex) {

            Logger.getLogger(FXMLMainScreenController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
