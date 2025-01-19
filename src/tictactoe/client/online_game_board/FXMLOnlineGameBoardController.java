/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.online_game_board;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author musta
 */
public class FXMLOnlineGameBoardController implements Initializable {

    @FXML
    private Label opponent_name;

    
    public void setOpponentName(String opponetnName){
        this.opponent_name.setText(opponetnName);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
