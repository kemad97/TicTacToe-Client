package scene_navigation;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneNavigation {

    private static SceneNavigation instatnce;

    private SceneNavigation() {}

    public static SceneNavigation getInstance() {
        if (instatnce == null) {
            instatnce = new SceneNavigation();
        }
        return instatnce;
    }

    public void nextScene(String fxmlPath, Node node) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root);

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }
}
