package tictactoe.client.main_screen;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import tictactoe.client.server_ip.ServerIP;

public class FXMLMainScreenController implements Initializable {

    @FXML
    private ImageView logo;
    @FXML
    private TextField inputIP;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //animate logo
        ScaleTransition transition = new ScaleTransition();
        transition.setNode(logo);
        transition.setDuration(Duration.millis(1000));
        transition.setCycleCount(ScaleTransition.INDEFINITE);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setByX(0.5);
        transition.setByY(0.5);
        transition.setAutoReverse(true);
        transition.play();

        //initialize server ip
        new Thread(() -> {
            System.out.println(getClass().getResource("/media/server_ip/server_ip.txt"));
            String ip = ServerIP.getIP(getClass().getResource("/media/server_ip/server_ip.txt").toString());
            System.out.println("ip: " + ip);
        }).start();

    }

    @FXML
    private void startOnlineMatch(MouseEvent event) {
        System.out.println("start online match");
    }

    @FXML
    private void startOfflineMatchVSFriend(MouseEvent event) {
        System.out.println("start offline match vs. Friend");
    }

    @FXML
    private void startOfflineMatchVSPC(MouseEvent event) {
        System.out.println("start offline match vs. PC");
    }

    @FXML
    private void updateIP(ActionEvent event) {
        System.out.println("update ip: " + inputIP.getText());
        inputIP.setPromptText(inputIP.getText());
        inputIP.setText("");
    }

}
