package com.learning;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.application.Application;

public class Main extends Application {

    public static void main(String[] args){
        // TODO: GUI


        /*

        Thread server = new Thread(new Server(49191));
        server.start();


        try {
            server.join();
        }
        catch(InterruptedException e) {}

         */


        launch(args);


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GUI gui = new GUI();
        gui.show();
    }
    //////// ADDED AS MODULE
}
