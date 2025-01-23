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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONObject;
import tictactoe.client.session_data.SessionData;

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

    private Boolean myTurn;
    
    private Boolean opponentTurn;
    
    private String opponentName;
    
    private Button[][] board;
    
    private DataInputStream dis; //input stream
    
    private DataOutputStream dos; //output stream
    
    
     //localhost:1527
    private final String SERVER_HOST = "localhost";
    private final int SERVER_PORT = 1527;
    
    private Socket socket;
    
    private String[][] gameBoard = {{"", "", ""},
                                    {"", "", ""},
                                    {"", "", ""}};
    
    

    public void setOpponentName(String opponetnName) {
        this.opponentName = opponetnName;
    }
  
    public void setMyTurn(Boolean playerTurn) {
        this.isMyTurnToPlay = playerTurn;
        
//        if (opponentTurn) {
//            opponnetUsername.setText(opponentName);
//        } else {
//            opponnetUsername.setText(opponentName);
//        }
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
      
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(FXMLOnlineGameBoardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
    }
    
    public void handleButtonClick(ActionEvent event){
        
        Button clickedButton = (Button) event.getSource();
        
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(clickedButton.equals(board[i][j])){
                    System.out.println("Button clicked at position: (" + i + ", " + j + ")");
                    if(myTurn){
                        
                        gameBoard[i][j] = "X";
                        sendMoveToServer();
                        clickedButton.setText("X");
                    }
                    
                    else{
                        gameBoard[i][j] = "O";
                        sendMoveToServer();
                        clickedButton.setText("O");
                    }
                    System.out.println(""+Arrays.deepToString(gameBoard));
                    break;
                }
            }
        }
    }
    
    public void sendMoveToServer(){   // technically send game board
        try{
            JSONObject json = new JSONObject();
            json.put("header", "move");
            json.put("opponnet", opponnetUsername);
            json.put("board", gameBoard);

            dos.writeUTF(json.toString());
            dos.flush();
            System.out.println("Move sent to server: " + json.toString());
        }
        catch(Exception e){
            e.printStackTrace();
            Logger.getLogger(FXMLOnlineGameBoardController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void recieveMoveFromServer(JSONObject json){
        
        //gameBoard = json.getString("game_board");
    }

}
