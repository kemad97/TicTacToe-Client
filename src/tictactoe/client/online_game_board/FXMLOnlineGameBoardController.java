package tictactoe.client.online_game_board;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;
import tictactoe.client.server_connection.Request;
import tictactoe.client.soundManager.SoundManager;
import tictactoe.client.RecScreen.RecScreenController;
import tictactoe.client.animation.Animation;
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
        SessionData.setOpponentName(opponentName);
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
        //animate logo
        Animation.scaleAnimation(logo, ScaleTransition.INDEFINITE, 0.5);

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

            Request.getInstance().sendMove(json.toString());
            //dos.flush();
            System.out.println("Move sent to server: " + json.toString());

            // Disable record checkbox after move
            checkBoxRecord.setDisable(true);
            checkWhoIsTheWinner();

        } catch (Exception e) {
            Logger.getLogger(FXMLOnlineGameBoardController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void recieveMoveFromServer(JSONObject json) {

        checkBoxRecord.setDisable(true);

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
        checkWhoIsTheWinner();
    }

    private void checkWhoIsTheWinner() {
        if (checkWin()) {
            winnerPlayer = firstTurn ? "X" : "O";
            highlightWinnerButtons();

            // Get current player's username
            String currentPlayer = SessionData.getUsername();

            // Simple winner determination
            boolean isCurrentPlayerWinner = (firstTurn && symbol.equals("X")) || (!firstTurn && symbol.equals("O"));

            if (isCurrentPlayerWinner) {
                try {
                    // update winner score
                    JSONObject json = new JSONObject();
                    json.put("header", "update_score");
                    json.put("winner", currentPlayer);
                    Request.getInstance().notifyServerOfWinner(json.toString());
                    System.out.println("send update score request to server");
                } catch (IOException ex) {
                    Logger.getLogger(FXMLOnlineGameBoardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            isGameOver = true;
            this.goToResultVideoScreen(isCurrentPlayerWinner);
        } else if (isBoardFull()) {
            showAlertAndBackToAvailableScreen();
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

    private void highlightLoserButtons() {

        for (Button button : winningButtons) {
            button.setStyle("-fx-background-color: red; -fx-border-color: green; -fx-font-weight: bold;");
        }

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

    private void showAlertAndBackToAvailableScreen() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Draw Game");
        alert.setHeaderText(null);
        alert.setContentText("You draw with your opponent!");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("alert-style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");

        alert.showAndWait();
        backToAvailableScreen();
    }

    private void backToAvailableScreen() {
        try {
            Request.getInstance().endPlayerGame();
            SceneNavigation.getInstance().nextScene("/tictactoe/client/available_players/FXMLAvailablePlayers.fxml", logo);
            SoundManager.pauseBackgroundMusic();
        } catch (IOException ex) {
            Logger.getLogger(FXMLOnlineGameBoardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    private void recieveRosponse() {
        while (!isGameOver) {
            try {
                JSONObject json = Request.getInstance().recieve();

                switch (json.getString("header")) {
                    case "move_res":
                        Platform.runLater(() -> recieveMoveFromServer(json));
                        break;
                    case "server_down":
                        Platform.runLater(() -> terminateOnlineGameBoardScreen());
                        break;
                    case "end_of_game":
                        isGameOver = true;
                        SessionData.setOpponentName("");
                        break;
                    case "opponent_exit_match":
                        System.out.println(json);
                        Platform.runLater(() -> opponentExitMatch());
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

    public void goToResultVideoScreen(boolean isWinner) {
        System.out.println("Waiting for 2 seconds To Know Who is the Winner before going to Result Video Screen ");

        // Store the stage reference using boardPane instead of logo
        Stage currentStage = (Stage) boardPane.getScene().getWindow();

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            try {
                System.out.println("GO To Result Video Screen");

                SceneNavigation.getInstance().gotoVideoScreen(logo, isWinner, opponentName);
                Request.getInstance().endPlayerGame();
                SoundManager.pauseBackgroundMusic();
                sendRequestToUpdateMatche_NO();

            } catch (IOException ex) {
                Logger.getLogger(ResultVideoScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        pause.play();
    }

    private void opponentExitMatch() {
        //show aleart the server is down
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Server Message");
        alert.setHeaderText(opponentName + " go out!");
        alert.show();
        backToAvailableScreen();
    }
    
    public void sendRequestToUpdateMatche_NO() {
        try {
            JSONObject json = new JSONObject();
            json.put("header", "update_matches_NO");
            json.put("username", SessionData.getUsername());

            Request.getInstance().sendMove(json.toString());
            System.out.println("Send request to server to update matche_no for each user: " + json.toString());
        } catch (Exception e) {
            Logger.getLogger(FXMLOnlineGameBoardController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
