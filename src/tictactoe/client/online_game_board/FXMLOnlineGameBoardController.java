/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.online_game_board;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
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

    private Boolean isMyTurnToPlay;
    private String opponentName;

    public void setOpponentName(String opponetnName) {
        this.opponentName = opponetnName;
    }

    public void serOpponentTurn(Boolean playerTurn) {
        this.isMyTurnToPlay = playerTurn;
        
        if (isMyTurnToPlay) {
            opponent_name.setText("Play with " + opponentName + " and this is my turn.");
        } else {
            opponent_name.setText("Play with " + opponentName + " and it is not my turn.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
