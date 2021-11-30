package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.Event;
import com.example.task_plan_manager.Pass;
import com.example.task_plan_manager.Utils.ErrorUtils;
import com.example.task_plan_manager.Utils.EventUtils;
import com.example.task_plan_manager.Utils.UIUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class TaskCreateShow extends VBox {

    private final static String TAG="com.example.task_plan_manager.TaskCreateShow.";

    private final ArrayList<FileShow>files=new ArrayList<>();
    private final int id;
    private int important=0;

    @FXML private TextField name;
    @FXML private DatePicker start;
    @FXML private DatePicker end;
    @FXML private ChoiceBox importance;
    @FXML private Button add;
    @FXML private TextArea detail;
    @FXML private VBox add_file;
    @FXML private Button finish;
    @FXML private Label detail_label;
    @FXML private Label other_label;

    public TaskCreateShow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("task_create_show.fxml"));
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
        add.setOnAction(e -> UIUtils.onAddFile(add_file,files));
        id=-1;
        finish.setOnAction(e -> onFinish());
        importance.getSelectionModel().selectedIndexProperty().addListener(
                (observableValue, number, t1) -> important=t1.intValue()>>>1);
    }

    public TaskCreateShow(int id, String name, long start_time, long end_time, int importance,
                          int offset, String s, boolean sub) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("task_create_show.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.id=id;
        this.name.setText(name);
        start.setValue(new Date(start_time).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        end.setValue(new Date(end_time).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        this.importance.getItems().add(Event.IMPORT[importance]);
        this.name.setDisable(true);
        start.setDisable(true);
        end.setDisable(true);
        this.importance.setDisable(true);
        detail_label.setText("总结：");
        other_label.setText("总结附件：");
        add.setOnAction(e -> UIUtils.onAddFile(add_file,files));
        finish.setOnAction(e -> toFinish(offset,s,sub));
    }

    public void toFinish(int offset, String str, boolean sub) {
        String s=detail.getText();
        if (s==null) {
            ErrorUtils.NullPointerInputError(TAG+"toFinish()");
            return;
        }
        HashSet<String>set=new HashSet<>();
        for (FileShow i:files) {
            if (set.contains(i.getName())) {
                ErrorUtils.FileNameRepeat();
                return;
            }
            set.add(i.getName());
        }
        int n=EventUtils.finishEvent(id,s,files);
        if (n!=0)ErrorUtils.OperateFail();
        Pass.getMainShow().getChildren().clear();
        Pass.getMainShow().getChildren().add(new EventsShow(str,offset-(sub?1:0)));
    }

    public void onFinish() {
        String s=name.getText();
        LocalDate left=start.getValue();
        LocalDate right=end.getValue();
        String detailStr=detail.getText();
        if (s==null||left==null||right==null) {
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
        HashSet<String>set=new HashSet<>();
        for (FileShow i:files) {
            if (set.contains(i.getName())) {
                ErrorUtils.FileNameRepeat();
                return;
            }
            set.add(i.getName());
        }
        EventUtils.createEvent(s,left,right,important,detailStr,files,-1,-1);
        Pass.getMainShow().getChildren().clear();
        Pass.getMainShow().getChildren().add(new TaskCreateShow());
    }
}
