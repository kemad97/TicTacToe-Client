package tictactoe.client.gameBoard;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
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
import tictactoe.client.main_screen.FXMLMainScreenController;

public class GameBoardController implements Initializable {

    @FXML
    private ImageView logo;
    @FXML
    private Button Btn11, Btn12, Btn13, Btn21, Btn22, Btn23, Btn31, Btn32, Btn33;
    
    private boolean isXTurn;
    private boolean isGameOver;
    private Button[][] board;
    private Button[] winningButtons;
    private String difficulty;
    
    public void setDifficulty(String difficulty){

        this.difficulty = difficulty;
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Animate logo
        ScaleTransition transition = new ScaleTransition();
        transition.setNode(logo);
        transition.setDuration(Duration.millis(1000));
        transition.setCycleCount(ScaleTransition.INDEFINITE);
        transition.setByX(0.5);
        transition.setByY(0.5);
        transition.setAutoReverse(true);
        transition.play();

        // Initialize the board
        board = new Button[][]{
                {Btn11, Btn12, Btn13},
                {Btn21, Btn22, Btn23},
                {Btn31, Btn32, Btn33}
        };

        isGameOver = false;
        isXTurn = true;

        difficulty = "Medium"; // test
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (isGameOver) return;

        Button clickedButton = (Button) event.getSource();
        if (!clickedButton.getText().isEmpty()) return;

        if (isXTurn) {
            clickedButton.setText("X");
            clickedButton.setStyle("-fx-text-fill: #242320; -fx-font-weight: bold;");
            isXTurn = false;

            checkWhoIsTheWinner();

            if (!isGameOver) {
                computerMove(); 
            }
        }
    }

    private void computerMove() {
        int[] bestMove = null;

        switch (difficulty) {
            case "Easy":
                bestMove = getRandomMove();
                break;
            case "Medium":
               bestMove = findBestMove(3); // Limited depth 
                break;
            case "Hard":
                bestMove = findBestMove(Integer.MAX_VALUE); // Full depth
                break;
        }

        if (bestMove != null) {
            int row = bestMove[0];
            int col = bestMove[1];
            board[row][col].setText("O");
            board[row][col].setStyle("-fx-text-fill: #242320; -fx-font-weight: bold;");
            isXTurn = true;

            checkWhoIsTheWinner();
        }
    }

    private int[] getRandomMove() {
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].getText().isEmpty()) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (!emptyCells.isEmpty()) {
            return emptyCells.get(new Random().nextInt(emptyCells.size()));
        }
        return null;
    }

    private int[] findBestMove(int depthLimit) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].getText().isEmpty()) {
                    board[i][j].setText("O");
                    int score = minimax(false, 0, depthLimit);
                    board[i][j].setText("");

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{i, j};
                    }
                }
            }
        }

        return bestMove;
    }

    private int minimax(boolean isMaximizing, int depth, int depthLimit) {
        if (checkWin("O")) return 10 - depth;
        if (checkWin("X")) return depth - 10;
        if (isBoardFull() || depth >= depthLimit) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].getText().isEmpty()) {
                        board[i][j].setText("O");
                        int score = minimax(false, depth + 1, depthLimit);
                        board[i][j].setText("");
                        bestScore = Math.max(bestScore, score);
                    }
                }
            }

            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].getText().isEmpty()) {
                        board[i][j].setText("X");
                        int score = minimax(true, depth + 1, depthLimit);
                        board[i][j].setText("");
                        bestScore = Math.min(bestScore, score);
                    }
                }
            }

            return bestScore;
        }
    }

    private boolean checkWin(String symbol) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0].getText().equals(symbol) &&
                    board[i][1].getText().equals(symbol) &&
                    board[i][2].getText().equals(symbol)){
                
                winningButtons = new Button[]{board[i][0], board[i][1], board[i][2]};
                return true;
            }

            if (board[0][i].getText().equals(symbol) &&
                    board[1][i].getText().equals(symbol) &&
                    board[2][i].getText().equals(symbol)){
                
                winningButtons = new Button[]{board[0][i], board[1][i], board[2][i]};
                return true;
            }
        }

        if (board[0][0].getText().equals(symbol) &&
                board[1][1].getText().equals(symbol) &&
                board[2][2].getText().equals(symbol)){
            
            winningButtons = new Button[]{board[0][0], board[1][1], board[2][2]};
            return true;
        }

        if (board[0][2].getText().equals(symbol) &&
                board[1][1].getText().equals(symbol) &&
                board[2][0].getText().equals(symbol)){
            
            winningButtons = new Button[]{board[0][2], board[1][1], board[2][0]};
            return true;
        }

        return false;
    }

    private void checkWhoIsTheWinner() {
        if (checkWin("X")) {
            highlightWinnerButtons();
            isGameOver = true;
        } else if (checkWin("O")) {
            highlightWinnerButtons();
            isGameOver = true;
        } else if (isBoardFull()) {
            System.out.println("Board is full, no winner. Showing alert.");
            showAlertAndReset();
            isGameOver = true;
        }
    }
    
    
    private void highlightWinnerButtons() {
 
        for (Button button : winningButtons) {
            
            button.setStyle("-fx-background-color: yellow; -fx-border-color: green; -fx-font-weight: bold;");
            
        }
        
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
    
    private void showAlertAndReset() {
        
        Platform.runLater(()->{
             Alert aboutAlert = new Alert(Alert.AlertType.CONFIRMATION);
                   
            aboutAlert.setTitle("No one won this match.");

            aboutAlert.setHeaderText(null);

            aboutAlert.setGraphic(null);

            aboutAlert.setContentText("Do you want to Play Another Match ?");

            aboutAlert.getDialogPane().getStylesheets().add(getClass().getResource("/tictactoe/client/gameBoardWithFriend/alert-style.css").toExternalForm());

            aboutAlert.getDialogPane().getStyleClass().add("dialog-pane");

            Optional<ButtonType> result = aboutAlert.showAndWait();


            if (result.isPresent() && result.get() == ButtonType.OK) {

                    System.out.println("Play another Match");

                    resetBoard();      

                } else{

                    goBackToMainScreen();

                }   
        });
       
                  
    }


    
    private void goBackToMainScreen(){
        
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
    

}
