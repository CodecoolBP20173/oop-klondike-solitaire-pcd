package com.codecool.klondike;
import java.net.URL;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

class PopUp {

    //private Label label;

    public void showDialog() {
        Alert alert = new Alert(AlertType.NONE, "YOU ARE WINNER");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        /*URL url = this.getClass().getResource("congrats.css");
        if (url == null) {
            System.out.println("Resource not found. Aborting.");
            System.exit(-1);
        }
        String css = url.toExternalForm();*/

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("congrats.css").toExternalForm());
        dialogPane.getStyleClass().add("congrats");
        //dialogPane.getStyleClass().add(css);

        ButtonType playAgain = new ButtonType("Play Again");
        ButtonType quit = new ButtonType("Quit");

        alert.setResizable(true);
        dialogPane.setPrefSize(600, 400);

        // Remove default ButtonTypes
        alert.getButtonTypes().clear();

        alert.getButtonTypes().addAll(playAgain, quit);

        // option != null.
        alert.show();

        /*if (option.get() == null) {
            //this.label.setText("No selection!");
        } else if (option.get() == ButtonType.OK) {
            //this.label.setText("File deleted!");
        } else if (option.get() == ButtonType.CANCEL) {
            //this.label.setText("Cancelled!");
        } else {
            //this.label.setText("-");
        }*/
    }


}
