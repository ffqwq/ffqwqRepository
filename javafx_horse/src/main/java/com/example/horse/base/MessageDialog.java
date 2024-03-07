package com.example.horse.base;

import com.example.horse.Controller.MessageController;
import com.example.horse.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class MessageDialog {
    private MessageController messageController= null;
    private static MessageDialog instance = new MessageDialog();
    private String s="注意！";
    private MessageDialog() {
        FXMLLoader fxmlLoader ;
        Scene scene;
        Stage stage;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("message.fxml"));
            scene = new Scene(fxmlLoader.load(), 380, 330);
            scene.getStylesheets().add(MainApplication.class.getResource("css/all.css").toExternalForm());
            stage = new Stage();
            stage.initOwner(null);
            stage.setScene(scene);
            stage.setTitle(s);
            messageController = (MessageController) fxmlLoader.getController();
            messageController.setStage(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void showDialog(String msg) {
        if(instance == null)return;
        if(instance.messageController == null)return;
        instance.messageController.showDialog(msg);
    }
}

