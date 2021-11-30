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

public class TaskDetailShow extends EventDetailShow {

    private int event;

    @FXML private Label name;
    @FXML private Label start;
    @FXML private Label end;
    @FXML private Label important;
    @FXML private Label status;
    @FXML private VBox detail_label;
    @FXML private VBox detail;
    @FXML private VBox remark_label;
    @FXML private VBox remark;
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
    @FXML private Button change;
    @FXML private Button finish;
    @FXML private Button delete_event;

    TaskDetailShow(int id, int offset, String s, boolean sub) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("task_detail_show.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.event=id;
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
        if (!(setFileShow(detail_label,detail,"detail",(boolean) tmp.get(4),event)&&
                setFileShow(remark_label,remark,"remark",(boolean) tmp.get(5),event))) {
            ErrorUtils.OperateFail();
            return;
        }
        if (!(setDateBaseShow(file_label,file_local,file_local_add,file_cloud,
                file_cloud_add,(boolean) tmp.get(6),FileUtils.IN,event)&&
            setDateBaseShow(other_label,other_local,other_local_add,other_cloud,
                    other_cloud_add,(boolean) tmp.get(7),FileUtils.OUT,event))) {
            ErrorUtils.OperateFail();
            return;
        }
        boolean finish_flag=(boolean) tmp.get(10);
        if(finish_flag) {
            status.setText("已完成");
        } else {
            long now=new Date().getTime();
            if(now>endTime)status.setText("已过时");
            else if(now<startTime)status.setText("未开始");
            else status.setText("正进行");
        }
        delete_event.setOnAction(e -> {
            EventUtils.deleteEvent(event);
            Pass.getMainShow().getChildren().clear();
            Pass.getMainShow().getChildren().add(new EventsShow(s,offset-(sub?1:0)));
        });
        finish.setVisible(!finish_flag);
        finish.setManaged(!finish_flag);
        if (!finish_flag) {
            finish.setOnAction(e -> {
                Pass.getMainShow().getChildren().clear();
                Pass.getMainShow().getChildren().add(new TaskCreateShow
                        (id,name,startTime,endTime,importance,offset,s,sub));
            });
        }
        change.setOnAction(e -> {
            Pass.getMainShow().getChildren().clear();
            Pass.getMainShow().getChildren().add(new TaskUpdateShow(tmp,id,offset,s));
        });
    }
}