package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.Event;
import com.example.task_plan_manager.Pass;
import com.example.task_plan_manager.Utils.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Date;
import java.util.List;

public class PlanDetailShow extends EventDetailShow {

    private int event;

    @FXML private Label name;
    @FXML private Label start;
    @FXML private Label end;
    @FXML private Label important;
    @FXML private Label offset_label;
    @FXML private Label continue_time;
    @FXML private VBox detail_label;
    @FXML private VBox detail;
    @FXML private VBox file_label;
    @FXML private VBox file_local;
    @FXML private VBox file_local_add;
    @FXML private VBox file_cloud;
    @FXML private VBox file_cloud_add;
    @FXML private Button change;
    @FXML private Button delete_event;

    public PlanDetailShow(int id, int offset, boolean sub) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("plan_detail_show.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        event=id;
        List<Object> tmp= DateBaseUtils.getEventById(id);
        if (tmp==null) {
            ErrorUtils.OperateFail();
            return;
        }
        long startTime= (long) tmp.get(2);
        long endTime=(long) tmp.get(3);
        String name=(String) tmp.get(0);
        this.name.setText(name);
        int importance=(int) tmp.get(1);
        important.setText(Event.IMPORT[importance]);
        start.setText(OtherUtils.getDateString(new Date(startTime)));
        end.setText(OtherUtils.getDateString(new Date(endTime)));
        int tmp_offset=(int) tmp.get(8);
        int tmp_continue=(int) tmp.get(9);
        offset_label.setText(tmp_offset+"");
        continue_time.setText(tmp_continue+"");
        if (!setFileShow(detail_label,detail,"detail",(boolean) tmp.get(4), event)) {
            ErrorUtils.OperateFail();
            return;
        }
        if (!setDateBaseShow(file_label,file_local,file_local_add,file_cloud,
                file_cloud_add,(boolean) tmp.get(6), FileUtils.IN,event)) {
            ErrorUtils.OperateFail();
            return;
        }
        delete_event.setOnAction(e -> {
            List<Integer>out=DateBaseUtils.getEventIdByBelong(id);
            EventUtils.deleteEvent(id);
            assert out != null;
            for (int i:out)EventUtils.deleteEvent(i);
            Pass.getMainShow().getChildren().clear();
            Pass.getMainShow().getChildren().add(new EventsShow(null,offset-(sub?1:0)));
        });
        change.setOnAction(e -> {
            Pass.getMainShow().getChildren().clear();
            Pass.getMainShow().getChildren().add(new PlanUpdateShow(tmp,id,offset));
        });
    }
}
