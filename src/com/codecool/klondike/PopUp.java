package com.codecool.klondike;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

class PopUp {

    public void showDialog() {
        Alert alert = new Alert(AlertType.NONE, "YOU ARE WINNER");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("congrats.css").toExternalForm());
        dialogPane.getStyleClass().add("congrats");

        ButtonType playAgain = new ButtonType("Play Again");
        ButtonType quit = new ButtonType("Quit");

        alert.setResizable(true);
        dialogPane.setPrefSize(600, 400);

        // Remove default ButtonTypes
        alert.getButtonTypes().clear();

        alert.getButtonTypes().addAll(playAgain, quit);

        alert.show();

    }


}
