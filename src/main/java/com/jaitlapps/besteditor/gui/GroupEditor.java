package com.jaitlapps.besteditor.gui;

import com.jaitlapps.besteditor.GroupSaver;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GroupEditor extends Application {

    Stage primaryStage;

    @FXML
    ImageView imageView;

    @FXML
    TextField groupTitleField;

    File iconFile;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
    }

    @FXML
    private void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Выбор иконки для группы");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File selectedImage = fileChooser.showOpenDialog(primaryStage);

        if (selectedImage != null) {
            if(validateIconSize(selectedImage)) {
                setImage(selectedImage.getPath());
                iconFile = selectedImage;
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Иконка группы слишком маленькая");
                alert.setHeaderText(null);
                alert.setContentText("Иконка группы слишком маленькая, выберите иконку побольше");
                alert.showAndWait();

                setImage(null);
                iconFile = null;
            }
        }
    }

    private boolean validateIconSize(File selectedImage) {

        BufferedImage image = null;

        try {
            image = ImageIO.read(selectedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image != null && (image.getHeight() < GroupSaver.IMAGE_HEIGHT
                || image.getWidth() < GroupSaver.IMAGE_HEIGHT)) {
            return false;
        }

        return true;
    }

    @FXML
    private void cancelDialog(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void saveGroup(ActionEvent event) {
        String groupTitle = groupTitleField.getText();

        if (groupTitle != null && groupTitle.length() > 0) {
            if (iconFile != null) {
                GroupSaver groupSaver = new GroupSaver();
                try {
                    groupSaver.saveGroup(groupTitle, iconFile);
                    clearDialog();
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Иконка группы не выбрана");
                alert.setHeaderText(null);
                alert.setContentText("Иконка группы не выбрана!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Поле не заполнено");
            alert.setHeaderText(null);
            alert.setContentText("Поле \"Название группы\" не заполнено!");
            alert.showAndWait();
        }
    }



    private void setImage(String pathToIcon) {
        Image icon = new Image("file:///" + pathToIcon);
        imageView.setImage(icon);
    }

    private void clearDialog() {
        groupTitleField.setText("");
        imageView.setImage(null);
    }
}
