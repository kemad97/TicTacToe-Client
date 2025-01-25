package tictactoe.client.available_players;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import tictactoe.client.animation.Animation;
import tictactoe.client.scene_navigation.SceneNavigation;
import tictactoe.client.session_data.SessionData;
import tictactoe.client.server_connection.Request;
import tictactoe.client.soundManager.SoundManager;

public class FXMLAvailablePlayersController implements Initializable {

    @FXML
    private Label username;
    @FXML
    private Label score;
    @FXML
    private ImageView logo;

    @FXML
    private ListView<String> availablePlayersList;

    private boolean isReceiving;

    private Thread receivingThread;

    @FXML
    private ImageView loading_img;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //animate logo
        Animation.scaleAnimation(logo, ScaleTransition.INDEFINITE, 0.5);

        loading_img.setVisible(false);

        requestAvailablePlayers();

        username.setText(SessionData.getUsername());
        score.setText(SessionData.getScore() + "");

        availablePlayersList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                SoundManager.playSoundEffect("click.wav");
                String selectedPlayer = availablePlayersList.getSelectionModel().getSelectedItem();
                if (selectedPlayer != null) {
                    String opponentUsername = selectedPlayer.split(" - ")[0].trim();
                    sendMatchRequest(opponentUsername);
                }
            }
        });

        //this thread list for any request coms to available players
        receivingThread = new Thread(() -> {
            isReceiving = true;
            receiveRequests();
        });
        receivingThread.start();
    }

    @FXML
    private void logout(MouseEvent event) {

        SoundManager.playSoundEffect("click.wav");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Do you want to logout!");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/commonStyle/alert-style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            try {
                SoundManager.playSoundEffect("click.wav");
                //cloase connection with server
                Request.getInstance().disconnectToServer();
            } catch (IOException ex) {
                System.out.println("server is dowen!");
            }

            //update session data
            SessionData.setAuthenticated(false);
            SessionData.setUsername(null);

            String mainScreenPath = "/tictactoe/client/main_screen/FXMLMainScreen.fxml";
            //goto main screen
            Parent root;
            try {
                SceneNavigation.getInstance().nextScene(mainScreenPath, score);
            } catch (IOException ex) {
                Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void requestAvailablePlayers() {
        JSONObject request = new JSONObject();

        request.put("header", "get_available_players");

        try {
            Request.getInstance().sendRequest(request.toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void receiveRequests() {
        while (isReceiving) {
            try {
                JSONObject jsonObject = Request.getInstance().recieve();

                switch (jsonObject.getString("header")) {
                    case "available_players":
                        Platform.runLater(() -> updateAvailablePlayersListView(jsonObject.getJSONArray("players")));
                        break;
                    case "match_request":
                        Platform.runLater(() -> showMatchRequestAlert(jsonObject.getString("fromPlayer")));
                        break;
                    case "request_decline":
                        Platform.runLater(() -> showDeclineMessage(jsonObject.getString("opponent")));
                        break;
                    case "start_game":
                        isReceiving = false;
                        Platform.runLater(() -> {
                            String onlineGameBoardPath = "/tictactoe/client/online_game_board/FXMLOnlineGameBoard.fxml";
                            try {
                                SceneNavigation.getInstance().gotoOnlineBoard(logo, jsonObject.getString("opponent"), jsonObject.getBoolean("yourTurn"));
                            } catch (IOException ex) {
                                Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        break;
                    case "your_state_not_available":
                        isReceiving = false;
                        break;

                    case "server_down":
                        Platform.runLater(() -> terminateAvailablePlayersScreen());
                        break;
                }
            } catch (IOException ex) {
                System.out.println("Server dowen!");
                break;
            }
        }
        System.out.println("finalize receving thread");
    }

    private void updateAvailablePlayersListView(JSONArray players) {

        availablePlayersList.getItems().clear();
        String currentUser = SessionData.getUsername();

        for (int i = 0; i < players.length(); i++) {
            JSONObject player = players.getJSONObject(i);

            if (!player.getString("username").equals(currentUser)) {
                String display = " " + player.get("username") + " - Score: " + player.get("score");
                availablePlayersList.getItems().add(display);
            } else {
                if (SessionData.getScore() != Integer.parseInt(player.getString("score"))) {
                    SessionData.setScore(Integer.parseInt(player.getString("score")));
                    //update user score
                    Platform.runLater(() -> {
                        score.setText(SessionData.getScore() + "");
                    });
                }
            }

        }
    }

    // Method to send a match request to the selected player
    private void sendMatchRequest(String opponentUsername) {
        try {
            Request.getInstance().sendMatchRequest(opponentUsername);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Match request sent to " + opponentUsername);
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/commonStyle/alert-style.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("dialog-pane");
            alert.show();

            //start wating screen
            availablePlayersList.setDisable(true);
            loading_img.setVisible(true);

        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to send match request.");
            alert.show();
            Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Show alert when a match request is received
    private void showMatchRequestAlert(String opponentUsername) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Match Request");
        alert.setHeaderText(opponentUsername + " has sent you a match request.");
        alert.setContentText("Do you want to accept the match?");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/commonStyle/alert-style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");

        //sleep for 10 sec then cancel this request
        new Thread(() -> {
            try {
                Thread.sleep(10000);
                if (alert.isShowing()) {
                    System.out.println("alert is showing");
                    Platform.runLater(() -> {
                        alert.close();
                    });
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SoundManager.playSoundEffect("click.wav");
            // Accept the match
            sendMatchResponse(opponentUsername, true);
            //goto online game board
        } else {
            // Decline the match
            SoundManager.playSoundEffect("click.wav");
            sendMatchResponse(opponentUsername, false);

        }
    }
    // Method to send a response to the match request (accept/decline)

    private void sendMatchResponse(String opponentUsername, boolean isAccepted) {
        try {
            Request.getInstance().sendMatchResponse(opponentUsername, isAccepted);
        } catch (IOException ex) {
            Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showDeclineMessage(String opponentName) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Match Responce");
        alert.setHeaderText(opponentName + " refuse your request.");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/commonStyle/alert-style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");
        alert.show();

        //stop wating screen
        availablePlayersList.setDisable(false);
        loading_img.setVisible(false);

    }

    private void terminateAvailablePlayersScreen() {
        //show aleart the server is dowen
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Server Message");
        alert.setHeaderText("Server now is dowen!");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/commonStyle/alert-style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");
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
            SceneNavigation.getInstance().nextScene(mainScenePath, score);
        } catch (IOException ex) {
            Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        SessionData.deleteDate();
    }

    @FXML
    private void goToUserProfile() {
        
        SoundManager.playSoundEffect("click.wav");
        try {
            //send to server i will be not available
            Request.getInstance().askServerToMakeMeNotAvailable();
        } catch (IOException ex) {
            Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
        }

        String userProfilePath = "/tictactoe/client/userProfile/FXMLUserProfile.fxml";
        try {
            SceneNavigation.getInstance().nextScene(userProfilePath, logo);
        } catch (IOException ex) {
            Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
