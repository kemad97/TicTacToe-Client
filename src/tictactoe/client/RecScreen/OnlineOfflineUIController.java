/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.RecScreen;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import session_data.SessionData;
import tictactoe.client.main_screen.FXMLMainScreenController;
import tictactoe.client.RecScreen.RecScreenController;
import session_data.SessionData;


/**
 * FXML Controller class
 *
 * @author Kerolos
 */
public class OnlineOfflineUIController implements Initializable 
{
    
    private static final Logger LOGGER = Logger.getLogger(OnlineOfflineUIController.class.getName());
    private static final String ONLINE_LOG_DIRECTORY = "gamelogs/online/";
    private static final String OFFLINE_LOG_DIRECTORY = "gamelogs/offline/";
    public static Boolean   atOnlineFolder ;

    @FXML
    private Button onlineRecordsBtn;
    @FXML
    private Button offlineRecordsBtn;
    @FXML
    private ImageView backBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

  @FXML
private void showOnlineRecords(ActionEvent event) {
    String username = SessionData.getUsername();
    if (username == null || username.isEmpty()) {
        LOGGER.warning("Username not set in session data.");
        return;
    }

    String directoryPath = "gamelogs/online/" + username;
    atOnlineFolder=true;

    navigateToRecScreen(event, directoryPath);
}

@FXML
private void showOfflineRecords(ActionEvent event)
{
    String directoryPath = "gamelogs/offline/";
        atOnlineFolder=false;

    navigateToRecScreen(event, directoryPath);
}

private void navigateToRecScreen(ActionEvent event, String directoryPath) {
    try {
        // Load the RecScreen FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tictactoe/client/RecScreen/RecScreen.fxml"));
        Parent root = loader.load();

        RecScreenController recScreenController = loader.getController();

        // Pass the directory path to the RecScreenController
        recScreenController.loadRecordedFiles(directoryPath);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    } catch (IOException ex) {
        LOGGER.log(Level.SEVERE, "Error loading RecScreen.fxml: {0}", ex.getMessage());
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
