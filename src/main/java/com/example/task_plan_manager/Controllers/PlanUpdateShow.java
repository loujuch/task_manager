package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.other.Globe;
import com.example.task_plan_manager.other.Pass;
import com.example.task_plan_manager.Utils.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlanUpdateShow extends EventUpdateShow {

    private int event;
    private int new_importance=0;
    private ArrayList<FileShow>in=new ArrayList<>();

    @FXML private TextField name;
    @FXML private DatePicker start;
    @FXML private DatePicker end;
    @FXML private ChoiceBox importance;
    @FXML private TextArea detail;
    @FXML private TextField space;
    @FXML private TextField continue_time;
    @FXML private VBox file_label;
    @FXML private VBox file_local;
    @FXML private VBox file_local_add;
    @FXML private VBox file_cloud;
    @FXML private VBox file_cloud_add;
    @FXML private Button file_add;
    @FXML private Button ok;

    public PlanUpdateShow(List<Object>tmp, int id, int offset) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("plan_update_show.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        event=id;
        name.setText((String) tmp.get(0));
        UIUtils.setChoiceBox(importance,(int) tmp.get(1));
        space.setText((int)tmp.get(8)+"");
        continue_time.setText((int)tmp.get(9)+"");
        new_importance=(int) tmp.get(1);
        start.setValue(new Date((long) tmp.get(2)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        end.setValue(new Date((long) tmp.get(3)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        start.setDisable(true);
        end.setDisable(true);
        continue_time.setDisable(true);
        space.setDisable(true);
        String path=Globe.getPath()+"/.data/"+ Globe.getUser().getId()+"/"+id+"/";
        if ((boolean) tmp.get(4))fileWrite(detail,path+"detail");
        setDateBaseShow(file_label,file_local,file_local_add,file_cloud,file_cloud_add,
                true,FileUtils.IN,in,event);
        boolean have=(boolean) tmp.get(7);
        importance.getSelectionModel().selectedIndexProperty().addListener(
                (observableValue, number, t1) -> new_importance=t1.intValue()>>>1);
        file_add.setOnAction(e -> UIUtils.onAddFile(file_local_add,in));
        ok.setOnAction(e -> onOK(offset,id));
        UIUtils.setNumberInput(space);
        UIUtils.setNumberInput(continue_time);
    }

    private void onOK(int offset, int event) {
        if (!UIUtils.isDuplicate(in)) {
            return;
        }
        String new_name=name.getText();
        LocalDate new_start=start.getValue();
        LocalDate new_end=end.getValue();
        String new_detail=detail.getText();
        int new_offset=Integer.parseInt(space.getText());
        int new_continue=Integer.parseInt(continue_time.getText());
        if (new_continue<=0||new_name.isBlank()) {
            ErrorUtils.NameError();
            return;
        }
        List<Integer>out=DateBaseUtils.getEventIdByBelong(event);
        for (int i:out) {
            EventUtils.updateEvent(i,new_name,null,null,new_importance,
                    new_detail,"",in,new ArrayList<>(),-1,-1);
        }
        EventUtils.updateEvent(event,new_name,new_start,new_end,new_importance,
                new_detail,"",in,new ArrayList<>(),new_offset,new_continue);
        Pass.getMainShow().getChildren().clear();
        Pass.getMainShow().getChildren().add(new EventsShow(null,offset));
    }
}
