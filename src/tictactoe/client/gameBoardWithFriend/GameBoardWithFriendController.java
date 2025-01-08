package tictactoe.client.gameBoardWithFriend;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Delayed;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Platform;
import tictactoe.client.main_screen.FXMLMainScreenController;
import tictactoe.client.resultVideoScreen.ResultVideoScreenController;

public class GameBoardWithFriendController implements Initializable {

    @FXML
    private ImageView logo;

    @FXML
    private Button Btn11, Btn12, Btn13, Btn21, Btn22, Btn23, Btn31, Btn32, Btn33;

    private boolean isXTurn;
    private boolean isGameOver;
    private Button[][] board;
    private Button[] winningButtons;
    
    private String winnerPlayer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Animate logo
        ScaleTransition transition = new ScaleTransition();
        transition.setNode(logo);
        transition.setDuration(Duration.millis(1000));
        transition.setCycleCount(ScaleTransition.INDEFINITE);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setByX(0.5);
        transition.setByY(0.5);
        transition.setAutoReverse(true);
        transition.play();
        
        
        // *********** Game With Friend Logic ***************
        board = new Button[][]{
            {Btn11, Btn12, Btn13},
            {Btn21, Btn22, Btn23},
            {Btn31, Btn32, Btn33}
        };
        
        
        isGameOver = false;
        isXTurn = true;  // اللي هيلعب الأول دائما  X
        //winnerPlayer = "X";
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        
        if (isGameOver) {
            
            return; 
            
        }

        Button clickedButton = (Button) event.getSource();

        if (!clickedButton.getText().isEmpty()) {
            
            return; 
            
        }

        if (isXTurn) {
            
            clickedButton.setText("X");
            
            clickedButton.setStyle("-fx-text-fill: #242320; -fx-font-weight: bold;");
            
            System.out.println("Button " + clickedButton.getId() +" is Clicked With sympol X");
            
            isXTurn = false;
            
        } else {
            
            clickedButton.setText("O");
            
            clickedButton.setStyle("-fx-text-fill: #242320; -fx-font-weight: bold;");
            
            System.out.println("Button " + clickedButton.getId() +" is Clicked With sympol O");
            
            isXTurn = true;
        
        }

        checkWhoIsTheWinner();
        
    }

    private void checkWhoIsTheWinner() {
        
        if (checkWin()) {
            
           winnerPlayer = isXTurn ? "O" : "X"; 
            
           highlightWinnerButtons();
            
            isGameOver = true;
            
            goToResultVideoScreen();
            
            
        } else if (isBoardFull()) {
            
           showAlertAndReset();
            
        }
    }

    private boolean checkWin() {
        
        for (int i = 0; i < 3; i++) {
            
            //نتشك لو الشخص فائز عن طريق الصفوف 
            
            if (checkThreeButtonsEquality(board[i][0], board[i][1], board[i][2])) {
                
                winningButtons = new Button[]{board[i][0], board[i][1], board[i][2]};
                
                return true;
                
            }
            
            // نتشك لو الشخص فائز عن طريق الأعمده
            else if (checkThreeButtonsEquality(board[0][i], board[1][i], board[2][i])) {
                
                winningButtons = new Button[]{board[0][i], board[1][i], board[2][i]};
                
                return true;
            
            }
            
        }
        
        // نتشك لو الشخص فاز عن طريق القطر
        
        if (checkThreeButtonsEquality(board[0][0], board[1][1], board[2][2])) {
            
            winningButtons = new Button[]{board[0][0], board[1][1], board[2][2]};
            
            return true;
            
        } else if (checkThreeButtonsEquality(board[0][2], board[1][1], board[2][0])) {
            
            winningButtons = new Button[]{board[0][2], board[1][1], board[2][0]};
            
            return true;
            
        }
        
        return false;
        
    }

    private boolean checkThreeButtonsEquality(Button b1, Button b2, Button b3) {
        
        boolean isTheTreeButtonsAreEqual = false;
        
        if (b1.getText().isEmpty()) {
            
            isTheTreeButtonsAreEqual = false;
            
        } else if ( b1.getText().equals(b2.getText()) && b1.getText().equals(b3.getText()) ){
           
            isTheTreeButtonsAreEqual = true;
      
        }    
        
        return isTheTreeButtonsAreEqual;
        
    }

    private void highlightWinnerButtons() {
        
        for (Button button : winningButtons) {
            
            button.setStyle("-fx-background-color: yellow; -fx-border-color: green; -fx-font-weight: bold;");
            
        }
        
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
    
    
     private void resetBoard() {
        
        for (int i = 0; i < 3; i++) {
            
            for (int j = 0; j < 3; j++) {
                
                board[i][j].setText("");
                
            }
            
        }
        
        isGameOver = false;
        
        isXTurn = true;
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
                  
                System.out.println("Play another Match");
                
                resetBoard()  ;      
                        
            } else{
                        
                backToMainScreen();
                
            }   
                  
    }


    private void backToMainScreen(){
        
        try {
             
            System.out.println("Back To Main Screen");

            Parent root = FXMLLoader.load(getClass().getResource("/tictactoe/client/main_screen/FXMLMainScreen.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) logo.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
                    
        } catch (IOException ex) {
                    
            Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void goToResultVideoScreen() {
        
        System.out.println("Waiting for 2 seconds To Know Who is the Winner before going to Result Video Screen ");

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        
        pause.setOnFinished(event -> {
            
            try {
                System.out.println("GO To Result Video Screen");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tictactoe/client/resultVideoScreen/ResultVideoScreen.fxml"));
                Parent root = loader.load();

                
                ResultVideoScreenController controller = loader.getController();
                
                controller.setWinner(winnerPlayer);
            
                          
                Scene scene = new Scene(root);
                Stage stage = (Stage) logo.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
                
                System.out.println("Winner " + winnerPlayer + " is passed to ResultVideoScreen: " );

            } catch (IOException ex) {
                Logger.getLogger(FXMLMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        pause.play();
}


     
}



