package tictactoe.client.onlineGameBoard;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import org.json.JSONObject;
import tictactoe.client.session_data.SessionData;

public class OnlineGameBoardController implements Initializable {
    
    @FXML
    private CheckBox checkBoxRecord;
    
    @FXML
    private Button Btn11, Btn12, Btn13, Btn21, Btn22, Btn23, Btn31, Btn32, Btn33;
    
    private Button[][] board;
    
    private DataInputStream dis; //input stream
    
    private DataOutputStream dos; //output stream
    
    
     //localhost:1527
    private final String SERVER_HOST = "localhost";
    private final int SERVER_PORT = 1527;
    
    private Socket socket;
    private String currentPlayerUsername = SessionData.getUsername();
    
    //private Player player1, player2;
    
    private final String opponnetUsername;
    private String[][] gameBoard = {{"", "", ""},
                                    {"", "", ""},
                                    {"", "", ""}};
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
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
            Logger.getLogger(OnlineGameBoardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    
    public OnlineGameBoardController(String opponnetUsername){
    
        this.opponnetUsername = opponnetUsername;        
        
    }
    
    public void handleButtonClick(ActionEvent event){
        
        Button clickedButton = (Button) event.getSource();
        
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(clickedButton.equals(board[i][j])){
                    System.out.println("Button clicked at position: (" + i + ", " + j + ")");
                    gameBoard[i][j] = "X";
                    System.out.println(gameBoard.toString());
                    break;
                }
            }
        }
    }
    
    public void sendMoveToServer(){
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
            Logger.getLogger(OnlineGameBoardController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
