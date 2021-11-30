package com.example.task_plan_manager;

import com.example.task_plan_manager.Utils.ErrorUtils;
import com.example.task_plan_manager.Utils.EventUtils;
import com.example.task_plan_manager.Utils.FileUtils;
import com.example.task_plan_manager.Utils.UIUtils;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        if(!EventUtils.init()) {
            ErrorUtils.InitFail();
            return;
        }
        stage.getIcons().add(new Image("file:"+Globe.getPath()+"/icon.png"));
        Globe.setStage(stage);
        if (FileUtils.existNow()) {
            int flag=EventUtils.start();
            if (flag!=0) return;
            UIUtils.showMain(stage);
        } else {
            UIUtils.showStart(stage);
        }
    }

    public static void main(String[] args) {
        if (args.length!=1) {
            ErrorUtils.Error();
            return;
        }
        Globe.setPath(args[0]);
        launch();
    }
}