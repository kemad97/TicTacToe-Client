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
import javafx.scene.image.Image;
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

    Button[] winningButtons;

    public void setOpponentName(String opponetnName) {
        this.opponentName = opponetnName;
        opponnetUsername.setText(opponetnName);
    }

    public void setMyTurn(Boolean firstTurn) {
        this.firstTurn = firstTurn;

        Image xImage = new Image(getClass().getResource("/media/images/X.png").toExternalForm());
        Image oImage = new Image(getClass().getResource("/media/images/O.png").toExternalForm());

        if (firstTurn) {
            myImageView.setImage(xImage);
            opponentImageView.setImage(oImage);
        } else {
            myImageView.setImage(oImage);
            opponentImageView.setImage(xImage);
        }

        if (!firstTurn) {
            boardPane.setDisable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        currentPlayerUsername.setText(SessionData.getUsername());
        //opponnetUsername.setText(opponentName);

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
                    symbol = firstTurn ? "X" : "O";

                    // Update the button with the player's symbol
                    clickedButton.setText(symbol);
                    clickedButton.setDisable(true);
                    clickedButton.setStyle("-fx-opacity: 1.0;");

                    checkWhoIsTheWinner();
                    // Send the move to the server
                    sendMoveToServer(symbol, i, j);

                   
                    break;
                }
            }
        }
         // Log button click only if recording is enabled
        if (recScreenController != null) {
            recScreenController.logButtonClick(clickedButton.getId(), clickedButton.getText());
        } else {
            System.out.println("Recording is disabled. Button click not logged.");
        }
    }

    public void sendMoveToServer(String symbol, int row, int col) {

        boardPane.setDisable(true);

        try {
            JSONObject json = new JSONObject();
            json.put("header", "move");
            json.put("opponent", opponentName);
            json.put("symbol", symbol);
            json.put("row", row);
            json.put("column", col);

            //checkWhoIsTheWinner();
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
            boardPane.setDisable(false);
        }
        
        // Log button click only if recording is enabled
        if (recScreenController != null) {
            
            String buttonId = "Btn" + (row + 1) + (col + 1);
            recScreenController.logButtonClick(buttonId, symbol);
        }
    }

    private void checkWhoIsTheWinner() {

        if (checkWin()) {

            winnerPlayer = firstTurn ? "X" : "O";

            if (winnerPlayer.equals("X")) {

                // xScore++;
            } else {

                // oScore++;
            }

            // updateScoreLabels();

            Platform.runLater(()->highlightWinnerButtons());
           

            // Only show video to winner
            if ((!firstTurn && winnerPlayer.equals("O")) || (firstTurn && winnerPlayer.equals("X"))) {
                try {
                    // update winner score
                    JSONObject json = new JSONObject();
                    json.put("header", "update_score");
                    Request.getInstance().notifyServerOfWinner(json.toString());
                    System.out.println("send update score request to server");
                } catch (IOException ex) {
                    Logger.getLogger(FXMLOnlineGameBoardController.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.goToResultVideoScreen();
            }

            highlightWinnerButtons();
                  // Only show video to winner
            if ((!firstTurn && winnerPlayer.equals("O")) || (firstTurn && winnerPlayer.equals("X"))) 
            {
                this.goToResultVideoScreen();
            }
           
           



            isGameOver = true;

            goToResultVideoScreen();
        } else if (isBoardFull()) {

            showAlertAndReset();

        }
    }

    private boolean checkWin() {

        for (int i = 0; i < 3; i++) {

            //check rows
            if (checkThreeButtonsEquality(board[i][0], board[i][1], board[i][2])) {

                winningButtons = new Button[]{board[i][0], board[i][1], board[i][2]};
                return true;

            } // check columns
            else if (checkThreeButtonsEquality(board[0][i], board[1][i], board[2][i])) {

                winningButtons = new Button[]{board[0][i], board[1][i], board[2][i]};
                return true;

            }

        }

        // check diagonals
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

        } else if (b1.getText().equals(b2.getText()) && b1.getText().equals(b3.getText())) {

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
            if (result.isPresent() && result.get() == ButtonType.OK) {
                SoundManager.playSoundEffect("click.wav");
                resetBoard();
            } else {
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
        //show aleart the server is down
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Server Message");
        alert.setHeaderText("Server now is down!");
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
