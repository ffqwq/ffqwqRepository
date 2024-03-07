package com.example.horse.Controller;

import com.example.horse.FXStatic;
import com.example.horse.MainApplication;
import com.example.horse.base.MessageDialog;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.io.IOException;
public class LoginController {

    @FXML
    private Button enterButton;
    @FXML
    private TextField rowId;
    @FXML
    private TextField colId;

    @FXML
    private Label useLabId;
    @FXML
    private void initialize() {
        useLabId.setTextFill(Color.GREEN);
        useLabId.setCursor(Cursor.HAND);
        useLabId.setStyle("-fx-underline:true");
        useLabId.setOnMouseClicked(e->{
            MessageDialog.showDialog("本项目是一个数据结构课设，目标是对任意规格的棋盘，"+
                            "任意选择位置，以象棋中马的行走方式，判断出能否把棋盘上所有位置不重复的走遍，并用图形表现出来。"
            +"在稍后的页面中请点击一个位置，作为第一个马的位置，点击“开始检查”，判断当前能否成功遍历；若能遍历，点击"+
                    "启动动画，观看马跳的过程。"+"你可以自己选择棋盘的长宽。在接下来的棋盘中，单击放置棋子，双击取消放置。");
        });
        rowId.setText("10");
        colId.setText("9");
        enterButton.setCursor(Cursor.HAND);
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        FXStatic.ScreenWidth = (int)screenRectangle.getWidth();
        FXStatic.ScreenHeight = (int)screenRectangle.getHeight();
    }
    @FXML
    void onEnterAction() throws IOException {
        if(rowId.getText().isEmpty()||colId.getText().isEmpty()){
            MessageDialog.showDialog("请输入行号和列号！");
            return;
        }
        try{
            int row=Integer.parseInt(rowId.getText());
            int col=Integer.parseInt(colId.getText());
            FXStatic.row=row;
            FXStatic.col=col;
        }catch (Exception e){
            MessageDialog.showDialog("请输入数字！");
            rowId.setText("");
            colId.setText("");
            return;
        }
        int row=FXStatic.row;
        int col=FXStatic.col;
        int width=FXStatic.ScreenWidth-150;
        int height=FXStatic.ScreenHeight;
        if(col*height<=row*width){
            FXStatic.atom=height/(row+1);
        }else{
            FXStatic.atom=width/(col+1);
        }
        MainApplication.resetStage("work.fxml","主内容",FXStatic.atom*(col+1)+150,FXStatic.atom*(row+1));
    }
}