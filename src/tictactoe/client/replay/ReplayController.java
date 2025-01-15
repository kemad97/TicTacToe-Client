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
import tictactoe.client.soundManager.SoundManager;

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
                                         
                                        SoundManager.playSoundEffect("click.wav");

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
                Platform.runLater(this::highlightWinner);

            } catch (IOException e) {
                  Logger.getLogger(ReplayController.class.getName()).log(Level.SEVERE, null, e);
            }
        }).start();
    }

    private void highlightWinner() {
        String winner = getWinner();
        if (winner != null) {
            for (int i = 0; i < 3; i++) {
               
                if (board[i][0].getText().equals(winner) &&
                    board[i][1].getText().equals(winner) &&
                    board[i][2].getText().equals(winner)) {
                    highlightButtons(board[i][0], board[i][1], board[i][2]);
                    return;
                }

              
                if (board[0][i].getText().equals(winner) &&
                    board[1][i].getText().equals(winner) &&
                    board[2][i].getText().equals(winner)) {
                    highlightButtons(board[0][i], board[1][i], board[2][i]);
                    return;
                }
            }

           
            if (board[0][0].getText().equals(winner) &&
                board[1][1].getText().equals(winner) &&
                board[2][2].getText().equals(winner)) {
                highlightButtons(board[0][0], board[1][1], board[2][2]);
            } else if (board[0][2].getText().equals(winner) &&
                       board[1][1].getText().equals(winner) &&
                       board[2][0].getText().equals(winner)) {
                highlightButtons(board[0][2], board[1][1], board[2][0]);
            }
        }
    }

    private String getWinner() {
        for (int i = 0; i < 3; i++) {
           
            if (!board[i][0].getText().isEmpty() &&
                board[i][0].getText().equals(board[i][1].getText()) &&
                board[i][1].getText().equals(board[i][2].getText())) {
                return board[i][0].getText();
            }

        
            if (!board[0][i].getText().isEmpty() &&
                board[0][i].getText().equals(board[1][i].getText()) &&
                board[1][i].getText().equals(board[2][i].getText())) {
                return board[0][i].getText();
            }
        }

       
        if (!board[0][0].getText().isEmpty() &&
            board[0][0].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][2].getText())) {
            return board[0][0].getText();
        }
        if (!board[0][2].getText().isEmpty() &&
            board[0][2].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][0].getText())) {
            return board[0][2].getText();
        }

        return null;
    }

    private void highlightButtons(Button... buttons) {
        for (Button button : buttons) {
            button.setStyle("-fx-background-color: yellow; -fx-text-fill: black; -fx-font-weight: bold;");
        }
    }
    
    @FXML
    private void backToGameLogs(){
        
        try {
                    
            SoundManager.playSoundEffect("click.wav");
             
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
