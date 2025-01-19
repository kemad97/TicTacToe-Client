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
import javafx.animation.ScaleTransition;
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
