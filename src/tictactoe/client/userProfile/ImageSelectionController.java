/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.userProfile;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ImageSelectionController {

    @FXML
    private ImageView image1;
    @FXML
    private ImageView image2;
    @FXML
    private ImageView image3;
    @FXML
    private ImageView image4;

    private Image selectedImage;

    @FXML
    public void initialize() {
       
        image1.setImage(new Image(getClass().getResourceAsStream("/media/images/character/character_1.png")));
        image2.setImage(new Image(getClass().getResourceAsStream("/media/images/character/character_2.png")));
        image3.setImage(new Image(getClass().getResourceAsStream("/media/images/character/character_3.png")));
        image4.setImage(new Image(getClass().getResourceAsStream("/media/images/character/character_4.png")));
    }

    @FXML
    private void selectImage1() {
        selectedImage = image1.getImage();
    }

    @FXML
    private void selectImage2() {
        selectedImage = image2.getImage();
    }

    @FXML
    private void selectImage3() {
        selectedImage = image3.getImage();
    }
    
    @FXML
    private void selectImage4() {
        selectedImage = image4.getImage();
    }

    @FXML
    private void confirmSelection() {
        Stage stage = (Stage) image1.getScene().getWindow();
        stage.close();
    }

    public Image getSelectedImage() {
        return selectedImage;
    }
}

