/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.online_game_board;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.json.JSONObject;
import tictactoe.client.server_connection.Request;
import tictactoe.client.session_data.SessionData;
import tictactoe.client.soundManager.SoundManager;

/**
 * FXML Controller class
 *
 * @author musta
 */
public class FXMLOnlineGameBoardController implements Initializable {

    @FXML
    private Label opponnetUsername;
    
    @FXML 
    private Label currentPlayerUsername;
    
    @FXML 
    private ImageView myImageView;
    
    @FXML 
    private ImageView opponentImageView;
    
    @FXML
    private CheckBox checkBoxRecord;
    
    @FXML
    private Button Btn11, Btn12, Btn13, Btn21, Btn22, Btn23, Btn31, Btn32, Btn33;
    

    private Boolean firstTurn;
    
    //private Boolean opponentTurn;
    
    private String opponentName;
    
    private Button[][] board;
    
   
    
    
//    private String[][] gameBoard = {{"", "", ""},
//                                    {"", "", ""},
//                                    {"", "", ""}};
    
    
    private String winnerPlayer;
    private boolean isGameOver;
    @FXML
    private GridPane boardPane;
    @FXML
    private ImageView logo;
    
    String symbol;

    public void setOpponentName(String opponetnName) {
        this.opponentName = opponetnName;
    }

    
    public void setPlayerTurn(Boolean firstTurn) {
        this.firstTurn = firstTurn;
        
//        if (opponentTurn) {
//            opponnetUsername.setText(opponentName);
//        } else {
//            opponnetUsername.setText(opponentName);
//        }

         if(!firstTurn)
            boardPane.setDisable(true);
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        currentPlayerUsername.setText(SessionData.getUsername());
        opponnetUsername.setText(opponentName);
        
        board = new Button[3][3];
        board[0][0] = Btn11;
        board[0][1] = Btn12;
        board[0][2] = Btn13;
        board[1][0] = Btn21;
        board[1][1] = Btn22;
        board[1][2] = Btn23;
        board[2][0] = Btn31;
        board[2][1] = Btn32;
        board[2][2] = Btn33;
        
        winnerPlayer = "";
        isGameOver = false;
        symbol = "";
      
        
        /*
        Image xImage = new Image(getClass().getResource("/media/images/X.png").toExternalForm());
        Image oImage = new Image(getClass().getResource("/media/images/O.png").toExternalForm());
        
        if(myTurn){
            myImageView.setImage(xImage);
            opponentImageView.setImage(oImage);
        }
        
        else{
            myImageView.setImage(oImage);
            opponentImageView.setImage(xImage);
        }
        */
        
        new Thread(()->{
            System.out.println("hhhhhhhhhh");
            recieveRosponse();
        }).start();
        
       
    }
    
