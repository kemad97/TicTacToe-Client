package levels;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public abstract class levelsUIBase extends AnchorPane {

    protected final ImageView imageView;
    protected final ImageView imageView0;
    protected final Text text;
    protected final Button hardBtn;
    protected final Button mediumBtn;
    protected final Button easyBtn;

    public levelsUIBase() {

        imageView = new ImageView();
        imageView0 = new ImageView();
        text = new Text();
        hardBtn = new Button();
        mediumBtn = new Button();
        easyBtn = new Button();

        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setPrefHeight(400.0);
        setPrefWidth(600.0);

        imageView.setFitHeight(400.0);
        imageView.setFitWidth(600.0);
        imageView.setImage(new Image(getClass().getResource("background.png").toExternalForm()));

        imageView0.setFitHeight(400.0);
        imageView0.setFitWidth(600.0);
        imageView0.setPickOnBounds(true);
        imageView0.setPreserveRatio(true);

        text.setFill(javafx.scene.paint.Color.valueOf("#fffc2e"));
        text.setLayoutX(162.0);
        text.setLayoutY(117.0);
        text.setStroke(javafx.scene.paint.Color.valueOf("#fffc2e"));
        text.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        text.setStrokeMiterLimit(5.0);
        text.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text.setStrokeWidth(0.5);
        text.setText("Choose Difficulty :");
        text.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        text.setWrappingWidth(283.671875);
        text.setFont(new Font(32.0));

        hardBtn.setLayoutX(221.0);
        hardBtn.setLayoutY(271.0);
        hardBtn.setMnemonicParsing(false);
        hardBtn.setPrefHeight(31.0);
        hardBtn.setPrefWidth(158.0);
        hardBtn.setStyle("-fx-background-color: red;");
        hardBtn.setText("Hard");
        hardBtn.setTextFill(javafx.scene.paint.Color.WHITE);

        mediumBtn.setLayoutX(221.0);
        mediumBtn.setLayoutY(220.0);
        mediumBtn.setMnemonicParsing(false);
        mediumBtn.setPrefHeight(31.0);
        mediumBtn.setPrefWidth(158.0);
        mediumBtn.setStyle("-fx-background-color: blue;");
        mediumBtn.setText("Medium");
        mediumBtn.setTextFill(javafx.scene.paint.Color.WHITE);

        AnchorPane.setRightAnchor(easyBtn, 221.0);
        easyBtn.setAlignment(javafx.geometry.Pos.CENTER);
        easyBtn.setLayoutX(221.0);
        easyBtn.setLayoutY(169.0);
        easyBtn.setMnemonicParsing(false);
        easyBtn.setPrefHeight(31.0);
        easyBtn.setPrefWidth(158.0);
        easyBtn.setStyle("-fx-background-color: green;");
        easyBtn.setText("Easy");
        easyBtn.setTextFill(javafx.scene.paint.Color.WHITE);

        getChildren().add(imageView);
        getChildren().add(imageView0);
        getChildren().add(text);
        getChildren().add(hardBtn);
        getChildren().add(mediumBtn);
        getChildren().add(easyBtn);

    }
}
