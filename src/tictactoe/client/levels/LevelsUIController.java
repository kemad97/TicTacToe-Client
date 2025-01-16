/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.levels;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tictactoe.client.main_screen.FXMLMainScreenController;
import tictactoe.client.gameBoard.GameBoardController;
 

/**
 * FXML Controller class
 *
 * @author Kerolos
 */
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import tictactoe.client.soundManager.SoundManager;
public class LevelsUIController implements Initializable {

    
    @FXML
    private ImageView btnBack;
    @FXML
    private Button hardBtn;
    @FXML
    private Button mediumBtn;
    @FXML
    private Button easyBtn;
    
   

    
    public  String selectedDifficulty;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

  
    @FXML
    private void setHardDiffculty(ActionEvent event) 
    {
         
        SoundManager.playSoundEffect("click.wav");

        selectedDifficulty="Hard";
        System.out.println ("Selected diffculty :" + selectedDifficulty);
       

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tictactoe/client/gameBoard/GameBoard.fxml"));
        loadFXMLFile(loader,event);

    }

    @FXML
    private void setMediumDiffculty(ActionEvent event) {
         
           SoundManager.playSoundEffect("click.wav");

           selectedDifficulty="Medium";
           System.out.println ("Selected diffculty :" + selectedDifficulty);
           
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/tictactoe/client/gameBoard/GameBoard.fxml"));
           loadFXMLFile(loader,event);

    }

    @FXML
    private void setEasyDiffculty(ActionEvent event) 
    {
         
        SoundManager.playSoundEffect("click.wav");

        selectedDifficulty = "Easy";
        System.out.println("Selected difficulty: " + selectedDifficulty);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tictactoe/client/gameBoard/GameBoard.fxml"));
        loadFXMLFile(loader,event);
      
    }

  


    public String getDiffculty ()
    {
        return selectedDifficulty;
    }

    
    private void loadFXMLFile(FXMLLoader loader , ActionEvent event)
    {
          try {
            Parent root = loader.load(); // Load the FXML file first

            // Now you can safely get the controller
            GameBoardController controller = loader.getController();
            controller.setDifficulty(selectedDifficulty);

            System.out.println("Start offline match vs. PC");

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(LevelsUIController.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void backToMainScreen() {

        try {

            System.out.println("Back To Main Screen");

            Parent root = FXMLLoader.load(getClass().getResource("/tictactoe/client/main_screen/FXMLMainScreen.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {

            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

   

   
}
