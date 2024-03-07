package com.example.horse.Controller;

import com.example.horse.MainApplication;
import com.example.horse.base.MessageDialog;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

public class LocationController {
    public BorderPane BorderPaneRoot;
    @FXML
    private TextFlow textFLow;

    private Text text;
    private Stage stage;
    @FXML
    public void initialize() {
        BorderPaneRoot.setStyle("-fx-background-color: #f0f8ff;-fx-font-family: 'Arial';-fx-font-size: 16;");
        text = new Text("");
        text.setFill(Color.BLACK);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        textFLow.getChildren().add(text);
        textFLow.setLineSpacing(5);
        textFLow.setDisable(false);
        textFLow.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 1;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;"+
                "-fx-background-color:#FFFACD");
    }

    @FXML
    public void okButtonClick(){
        this.stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);//让窗口不能改变大小
    }
    public void showDialog(String msg) {
        text.setText(msg);
        stage.setAlwaysOnTop(true);
        ImageView img = new ImageView();//修改图片位置
        Image image=new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("Picture/horsePic.jpg")));
        img.setImage(image);
        stage.getIcons().add(image);
        this.stage.show();
    }
    @FXML
    void onDownloadButtonAction() {
        String path = System.getProperty("user.dir");
        String folderName="DownLoadLocation";
        String fileName="file1";
        byte[] sourceByte = text.getText().getBytes();
        if(null != sourceByte){
            try {
                File file = new File(path+"\\"+folderName+"\\"+fileName+".txt");
                int nn=2;
                while(file.exists()){
                    fileName=fileName.substring(0,fileName.length()-1);
                    fileName+=nn+"";
                    file = new File(path+"\\"+folderName+"\\"+fileName+".txt");
                    nn++;
                }
                //文件不存在则创建文件，先创建目录
                file.getParentFile().mkdirs();
                file.createNewFile();

                FileOutputStream outStream = new FileOutputStream(file); //文件输出流将数据写入文件
                outStream.write(sourceByte);
                outStream.close();
                MessageDialog.showDialog("文件已下载!\n"
                +"下载位置：\n"+path+"\\"+folderName+"\\"+fileName+".txt");
            } catch (Exception e) {
                e.printStackTrace();
                MessageDialog.showDialog("下载失败！");
            }
        }
    }
    public void writeToFile(String path,String fileName,String data){

    }
}
