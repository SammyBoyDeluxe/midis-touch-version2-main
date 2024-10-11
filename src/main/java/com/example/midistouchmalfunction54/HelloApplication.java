package com.example.midistouchmalfunction54;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Available resources:");
        Enumeration<URL> resources = getClass().getClassLoader().getResources("");
        while (resources.hasMoreElements()) {
            System.out.println(resources.nextElement());
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getClassLoader().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 860, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {

        launch();



    }
}