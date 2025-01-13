/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.replay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tictactoe.client.main_screen.FXMLMainScreenController;

/**
 * FXML Controller class
 *
 * @author habib
 */
public class ReplayController implements Initializable {

    @FXML
    private Button Btn11, Btn12, Btn13, Btn21, Btn22, Btn23, Btn31, Btn32, Btn33;
    @FXML
    private ImageView backImgView;
    
    private Button[][] board;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        board = new Button[][]{
            {Btn11, Btn12, Btn13},
            {Btn21, Btn22, Btn23},
            {Btn31, Btn32, Btn33}
        };

    }

    public void replay(String filePath) {

        new Thread(() -> {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] token = line.split(",");
                    if (token.length == 2) {
                        String buttonId = token[0].trim();
                        String symbol = token[1].trim();

                        Platform.runLater(() -> {
                            for (Button[] row : board) {
                                for (Button button : row) {
                                    if (button.getId().equals(buttonId)) {
                                        button.setText(symbol);
                                        button.setStyle("-fx-text-fill: #242320; -fx-font-weight: bold;");
                                    }
                                }
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.err.println("Replay thread interrupted");
                            return;
                        }
                    }
                }
            } catch (IOException e) {
                Logger.getLogger(ReplayController.class.getName()).log(Level.SEVERE, null, e);
            }
        }).start();
    }
    
    @FXML
    private void backToGameLogs(){
        
        try {
             
            System.out.println("Back to game logs");
            
            Parent root = FXMLLoader.load(getClass().getResource("/tictactoe/client/RecScreen/RecScreen.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) Btn12.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
                    
        } catch (IOException ex) {
                    
            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
