package com.codecool.klondike;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.KeyCombination;



public class Klondike extends Application {

    private static final double WINDOW_WIDTH = 1400;
    private static final double WINDOW_HEIGHT = 900;

    public static void main(String[] args) {
        Application.launch(args);
    }


    public void startGame(Stage primaryStage) {
        Card.loadCardImages();
        Game game = new Game();
        game.setTableBackground(new Image("/table/coin.jpeg"));


        addMenu(game, primaryStage);

        primaryStage.setTitle("Klondike Solitaire");
        primaryStage.setScene(new Scene(game, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
        PopUp.gameInstance = this;
        PopUp.stage = primaryStage;
        //MouseUtil.game = game;

    }

    public void addMenu(Game game, Stage stage) {
        // Create MenuBar
        MenuBar menuBar = new MenuBar();

        // Create menus
        Menu fileMenu = new Menu("File");
        // TODO implement if edit is done, else remove before release
        // Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");

        // Create MenuItems
        MenuItem newItem = new MenuItem("New");
        MenuItem exitItem = new MenuItem("Exit");

        // Set Accelerator for Exit MenuItem.
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));

        // When user click on the Exit item.
        exitItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        //Set Accelerator for Restart(New) MenuItem
        newItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));

        //When user click on New item
        newItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                restart(stage);
            }
        });

        //MenuItem copyItem = new MenuItem("");

        // Add menuItems to the Menus
        fileMenu.getItems().addAll(newItem, exitItem);
        //editMenu.getItems().addAll(copyItem, pasteItem);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, /*editMenu,*/ helpMenu);

        menuBar.prefWidthProperty().bind(stage.widthProperty());
        game.getChildren().add(menuBar);

    }

    public void restart(Stage stage) {
        startGame(stage);
    }


    @Override
    public void start(Stage primaryStage) {
        startGame(primaryStage);
    }


}
