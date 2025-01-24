package tictactoe.client.online_game_board;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;
import sun.misc.REException;
import tictactoe.client.server_connection.Request;
import tictactoe.client.soundManager.SoundManager;
import tictactoe.client.RecScreen.RecScreenController;
import tictactoe.client.available_players.FXMLAvailablePlayersController;
import tictactoe.client.session_data.SessionData;
import tictactoe.client.scene_navigation.SceneNavigation;
import tictactoe.client.resultVideoScreen.ResultVideoScreenController;



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

    private String opponentName;

    private Button[][] board;

    private RecScreenController recScreenController;
    private ResultVideoScreenController resVideo;

    @FXML
    private ImageView logo;

    private String winnerPlayer;
    private boolean isGameOver;
    @FXML
    private GridPane boardPane;

    String symbol;

    public void setOpponentName(String opponetnName) {
        this.opponentName = opponetnName;
    }

    public void setMyTurn(Boolean firstTurn) {
        this.firstTurn = firstTurn;

        if (!firstTurn) {
            boardPane.setDisable(true);
        }
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
        new Thread(() -> {
            recieveRosponse();
        }).start();

    }

    @FXML
    public void handleButtonClick(ActionEvent event) {

        Button clickedButton = (Button) event.getSource();
        
            // Check if the button is already filled or game is over
        if (!clickedButton.getText().isEmpty() || isGameOver) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (clickedButton.equals(board[i][j])) {
                    System.out.println("Button clicked at position: (" + i + ", " + j + ")");
                    if (firstTurn) {
                        symbol = "X";
                        sendMoveToServer(symbol, i, j);
                        clickedButton.setText("X");
                        //firstTurn = false;
                    } else {
                        symbol = "O";
                        sendMoveToServer(symbol, i, j);
                        clickedButton.setText("O");
                        // firstTurn = true;
                    }
                    // System.out.println(""+Arrays.deepToString(gameBoard));
                    clickedButton.setDisable(true);
                    clickedButton.setStyle("-fx-opacity: 1.0;");
                    break;
                }
            }
        }
    }

    public void sendMoveToServer(String symbol, int row, int col) {   // technically send game board

        boardPane.setDisable(true);

        try {
            JSONObject json = new JSONObject();
            json.put("header", "move");
            json.put("opponent", opponentName);
            json.put("symbol", symbol);
            json.put("row", row);
            json.put("column", col);

            Request.getInstance().sendMove(json.toString());
            //dos.flush();
            System.out.println("Move sent to server: " + json.toString());
            // Disable record checkbox after move
            checkBoxRecord.setDisable(true);
            checkWhoIsTheWinner();
             

        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(FXMLOnlineGameBoardController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void recieveMoveFromServer(JSONObject json) {

        symbol = json.getString("symbol");
        int row = json.getInt("row");
        int col = json.getInt("column");
        
        // Check if the button is empty before setting text
        if (board[row][col].getText().isEmpty()) {
            board[row][col].setText(symbol);
            board[row][col].setDisable(true);
            board[row][col].setStyle("-fx-opacity: 1.0;");
            checkWhoIsTheWinner();
            boardPane.setDisable(false);
        }
    }
/*
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
                  // Only show video to winner
            if ((!firstTurn && winnerPlayer.equals("O")) || (firstTurn && winnerPlayer.equals("X"))) 
            {
                this.goToResultVideoScreen();
            }
           
           


            isGameOver = true;

            // goToResultVideoScreen();
        } else if (isBoardFull()) {

            showAlertAndReset();

        }
    }*/
 private void checkWhoIsTheWinner() 
{
        if (checkWin()) 
        {
            winnerPlayer = firstTurn ? "O" : "X";
            highlightWinnerButtons();

            // Get current player's username
            String currentPlayer = SessionData.getUsername();

            // Simple winner determination
            boolean isCurrentPlayerWinner = (firstTurn && symbol.equals("X")) || (!firstTurn && symbol.equals("O"));

            // Only show video to winner
            if (!isCurrentPlayerWinner) {  // Show video to winner
                System.out.println("Showing winner video to: " + currentPlayer);
                this.goToResultVideoScreen();
            } 
            else {  // Show alert to loser
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game Over");
                    alert.setHeaderText(null);
                    alert.setContentText("Game Over! You lost!");
                    alert.showAndWait();
                });
            }

            isGameOver = true;
        } 
        else if (isBoardFull()) {
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
        // Re-enable record checkbox for new game
        checkBoxRecord.setDisable(false);
        checkBoxRecord.setSelected(false);
        recScreenController = null;

        // isXTurn = true;
    }
    
    
    private void showAlertAndReset() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Draw Game");
            alert.setHeaderText(null);
            alert.setContentText("Do you want to Play Another Match?");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("alert-style.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("dialog-pane");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) 
            {
                SoundManager.playSoundEffect("click.wav");
                resetBoard();
            } 
            else 
            {
                SoundManager.playSoundEffect("click.wav");
                //backToMainScreen();       
            }
        });
    }

    private void recieveRosponse() {
        while (true) {
            try {
                JSONObject json = Request.getInstance().recieve();
                System.out.println(json);
                switch (json.getString("header")) {
                    case "move_res":
                        Platform.runLater(() -> recieveMoveFromServer(json));
                        break;
                    case "server_down":
                        Platform.runLater(() -> terminateOnlineGameBoardScreen());
                        break;
                }
            } catch (IOException ex) {
                System.out.println("finalize conniction");
                break;
            }
        }

    }

    @FXML
    private void handleCheckBox(ActionEvent event) {
        if (checkBoxRecord.isSelected()) {
            if (recScreenController == null) {
                recScreenController = new RecScreenController();
                recScreenController.initializeLogFile();
            }
            System.out.println("Recording is enabled");
        } else {
            recScreenController = null;
            System.out.println("Recording is diabled");
        }
    }

    private void terminateOnlineGameBoardScreen() {
        //show aleart the server is dowen
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Server Message");
        alert.setHeaderText("Server now is dowen!");
        alert.show();
        //close conniction with server
        try {
            Request.getInstance().disconnectToServer();
        } catch (IOException ex) {
            Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //redirect all users to main screen
        String mainScenePath = "/tictactoe/client/main_screen/FXMLMainScreen.fxml";
        try {
            SceneNavigation.getInstance().nextScene(mainScenePath, logo);
        } catch (IOException ex) {
            Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        SessionData.deleteDate();
    }
    
      public void goToResultVideoScreen() {
        System.out.println("Waiting for 2 seconds To Know Who is the Winner before going to Result Video Screen ");

        PauseTransition pause = new PauseTransition(Duration.seconds(2));

        pause.setOnFinished(event -> {
            try {
                System.out.println("GO To Result Video Screen");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tictactoe/client/resultVideoScreen/ResultVideoScreen.fxml"));
                Parent root = loader.load();

                ResultVideoScreenController controller = loader.getController();

                Platform.runLater(() -> {
                    try {
                        controller.setWinner(winnerPlayer);

                        Scene scene = new Scene(root);
                        Stage stage = (Stage) logo.getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();

                        SoundManager.pauseBackgroundMusic();

                        System.out.println("Winner " + winnerPlayer + " is passed to ResultVideoScreen: ");
                    } catch (Exception e) {
                        Logger.getLogger(ResultVideoScreenController.class.getName()).log(Level.SEVERE, null, e);
                    }
                });

            } catch (IOException ex) {
                Logger.getLogger(ResultVideoScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    pause.play();
}

}
