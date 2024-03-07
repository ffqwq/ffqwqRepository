package com.example.horse.base;

import com.example.horse.Controller.LocationController;
import com.example.horse.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class LocationDownload {
    private LocationController locationController= null;
    private static LocationDownload instance = new LocationDownload();
    private LocationDownload() {
        FXMLLoader fxmlLoader;
        Scene scene;
        Stage stage;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("location.fxml"));
            scene = new Scene(fxmlLoader.load(), 380, 330);
            scene.getStylesheets().add(MainApplication.class.getResource("css/all.css").toExternalForm());
            stage = new Stage();
            stage.initOwner(null);
            stage.setScene(scene);
            stage.setTitle("坐标查看及下载");
            locationController = (LocationController) fxmlLoader.getController();
            locationController.setStage(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void showDialog(String msg) {
        if(instance == null)return;
        if(instance.locationController == null)return;
        instance.locationController.showDialog(msg);
    }
}

