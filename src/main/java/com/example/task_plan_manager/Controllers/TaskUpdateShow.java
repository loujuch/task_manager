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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskUpdateShow extends EventUpdateShow {

    private int event;
    private int new_importance;
    private ArrayList<FileShow>out=new ArrayList<>();
    private ArrayList<FileShow>in=new ArrayList<>();

    @FXML private TextField name;
    @FXML private DatePicker start;
    @FXML private DatePicker end;
    @FXML private ChoiceBox importance;
    @FXML private TextArea detail;
    @FXML private VBox remark_label;
    @FXML private TextArea remark;
    @FXML private VBox file_label;
    @FXML private VBox file_local;
    @FXML private VBox file_local_add;
    @FXML private VBox file_cloud;
    @FXML private VBox file_cloud_add;
    @FXML private VBox other_label;
    @FXML private VBox other_local;
    @FXML private VBox other_local_add;
    @FXML private VBox other_cloud;
    @FXML private VBox other_cloud_add;
    @FXML private Button file_add;
    @FXML private Button other_add;
    @FXML private Button ok;

    public TaskUpdateShow(List<Object>tmp, int id, int offset, String s) {
        if (tmp==null) {
            ErrorUtils.Error();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("task_update_show.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.event=id;
        name.setText((String) tmp.get(0));
        UIUtils.setChoiceBox(importance,(int) tmp.get(1));
        new_importance=(int) tmp.get(1);
        start.setValue(new Date((long) tmp.get(2)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        end.setValue(new Date((long) tmp.get(3)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        String path=Globe.getPath()+"/.data/"+ Globe.getUser().getId()+"/"+id+"/";
        if ((boolean) tmp.get(4))fileWrite(detail,path+"detail");
        boolean finish= (boolean) tmp.get(10);
        remark_label.setVisible(finish);
        remark_label.setManaged(finish);
        if (finish&&(boolean) tmp.get(5))fileWrite(remark,path+"remark");
        setDateBaseShow(file_label,file_local,file_local_add,file_cloud,file_cloud_add,
                true,FileUtils.IN,in,event);
        boolean have=(boolean) tmp.get(7);
        other_label.setManaged(finish);
        other_label.setVisible(finish);
        if (finish) setDateBaseShow(other_label,other_local,other_local_add,other_cloud,other_cloud_add,
                true,FileUtils.OUT,out,event);
        importance.getSelectionModel().selectedIndexProperty().addListener(
                (observableValue, number, t1) -> new_importance=t1.intValue()>>>1);
        file_add.setOnAction(e -> UIUtils.onAddFile(file_local_add,in));
        if (finish)other_add.setOnAction(e -> UIUtils.onAddFile(other_local_add,out));
        ok.setOnAction(e -> onOK(offset,s));
    }

    private void onOK(int offset, String s) {
        if (!(UIUtils.isDuplicate(in)&&UIUtils.isDuplicate(out))) {
            ErrorUtils.InputError();
            return;
        }
        String new_name=name.getText();
        LocalDate new_start=start.getValue();
        LocalDate new_end=end.getValue();
        String new_detail=detail.getText();
        String new_remark=remark.getText();
        if (new_name.isBlank()) {
            ErrorUtils.NameError();
            return;
        }
        EventUtils.updateEvent(event,new_name,new_start,new_end,new_importance,
                new_detail,new_remark,in,out,-1,-1);
        Pass.getMainShow().getChildren().clear();
        Pass.getMainShow().getChildren().add(new EventsShow(s,offset));
    }
}
