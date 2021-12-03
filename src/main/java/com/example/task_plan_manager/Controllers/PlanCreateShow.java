package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.other.Globe;
import com.example.task_plan_manager.other.Pass;
import com.example.task_plan_manager.Utils.ErrorUtils;
import com.example.task_plan_manager.Utils.EventUtils;
import com.example.task_plan_manager.Utils.FileUtils;
import com.example.task_plan_manager.Utils.UIUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;

public class PlanCreateShow extends VBox {

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
        UIUtils.setChoiceBox(importance,0);
        files=new ArrayList<>();
        add.setOnAction(e -> UIUtils.onAddFile(add_file,files));
        id=-1;
        finish.setOnAction(e -> onFinish());
        importance.getSelectionModel().selectedIndexProperty().addListener(
                (observableValue, number, t1) -> important=t1.intValue()>>>1);
        space.setText("0");
        continue_time.setText("1");
        UIUtils.setNumberInput(space);
        UIUtils.setNumberInput(continue_time);
    }

    public void onFinish() {
        String s=name.getText();
        LocalDate left=start.getValue().atStartOfDay().toLocalDate();
        LocalDate right=end.getValue().atStartOfDay().toLocalDate();
        String detailStr=detail.getText();
        String timeStr=continue_time.getText();
        String spaceStr=space.getText();
        if (s==null||left==null||right==null||detailStr==null||timeStr==null||spaceStr==null) {
            ErrorUtils.Error();
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
        if (!UIUtils.isDuplicate(files)) {
            ErrorUtils.InputError();
            return;
        }
        int spaceInt=Integer.parseInt(spaceStr);
        int timeInt=Integer.parseInt(timeStr);
        if (id==-1) {
            int num=EventUtils.createEvent(s,left,right,important,detailStr,files,
                    spaceInt,timeInt,-1,true);
            for (;left.isBefore(right.plusDays(1));left=left.plusDays(spaceInt+timeInt)) {
                EventUtils.createEvent(s,left,left.plusDays(timeInt-1),important,detailStr,
                        files,-1,-1,num,true);
            }
            if (Globe.getUser().isCut()) {
                for(FileShow fileShow:files) {
                    FileUtils.deleteFile(fileShow.getPath());
                }
            }
        }
        Pass.getMainShow().getChildren().clear();
        Pass.getMainShow().getChildren().add(new PlanCreateShow());
    }
}
