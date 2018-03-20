package com.codecool.klondike;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class PopUp {

    //private Label label;

    public void showDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Congratulations!");
        alert.setHeaderText("You won");
        alert.setContentText("Do you want to play again?");

        ButtonType playAgain = new ButtonType("Play Again");
        ButtonType quit = new ButtonType("Quit");

        // Remove default ButtonTypes
        alert.getButtonTypes().clear();

        alert.getButtonTypes().addAll(playAgain, quit);

        // option != null.
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null) {
            //this.label.setText("No selection!");
        } else if (option.get() == ButtonType.OK) {
            //this.label.setText("File deleted!");
        } else if (option.get() == ButtonType.CANCEL) {
            //this.label.setText("Cancelled!");
        } else {
            //this.label.setText("-");
        }
    }


}
