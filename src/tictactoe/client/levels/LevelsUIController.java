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
public class LevelsUIController implements Initializable {

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
    private void setHardDiffculty(ActionEvent event) {
         selectedDifficulty="hard";
        System.out.println ("Selected diffculty :" + selectedDifficulty);
    }

    @FXML
    private void setMediumDiffculty(ActionEvent event) {
           selectedDifficulty="medium";
           System.out.println ("Selected diffculty :" + selectedDifficulty);
    }

    @FXML
    private void setEasyDiffculty(ActionEvent event) {
         selectedDifficulty="easy";
         System.out.println ("Selected diffculty :" + selectedDifficulty);
         
         
         //go to gameboard after choosing easy
             try {
            System.out.println("start offline match vs. PC");

            Parent root = FXMLLoader.load(getClass().getResource  ("/tictactoe/client/gameBoardWithFriend/GameBoardWithFriend.fxml"));        
            Scene scene = new Scene(root);

            Stage stage = (Stage) logo.getScene().getWindow();
            

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
  


    public String getDiffculty ()
    {
        return selectedDifficulty;
    }

    @FXML
    private void Levelstate(MouseEvent event) {
    }

   
}