    @FXML
    public void handleButtonClick(ActionEvent event){
        
        Button clickedButton = (Button) event.getSource();
        
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(clickedButton.equals(board[i][j])){
                    System.out.println("Button clicked at position: (" + i + ", " + j + ")");
                    if(firstTurn){  
                        symbol = "X";
                        sendMoveToServer(symbol, i, j);
                        clickedButton.setText("X");
                        //firstTurn = false;
                    }
                    
                    else{
                        symbol = "O";
                        sendMoveToServer(symbol, i, j);
                        clickedButton.setText("O");
                       // firstTurn = true;
                    }
                   // System.out.println(""+Arrays.deepToString(gameBoard));
                    break;
                }
            }
        }
    }
    
    public void sendMoveToServer(String symbol, int row, int col){   // technically send game board
        
        boardPane.setDisable(true);
        
        try{
            JSONObject json = new JSONObject();
            json.put("header", "move");
            json.put("opponent", opponentName);
            json.put("symbol", symbol);
            json.put("row", row);
            json.put("column", col);

            Request.getInstance().sendMove(json.toString());
            //dos.flush();
            System.out.println("Move sent to server: " + json.toString());
            
            checkWhoIsTheWinner();
            
        }
        catch(Exception e){
            e.printStackTrace();
            Logger.getLogger(FXMLOnlineGameBoardController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void recieveMoveFromServer(JSONObject json){
 
        symbol = json.getString("symbol");
        int row = json.getInt("row");
        int col = json.getInt("column");
        board[row][col].setText(symbol);
        checkWhoIsTheWinner();
        boardPane.setDisable(false);
    }
    
    
    // check winner
     private void checkWhoIsTheWinner() {

        if (checkWin()) {

            winnerPlayer = firstTurn ? "O" : "X";
            
            if (winnerPlayer.equals("X")) {
                
               // xScore++;
                
            } else {
                
               // oScore++;
                
            }
   
            // updateScoreLabels();

            highlightWinnerButtons();

            isGameOver = true;

           // goToResultVideoScreen();

        } else if (isBoardFull()) {

            showAlertAndReset();

        }
    }

    private boolean checkWin() {

        for (int i = 0; i < 3; i++) {

            //نتشك لو الشخص فائز عن طريق الصفوف 
            if (checkThreeButtonsEquality(board[i][0], board[i][1], board[i][2])) {

              //  winningButtons = new Button[]{board[i][0], board[i][1], board[i][2]};

                return true;

            } // نتشك لو الشخص فائز عن طريق الأعمده
            else if (checkThreeButtonsEquality(board[0][i], board[1][i], board[2][i])) {

               // winningButtons = new Button[]{board[0][i], board[1][i], board[2][i]};

                return true;

            }

        }

        // نتشك لو الشخص فاز عن طريق القطر
        if (checkThreeButtonsEquality(board[0][0], board[1][1], board[2][2])) {

         //   winningButtons = new Button[]{board[0][0], board[1][1], board[2][2]};

            return true;

        } else if (checkThreeButtonsEquality(board[0][2], board[1][1], board[2][0])) {

         //   winningButtons = new Button[]{board[0][2], board[1][1], board[2][0]};

            return true;

        }

        return false;

    }

    private boolean checkThreeButtonsEquality(Button b1, Button b2, Button b3) {

        boolean isTheTreeButtonsAreEqual = false;

        if (b1.getText().isEmpty()) {

            isTheTreeButtonsAreEqual = false;

        } else if (b1.getText().equals(b2.getText()) && b1.getText().equals(b3.getText())) {

            isTheTreeButtonsAreEqual = true;

        }

        return isTheTreeButtonsAreEqual;

    }

    private void highlightWinnerButtons() {
        /*
        for (Button button : winningButtons) {

            button.setStyle("-fx-background-color: yellow; -fx-border-color: green; -fx-font-weight: bold;");

        }
        */
    }

    private boolean isBoardFull() {

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

                if (board[i][j].getText().isEmpty()) {

                    return false;

                }

            }

        }

        return true;

    }

    public void resetBoard() {

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {

                board[i][j].setText("");

            }

        }

        isGameOver = false;

       // isXTurn = true;
    }

    private void showAlertAndReset() {

        Alert aboutAlert = new Alert(Alert.AlertType.CONFIRMATION);

        aboutAlert.setTitle("No one won this match.");

        aboutAlert.setHeaderText(null);

        aboutAlert.setGraphic(null);

        aboutAlert.setContentText("Do you want to Play Another Mathch ?");

        aboutAlert.getDialogPane().getStylesheets().add(getClass().getResource("alert-style.css").toExternalForm());

        aboutAlert.getDialogPane().getStyleClass().add("dialog-pane");

        Optional<ButtonType> result = aboutAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            
             
            SoundManager.playSoundEffect("click.wav");

            System.out.println("Play another Match");

            resetBoard();

        } else {
            
             
            SoundManager.playSoundEffect("click.wav");

            //backToMainScreen();

        }

    }

    private void recieveRosponse() {
        System.out.println("kkkkkkkkkk");
        while(true){
            
            System.out.println("lllllllllllllllll");
            try {
                JSONObject json = Request.getInstance().recieve();
                System.out.println(json);
                switch(json.getString("header")){
                    
                    case "move_res":
                        Platform.runLater(()->recieveMoveFromServer(json));
                        break;
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(FXMLOnlineGameBoardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
   

}
