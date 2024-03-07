package com.example.horse.Controller;

import com.example.horse.FXStatic;
import com.example.horse.MainApplication;
import com.example.horse.base.LocationDownload;
import com.example.horse.base.MessageDialog;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WorkController {
    private final int row= FXStatic.row;
    private final int col=FXStatic.col;
    private int n=row*col;
    private final int atom=FXStatic.atom;
    private Circle[][] arr=new Circle[row][col];
    private int[][] next={{-2,1},{-1,2},{1,2},{2,1},{-2,-1},{-1,-2},{1,-2},{2,-1}};
    private Map<Circle,Integer> beCheck=new HashMap<>();
    private int[][] array=new int[row+4][col+4];
    private int[] okArr=new int[n];
    private int aa=0,bb=0;//被选中的那个坐标
    private EventHandler<MouseEvent> locationEvent;
    private int speed=300;
    private boolean isPause=true;
    private boolean isStart=false;
    private String location_str="";
    private String str="";
    private int nowNum=1;
    private int len=1;
    private Thread th1;

    private Thread th2;
    GridPane gridPane = new GridPane();

    @FXML
    private Slider sliderSpeed;
    @FXML
    private AnchorPane anchorPaneId;
    @FXML
    private Button addPointButton;
    @FXML
    private Button checkButton;
    @FXML
    private Button startButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button returnButton;
    @FXML
    private Button dfsButton;

    @FXML
    private ChoiceBox<String> choiceBox_mode;
    @FXML
    private Label Label_isOK;

    @FXML
    private Label Label_location;
    @FXML
    private Button nextStepButton;
    @FXML
    private Button continueButton;

    @FXML
    private TextField textField_row;

    @FXML
    private TextField textField_col;





    @FXML
    public void initialize() {


        gridPane.setLayoutX(150);
        gridPane.setLayoutY(5);

        gridPane.setStyle("-fx-background-color:#ffffff");
        for (int i=0;i<row;i++){
            RowConstraints rowConstraints=new RowConstraints(atom);
            rowConstraints.setValignment(VPos.CENTER);
            gridPane.getRowConstraints().add(rowConstraints);
        }
        for(int i=0;i<col;i++){
            ColumnConstraints columnConstraints=new ColumnConstraints(atom);
            columnConstraints.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Circle circle = new Circle();
                circle.setRadius(atom/3.0);
                circle.setFill(Color.WHITE);
//                circle.setCursor(Cursor.CROSSHAIR);
                arr[i][j]=circle;
                beCheck.put(circle,0);
                circle.setOnMouseClicked(e->{
                    if(e.getClickCount()==1){
                        for(int ll=0;ll<row;ll++){
                            for(int kk=0;kk<col;kk++){
                                if(beCheck.get(arr[ll][kk])==1){
                                    beCheck.put(arr[ll][kk],0);
                                    arr[ll][kk].setFill(Color.WHITE);
                                }
                            }
                        }
                        circle.setFill(Color.BLACK);
                        beCheck.put(circle,1);
                    }
                    if(e.getClickCount()==2){
                        circle.setFill(Color.WHITE);
                        beCheck.put(circle,0);
                    }
                });
                circle.setOnMouseEntered(e->{
                    if(beCheck.get(circle)!=1){
                        circle.setFill(Color.web("0xd3d7d4",1.0));
                    }
                });
                circle.setOnMouseExited(e->{
                    if(beCheck.get(circle)!=1){
                        circle.setFill(Color.WHITE);
                    }
                });
                gridPane.add(circle, j,i);
            }
        }

        sliderSpeed.setValue(50);
        speed=850-8*50;
        sliderSpeed.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                speed=850-8*number.intValue();
            }
        });
        choiceBox_mode.getItems().addAll("均衡模式","贪心模式","回溯模式");
        choiceBox_mode.setValue("均衡模式");

        startButton.setDisable(true);
        nextStepButton.setDisable(true);
        pauseButton.setDisable(true);
        continueButton.setDisable(true);

        checkButton.setCursor(Cursor.HAND);
        startButton.setCursor(Cursor.HAND);
        nextStepButton.setCursor(Cursor.HAND);
        pauseButton.setCursor(Cursor.HAND);
        continueButton.setCursor(Cursor.HAND);
        resetButton.setCursor(Cursor.HAND);
        returnButton.setCursor(Cursor.HAND);
        dfsButton.setCursor(Cursor.HAND);

        anchorPaneId.getChildren().add(gridPane);


        double X=gridPane.getLayoutX()+atom/2.0;
        double Y=gridPane.getLayoutY()+atom/2.0;

        for(int i=0;i<col;i++){
            Line line=new Line();
            line.setStrokeWidth(2);
            line.setStartX(X+i*atom);
            line.setStartY(Y);
            line.setEndX(X+i*atom);
            line.setEndY(Y+(row-1)*atom);
            anchorPaneId.getChildren().add(line);
        }
        for(int i=0;i<row;i++){
            Line line=new Line();
            line.setStrokeWidth(2);
            line.setStartX(X);
            line.setStartY(Y+i*atom);
            line.setEndX(X+(col-1)*atom);
            line.setEndY(Y+i*atom);
            anchorPaneId.getChildren().add(line);
        }

        locationEvent=new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                LocationDownload.showDialog(location_str);
            }
        };
        Label_location.addEventHandler(MouseEvent.MOUSE_CLICKED,locationEvent);
        Label_location.setCursor(Cursor.HAND);
        Label_location.setStyle("-fx-underline:true");
    }
    @FXML
    void onAddPointButtonAction() {
        int addRow=-1,addCol=-1;
        try{
            addRow=Integer.parseInt(textField_row.getText());
            addCol=Integer.parseInt(textField_col.getText());
        }catch (Exception e){
            MessageDialog.showDialog("请输入正确的行列号！");
            return;
        }
        if(addRow>=row||addCol>=col||addRow<=-1||addCol<=-1){
            MessageDialog.showDialog("请输入正确的行列号！");
            return;
        }
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                if(beCheck.get(arr[i][j])==1){
                    beCheck.put(arr[i][j],0);
                    arr[i][j].setFill(Color.WHITE);
                }
            }
        }
        arr[addRow][addCol].setFill(Color.BLACK);
        beCheck.put(arr[addRow][addCol],1);
    }
    @FXML
    private void onCheckButtonAction(){
        int num=0;
        int a=0,b=0;
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                if(beCheck.get(arr[i][j])==1){
                    num++;
                    a=i;
                    b=j;
                }
            }
        }
        if(num==0)MessageDialog.showDialog("请点击一个棋子");
        else if(num>1)MessageDialog.showDialog("请只点击一个棋子");
        else {
            aa=a;
            bb=b;
            resetArray();
            String s=choiceBox_mode.getValue();
            boolean bool;
            if(s.equals("回溯模式")){
                if(row*col>36){
                    MessageDialog.showDialog("此棋盘过大，纯粹的回溯模式无法实现，请使用贪心模式或者均衡模式判断");
                    return;
                }
                bool=dfs_(a+2,b+2,1);
            }
            else if (s.equals("贪心模式"))bool=check(a,b);
            else bool=dfs(a+2,b+2,1);
            if(bool){
                Label_isOK.setText("可以实现！");
//                MessageDialog.showDialog("可以实现！");
                dfsButton.setDisable(false);
                Label_isOK.setTextFill(Color.GREEN);
                startButton.setDisable(false);
                nextStepButton.setDisable(false);
            }else{
                Label_isOK.setText("无法实现！");
//                MessageDialog.showDialog("无法实现！");
                Label_isOK.setTextFill(Color.RED);
                startButton.setDisable(true);
            }
        }
    }
    @FXML
    private void onStartButtonAction(){
        gridPane.setDisable(true);
        addPointButton.setDisable(true);
        checkButton.setDisable(true);
        startButton.setDisable(true);
        pauseButton.setDisable(false);
        choiceBox_mode.setDisable(true);
        nextStepButton.setDisable(true);
        dfsButton.setDisable(true);

        th1=new Thread(() -> {
            while (nowNum<n-1){
                try {
                    Thread.sleep(speed);
                }
                catch (Exception ignored) {
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(nowNum==n-1)pauseButton.setDisable(true);
                        aa+=next[ okArr[nowNum] ][ 0 ];
                        bb+=next[ okArr[nowNum] ][ 1 ];
                        Label_location.setText("( "+aa+" , "+bb+" )");
                        location_str+= " ("+aa+" , "+bb+")" ;
                        arr[aa][bb].setFill(Color.BLACK);
                        nowNum++;
                    }
                });
            }
        });
        th1.start();
    }
    int isTh1OrTh2=1;
    @FXML
    private void onPauseButtonAction(){
        if(th1!=null&&th1.isAlive()){
            th1.stop();
            isTh1OrTh2=1;
            continueButton.setDisable(false);
            pauseButton.setDisable(true);
        }
        if(th2!=null&&th2.isAlive()){
            th2.stop();
            isTh1OrTh2=2;
            continueButton.setDisable(false);
            pauseButton.setDisable(true);
        }
    }
    @FXML
    void onContinueButtonAction() {
        pauseButton.setDisable(false);
        continueButton.setDisable(true);
        if(isTh1OrTh2==1){
            th1=new Thread(() -> {
                while (nowNum<n-1){
                    try {
                        Thread.sleep(speed);
                    }
                    catch (Exception ignored) {
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(nowNum==n-1)pauseButton.setDisable(true);
                            aa+=next[ okArr[nowNum] ][ 0 ];
                            bb+=next[ okArr[nowNum] ][ 1 ];
                            Label_location.setText("( "+aa+" , "+bb+" )");
                            location_str+= " ("+aa+" , "+bb+")" ;
                            arr[aa][bb].setFill(Color.BLACK);
                            nowNum++;
                        }
                    });
                }
            });
            th1.start();
        }
        if(isTh1OrTh2==2){
            th2=new Thread(() -> {
                while (len<str.length()-1){
                    try {
                        Thread.sleep(speed);
                    }
                    catch (Exception ignored) {
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            int ii=str.charAt(len)-'0';
                            if(len==str.length()-2)pauseButton.setDisable(true);
                            aa+=next[ii][0];
                            bb+=next[ii][1];
                            if(array[aa+2][bb+2]==0){
                                array[aa+2][bb+2]=1;
                                arr[aa][bb].setFill(Color.BLACK);
                            }else{
                                array[aa+2-next[ii][0]][bb+2-next[ii][1]]=0;
                                arr[aa-next[ii][0]][bb-next[ii][1]].setFill(Color.WHITE);
                            }
                            Label_location.setText("( "+aa+" , "+bb+" )");
                            location_str+= " ("+aa+" , "+bb+")" ;
                            len++;
                        }
                    });
                }

            });
            th2.start();
        }
    }
    @FXML
    void onNextStepButtonAction(){
        gridPane.setDisable(true);
        addPointButton.setDisable(true);
        checkButton.setDisable(true);
        startButton.setDisable(true);
        dfsButton.setDisable(true);
        pauseButton.setDisable(true);
        continueButton.setDisable(true);
        if(nowNum==n-1){
            checkButton.setDisable(false);
            nextStepButton.setDisable(true);
        }
        aa+=next[ okArr[nowNum] ][ 0 ];
        bb+=next[ okArr[nowNum] ][ 1 ];
        Label_location.setText("( "+aa+" , "+bb+" )");
        location_str+= " ("+aa+" , "+bb+")" ;
        arr[aa][bb].setFill(Color.BLACK);
        nowNum++;
    }
    @FXML
    private void onResetButtonAction(){
        if(th1!=null&&th1.isAlive()){
            th1.stop();
            isStart=false;
        }
        if(th2!=null&&th2.isAlive()){
            th2.stop();
            isStart=false;
        }
        nowNum=1;
        location_str="";
        for(int i=0;i<row;i++){
            for (int j=0;j<col;j++){
                arr[i][j].setFill(Color.WHITE);
                beCheck.put(arr[i][j],0);
            }
        }
        textField_row.setText("");
        textField_col.setText("");

        gridPane.setDisable(false);
        Label_isOK.setText("未测定");
        Label_isOK.setTextFill(Color.BLACK);
        Label_location.setText("坐标");
        choiceBox_mode.setValue("均衡模式");
        choiceBox_mode.setDisable(false);

        addPointButton.setDisable(false);
        checkButton.setDisable(false);
        startButton.setDisable(true);
        nextStepButton.setDisable(true);
        pauseButton.setDisable(true);
        continueButton.setDisable(true);
        nextStepButton.setDisable(true);
        dfsButton.setDisable(false);
    }

    @FXML
    private void onReturnButtonAction() throws IOException {
        if(th1!=null&&th1.isAlive()){
            th1.stop();
            isStart=false;
        }
        if(th2!=null&&th2.isAlive()){
            th2.stop();
            isStart=false;
        }
        MainApplication.resetStage("login.fxml","请选择",600,400);
    }
    @FXML
    void onDfsButtonAction(){
        int num=0;
        int a=0,b=0;
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                if(beCheck.get(arr[i][j])==1){
                    num++;
                    a=i;
                    b=j;
                }
            }
        }
        if(num==0)MessageDialog.showDialog("请点击一个棋子");
        else if(num>1)MessageDialog.showDialog("请只点击一个棋子");
        else {
            resetArray();
            aa=a;
            bb=b;
            str="";
            if(dfs_to_Str(a+2,b+2,1,0)){
                choiceBox_mode.setDisable(true);
                addPointButton.setDisable(true);
                checkButton.setDisable(true);
                startButton.setDisable(true);
                nextStepButton.setDisable(true);
                dfsButton.setDisable(true);
                pauseButton.setDisable(false);

                gridPane.setDisable(true);

                Label_isOK.setText("可以实现！");
                Label_isOK.setTextFill(Color.GREEN);
//                MessageDialog.showDialog("可以实现！");
                dfsThread();
            }else{
                Label_isOK.setText("无法实现！");
                Label_isOK.setTextFill(Color.RED);
//                MessageDialog.showDialog("无法实现！");
            }
        }
    }

    private void resetArray() {
        int p=row+4,q=col+4;
        for(int i=0;i<p;i++){
            for(int j=0;j<q;j++){
                if(i>=2&&i<=p-3&&j>=2&&j<=q-3){
                    array[i][j]=0;
                }else{
                    array[i][j]=1;
                }
            }
        }
    }
    private boolean check(int x,int y){//(x,y)位于原点时的坐标为(0,0)
        int[] dp={0,0,0,0,0,0,0,0};
        int rank=1;
        x+=2;
        y+=2;
        while (rank<n) {
            array[x][y]=rank++;
            int nextNum=getNextStepNum(array,dp,x,y,true);
            if(nextNum==0)return false;
            if(nextNum==1){
                for(int i=0;i<8;i++){
                    if(dp[i]==1){
                        x+=next[i][0];
                        y+=next[i][1];
                        okArr[rank-1]=i;
                        break;
                    }
                }
                continue;
            }
            int minLen=9;
            int mini=0;
            for(int i=0;i<8;i++){
                if(dp[i]==1){
                    int Num=getNextStepNum(array,dp,x+next[i][0],y+next[i][1],false);
                    if(Num==0)return false;
                    if(Num<minLen){
                        minLen=Num;
                        mini=i;
                    }
                }
            }
            x+=next[mini][0];
            y+=next[mini][1];
            okArr[rank-1]=mini;
        }
        return true;
    }
    private int getNextStepNum(int[][] array,int[] dp,int x,int y,boolean code) {
        if(code){
            for(int i=0;i<8;i++)dp[i]=0;
        }
        int num=0;
        for(int i=0;i<8;i++){
            if(array[x+next[i][0]][y+next[i][1]]==0){
                num++;
                if(code)dp[i]=1;
            }
        }
        return num;
    }
    private boolean dfs_(int x,int y,int rank){
        if(rank==n)return true;
        array[x][y]=rank;
        for(int i=0;i<8;i++){
            if(array[x+next[i][0]][y+next[i][1]]==0){
                if(dfs_(x+next[i][0],y+next[i][1],rank+1)){
                    okArr[rank]=i;
                    return true;
                }
            }
        }
        array[x][y]=0;
        return false;
    }
    private boolean dfs(int x,int y,int rank){
        if(rank==n)return true;
        array[x][y]=rank;
        int[] dp={0,0,0,0,0,0,0,0};
        int[] ar={0,0,0,0,0,0,0,0};
        int nextNum=getNextStepNum(array,dp,x,y,true);
        if(nextNum==0){
            array[x][y]=0;
            return false;
        }
        int mini=9;
        for(int i=0;i<8;i++){
            if(dp[i]==1){
                int Num=getNextStepNum(array,dp,x+next[i][0],y+next[i][1],false);
                if(Num!=0){
                    ar[i]=Num;
                    if(Num<mini)mini=Num;
                }
            }
        }
        if(rank==n-1){
            mini=1;
            System.arraycopy(dp, 0, ar, 0, 8);
        }
        for(int i=0;i<8;i++){
            if(ar[i]==mini) {
                if (array[x + next[i][0]][y + next[i][1]] == 0) {
                    if (dfs(x + next[i][0], y + next[i][1], rank + 1)) {
                        okArr[rank] = i;
                        return true;
                    }
                }
            }
        }
        array[x][y]=0;
        return false;
    }
    private boolean dfs_to_Str(int x,int y,int rank,int code){
        if(rank==n){
            str+=code+"";
            return true;
        }
        array[x][y]=rank;
        str+=code+"";
        int[] dp={0,0,0,0,0,0,0,0};
        int[] ar={0,0,0,0,0,0,0,0};
        int nextNum=getNextStepNum(array,dp,x,y,true);
        if(nextNum==0){
            array[x][y]=0;
            str+=(7-code)+"";
            return false;
        }
        int mini=9;
        for(int i=0;i<8;i++){
            if(dp[i]==1){
                int Num=getNextStepNum(array,dp,x+next[i][0],y+next[i][1],false);
                if(Num!=0){
                    ar[i]=Num;
                    if(Num<mini)mini=Num;
                }
            }
        }
        if(rank==n-1){
            mini=1;
            System.arraycopy(dp, 0, ar, 0, 8);
        }
        for(int i=0;i<8;i++){
            if(ar[i]==mini) {
                if (array[x + next[i][0]][y + next[i][1]] == 0) {
                    if (dfs_to_Str(x + next[i][0], y + next[i][1], rank + 1,i)) {
                        okArr[rank] = i;
                        return true;
                    }
                }
            }
        }
        str+=(7-code)+"";
        array[x][y]=0;
        return false;
    }

    private void dfsThread(){
        len=1;
        resetArray();
        array[aa+2][bb+2]=1;
        th2=new Thread(() -> {
            while (len<str.length()-1){
                try {
                    Thread.sleep(speed);
                }
                catch (Exception ignored) {
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        int ii=str.charAt(len)-'0';
                        if(len==str.length()-2)pauseButton.setDisable(true);
                        aa+=next[ii][0];
                        bb+=next[ii][1];
                        if(array[aa+2][bb+2]==0){
                            array[aa+2][bb+2]=1;
                            arr[aa][bb].setFill(Color.BLACK);
                        }else{
                            array[aa+2-next[ii][0]][bb+2-next[ii][1]]=0;
                            arr[aa-next[ii][0]][bb-next[ii][1]].setFill(Color.WHITE);
                        }
                        Label_location.setText("( "+aa+" , "+bb+" )");
                        location_str+= " ("+aa+" , "+bb+")" ;
                        len++;
                    }
                });
            }

        });
        th2.start();
    }
}
