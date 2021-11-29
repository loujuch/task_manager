package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.Event;
import com.example.task_plan_manager.Pass;
import com.example.task_plan_manager.Utils.ErrorUtils;
import com.example.task_plan_manager.Utils.EventUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PlanCreateShow extends VBox {

    private final static String TAG="com.example.task_plan_manager.PlanCreateShow.";

    private final ArrayList<FileShow>files;
    private final int id;
    private int important=0;

    @FXML private TextField name;
    @FXML private DatePicker start;
    @FXML private DatePicker end;
    @FXML private TextField space;
    @FXML private TextField continue_time;
    @FXML private ChoiceBox importance;
    @FXML private TextArea detail;
    @FXML private Button add;
    @FXML private VBox add_file;
    @FXML private Button finish;

    public PlanCreateShow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("plan_create_show.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        start.setValue(LocalDate.now());
        end.setValue(LocalDate.now().plusDays(7));
        importance.getItems().add(Event.IMPORT[0]);
        importance.getItems().add(new Separator());
        importance.getItems().add(Event.IMPORT[1]);
        importance.getItems().add(new Separator());
        importance.getItems().add(Event.IMPORT[2]);
        importance.setValue(Event.IMPORT[0]);
        files=new ArrayList<>();
        add.setOnAction(e -> onAddFile());
        id=-1;
        finish.setOnAction(e -> onFinish());
        importance.getSelectionModel().selectedIndexProperty().addListener(
                (observableValue, number, t1) -> important=t1.intValue()>>>1);
        space.setText("0");
        continue_time.setText("1");
        space.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                space.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        continue_time.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                continue_time.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void onFinish() {
        String s=name.getText();
        LocalDate left=start.getValue();
        LocalDate right=end.getValue();
        String detailStr=detail.getText();
        String timeStr=continue_time.getText();
        String spaceStr=space.getText();
        if (s==null||left==null||right==null||detailStr==null||timeStr==null||spaceStr==null) {
            ErrorUtils.NullPointerInputError(TAG+"onFinish");
            return;
        }
        if (s.isBlank()||s.length()>255) {
            ErrorUtils.NameError();
            return;
        }
        if (left.isAfter(right)) {
            ErrorUtils.DateError();
            return;
        }
        HashSet<String> set=new HashSet<>();
        for (FileShow i:files) {
            if (set.contains(i.getName())) {
                ErrorUtils.FileNameRepeat();
                return;
            }
            set.add(i.getName());
        }
        if (id==-1) {
            EventUtils.createEvent(s,left,right,important,detailStr,files,
                    Integer.parseInt(spaceStr),Integer.parseInt(timeStr));

        }
        Pass.getMainShow().getChildren().clear();
        Pass.getMainShow().getChildren().add(new PlanCreateShow());
    }

    @FXML
    public void onAddFile() {
        FileChooser fileChooser=new FileChooser();
        List<File> list=fileChooser.showOpenMultipleDialog(new Stage());
        if(list!=null) {
            for (File file:list) {
                FileShow fileShow=new FileShow(add_file,file,files);
                files.add(fileShow);
                add_file.getChildren().add(fileShow);
            }
        }
    }
}
