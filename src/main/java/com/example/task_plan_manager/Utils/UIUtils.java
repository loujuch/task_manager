package com.example.task_plan_manager.Utils;

import com.example.task_plan_manager.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UIUtils {

    public final static String TAG="com.example.task_plan_manager.Utils.UIUtils.";

    public static void showStart(Stage stage) throws IOException {
        if(stage==null) {
            ErrorUtils.NullPointerInputError(TAG+"showStart");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setTitle("登录/注册");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void showMain(Stage stage) throws IOException {
        if(stage==null) {
            ErrorUtils.NullPointerInputError(TAG+"showMain");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 520);
        stage.setTitle("任务/计划管理器");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
