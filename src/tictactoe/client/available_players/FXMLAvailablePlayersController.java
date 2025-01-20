package tictactoe.client.available_players;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tictactoe.client.animation.Animation;
import tictactoe.client.scene_navigation.SceneNavigation;
import tictactoe.client.session_data.SessionData;
import tictactoe.client.server_connection.Request;

public class FXMLAvailablePlayersController implements Initializable {

    @FXML
    private Label username;
    @FXML
    private Label score;
    @FXML
    private ImageView logo;

    @FXML
    private ListView<String> availablePlayersList;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //animate logo
        Animation.scaleAnimation(logo, ScaleTransition.INDEFINITE, 0.5);

        username.setText(SessionData.getUsername());
        score.setText(SessionData.getScore() + "");

        showAvailablePlayers();
        // Start polling for available players and incoming match requests
        startPolling();

        availablePlayersList.setOnMouseClicked(event -> 
        {
            if(event.getClickCount ()==2)
            {
                String selectedPlayer = availablePlayersList.getSelectionModel().getSelectedItem();
                if (selectedPlayer != null) 
                {
                    String opponentUsername = selectedPlayer.split(" - ")[0].trim();
                     sendMatchRequest(opponentUsername);
                    
                }
                
        
            }

        

        });

        //this thread list for any request coms to available players
        new Thread(() -> {
            receiveRequests();
        }).start();
    }

    @FXML
    private void logout(MouseEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Do you want to logout!");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            try {

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
    
    private List<Map<String, String>> receiveAvailablePlayers() {

        List<Map<String, String>> players = new ArrayList<>();

        try {
            
            JSONObject request = new JSONObject();
            request.put("header", "get_available_players");

            Request.getInstance().sendRequest(request.toString());
        } catch (IOException ex) {

            Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);

        }

        return players;   
    }


    private List<Map<String, String>> handleResponse(String response) {
        
        List<Map<String, String>> players = new ArrayList<>();
        
        try {
            
            JSONObject jsonResponse = new JSONObject(response);

            if ("available_players".equals(jsonResponse.getString("header"))) {
                
                JSONArray jsonArray = jsonResponse.getJSONArray("players");

                String currentUser = SessionData.getUsername();

                for (int i = 0; i < jsonArray.length(); i++) {
                    
                    JSONObject player = jsonArray.getJSONObject(i);
                    
                    String username = player.getString("username");

                    if (!username.equals(currentUser)) {
                        
                        Map<String, String> playerMap = new HashMap<>();
                        
                        playerMap.put("username", username);
                        
                        playerMap.put("score", player.getString("score"));
                        
                        players.add(playerMap);
                    }
                }
            }
        } catch (JSONException e) {
            
            e.printStackTrace();
            
        }
        return players;
    }

    private void showAvailablePlayers() {
        JSONObject request = new JSONObject();

        request.put("header", "get_available_players");

        try {
            Request.getInstance().sendRequest(request.toString());
        } catch (IOException ex) {
            Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    // Method to send a match request to the selected player
    private void sendMatchRequest(String opponentUsername) {
        try {
            Request.getInstance().sendMatchRequest(opponentUsername);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Match request sent to " + opponentUsername);
            alert.show();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to send match request.");
            alert.show();
            Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     // Polling mechanism for available players and match requests
    private void startPolling() {
        pollingTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            showAvailablePlayers();
            checkForMatchRequest();  // Check for incoming match requests
        }));
        pollingTimeline.setCycleCount(Timeline.INDEFINITE);
        pollingTimeline.play(); 
    }

      // Stop polling when the scene is closed or the player logs out
    private void stopPolling() {
        if (pollingTimeline != null) {
            pollingTimeline.stop();
        }
    }
    
      // Stop polling when the controller is destroyed or the scene is no longer needed
    @Override
    public void finalize() throws Throwable {
        super.finalize();
        stopPolling();
    }
    
      // Method to check for match requests from other players
    private void checkForMatchRequest() {
        try {
            JSONObject request = new JSONObject();
            request.put("header", "check_match_request");
            String response = Request.getInstance().sendRequest(request.toString());

            JSONObject jsonResponse = new JSONObject(response);
            if ("match_request".equals(jsonResponse.getString("header"))) {
                String opponentUsername = jsonResponse.getString("opponent");
                showMatchRequestAlert(opponentUsername);
            }
        } catch (IOException ex) {
            Logger.getLogger(FXMLAvailablePlayersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     // Show alert when a match request is received
    private void showMatchRequestAlert(String opponentUsername) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Match Request");
        alert.setHeaderText(opponentUsername + " has sent you a match request.");
        alert.setContentText("Do you want to accept the match?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Accept the match
            sendMatchResponse(opponentUsername, true);
        } else {
            // Decline the match
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
    private void receiveRequests() {
        while (true) {
            try {
                JSONObject jsonObject = Request.getInstance().receve();
                System.out.println(jsonObject);
                switch (jsonObject.getString("header")) {
                    case "available_players":
                        System.out.println(jsonObject);
                        Platform.runLater(() -> updateAvailablePlayersListView(jsonObject.getJSONArray("players")));
                        break;
                }
            } catch (IOException ex) {
                System.out.println("Server error!");
                break;
            }
        }
        System.out.println("stop receving");
    }

    private void updateAvailablePlayersListView(JSONArray players) {

        availablePlayersList.getItems().clear();
        
        String currentUser = SessionData.getUsername();

        for (int i = 0; i < players.length(); i++) {
            JSONObject player = players.getJSONObject(i);

            if (!player.getString("username").equals(currentUser)) {
                String display = " " + player.get("username") + " - Score: " + player.get("score");
                availablePlayersList.getItems().add(display);
            }
        }
    }
    
    
}
