package com.codecool.klondike;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

class PopUp {
    public static Klondike gameInstance;
    public static Stage stage;

    public void showDialog() {
        Alert alert = new Alert(AlertType.NONE, "YOU ARE WINNER");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("congrats.css").toExternalForm());
        dialogPane.getStyleClass().add("congrats");

        ButtonType playAgain = new ButtonType("Play Again");
        ButtonType quit = new ButtonType("Quit");

        // Remove default ButtonTypes
        alert.getButtonTypes().clear();

        alert.getButtonTypes().addAll(playAgain, quit);

        alert.show();

        Button playAgainButton = (Button) alert.getDialogPane().lookupButton(playAgain);
        playAgainButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                /*try {

                    try {
                        Thread.sleep(900);
                        System.out.println("sleep success");
                    } catch (Exception e) {
                        System.out.println("sleep failed");
                    }
                    Robot robot = new Robot();

                    // Simulate a mouse click
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);

                    // Simulate a key press
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_X);
                    System.out.println("ctrlx pressed");
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                    robot.keyRelease(KeyEvent.VK_X);
                    System.out.println("ctrlx released");
                } catch (AWTException e) {
                    e.printStackTrace();
                }*/

                gameInstance.restart(stage);
            }
        });

        Button quitButton = (Button) alert.getDialogPane().lookupButton(quit);
        quitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
    }
}
