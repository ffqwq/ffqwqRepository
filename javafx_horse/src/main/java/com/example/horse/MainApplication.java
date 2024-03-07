package com.example.horse;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    public static Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600,400);
        scene.getStylesheets().add(MainApplication.class.getResource("css/all.css").toExternalForm());
        stage.setTitle("请选择");
        stage.setScene(scene);

        ImageView img = new ImageView();//修改图片位置
        Image image=new Image(Objects.requireNonNull(getClass().getResourceAsStream("Picture/horsePic.jpg")));
        img.setImage(image);
        stage.getIcons().add(image);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        stage.show();
        mainStage=stage;
    }
    public static void resetStage(String UML, String title, int width, int height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(UML));
        Scene scene = new Scene(fxmlLoader.load(), width,height);

        scene.getStylesheets().add(MainApplication.class.getResource("css/all.css").toExternalForm());
        mainStage.setY((FXStatic.ScreenHeight-height)/2);
        mainStage.setX((FXStatic.ScreenWidth-width)/2);
        mainStage.setTitle(title);
        mainStage.setScene(scene);
        ImageView img = new ImageView();//修改图片位置
        Image image=new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("Picture/horsePic.jpg")));
        img.setImage(image);
        mainStage.getIcons().add(image);
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        mainStage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}