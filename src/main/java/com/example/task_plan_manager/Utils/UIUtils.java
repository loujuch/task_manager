package com.example.task_plan_manager.Utils;

import com.example.task_plan_manager.Controllers.FileShow;
import com.example.task_plan_manager.other.Event;
import com.example.task_plan_manager.other.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class UIUtils {


    public static void showStart(Stage stage) throws IOException {
        if(stage==null) {
            ErrorUtils.Error();
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
            ErrorUtils.Error();
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 520);
        stage.setTitle("任务/计划管理器");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void setNumberInput(TextField text) {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                text.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public static void onAddFile(VBox root, ArrayList<FileShow> tmp) {
        FileChooser fileChooser=new FileChooser();
        List<File> list=fileChooser.showOpenMultipleDialog(new Stage());
        if(list!=null) {
            for (File file:list) {
                FileShow fileShow=new FileShow(root,file,tmp);
                tmp.add(fileShow);
                root.getChildren().add(fileShow);
            }
        }
    }

    public static boolean isDuplicate(ArrayList<FileShow>files) {
        HashSet<String> set=new HashSet<>();
        for (FileShow i:files) {
            if (set.contains(i.getName())) {
                ErrorUtils.FileNameRepeat();
                return false;
            }
            set.add(i.getName());
        }
        return true;
    }

    public static void setChoiceBox(ChoiceBox importance, int n) {
        if (importance==null) {
            return;
        }
        importance.getItems().add(Event.IMPORT[0]);
        importance.getItems().add(new Separator());
        importance.getItems().add(Event.IMPORT[1]);
        importance.getItems().add(new Separator());
        importance.getItems().add(Event.IMPORT[2]);
        importance.setValue(Event.IMPORT[n]);
    }
}
