package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.Event;
import com.example.task_plan_manager.Globe;
import com.example.task_plan_manager.Pass;
import com.example.task_plan_manager.Utils.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;

public class PlanDetailShow extends VBox {

    private final static String TAG="com.example.task_plan_manager.Controllers.PlanDetailShow.";

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
        if (!setFileShow(detail_label,detail,"detail",(boolean) tmp.get(4))) {
            ErrorUtils.OperateFail();
            return;
        }
        if (!setDateBaseShow(file_label,file_local,file_local_add,file_cloud,
                file_cloud_add,(boolean) tmp.get(6), FileUtils.IN)) {
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

    private boolean setFileShow(VBox vBox, VBox text, String path, boolean have) {
        if (vBox==null||text==null||path==null) {
            ErrorUtils.NullPointerInputError(TAG+"setFileShow");
            return false;
        }
        vBox.setVisible(have);
        vBox.setManaged(have);
        if (have) {
            List<String>list= FileUtils.readFile("./data/"+ Globe.getUser().getId()+"/"+event+"/"+path);
            for (String s:list) {
                Label label=new Label();
                label.setFont(Font.font(20));
                label.setWrapText(true);
                label.setText(s);
                text.getChildren().add(label);
            }
        }
        return true;
    }

    private boolean setDateBaseShow(VBox label, VBox local, VBox local_add, VBox cloud, VBox cloud_add,
                                    boolean have, boolean io) {
        if (label==null||local==null||local_add==null||cloud==null||cloud_add==null) {
            ErrorUtils.NullPointerInputError(TAG+"setDateBaseShow");
            return false;
        }
        label.setVisible(have);
        label.setManaged(have);
        if (!have)return true;
        List<Pair<Integer, String>>tmp=DateBaseUtils.getFile(Globe.getUser().getId(),event,io,-1);
        if (tmp==null||tmp.isEmpty()) {
            local.setVisible(false);
            local.setManaged(false);
        } else {
            for (Pair<Integer, String> i:tmp) {
                int left=i.getValue().lastIndexOf('/')+1;
                local_add.getChildren().add(new FileLinkShow(i.getValue().substring(left),
                        i.getValue(),FileUtils.LOCAL,i.getKey(),event,local,cloud));
            }
        }
        tmp=DateBaseUtils.getFile(Globe.getUser().getId(),event,io,0);
        if (tmp==null||tmp.isEmpty()) {
            cloud.setVisible(false);
            cloud.setManaged(false);
        } else {
            for (Pair<Integer, String> i:tmp) {
                int left=i.getValue().lastIndexOf('/')+1;
                cloud_add.getChildren().add(new FileLinkShow(i.getValue().substring(left),
                        i.getValue(),FileUtils.CLOUD,i.getKey(),event,cloud,local));
            }
        }
        return true;
    }
}
