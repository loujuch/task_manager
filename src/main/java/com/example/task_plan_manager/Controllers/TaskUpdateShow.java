package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.Event;
import com.example.task_plan_manager.Globe;
import com.example.task_plan_manager.Pass;
import com.example.task_plan_manager.Utils.DateBaseUtils;
import com.example.task_plan_manager.Utils.ErrorUtils;
import com.example.task_plan_manager.Utils.EventUtils;
import com.example.task_plan_manager.Utils.FileUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class TaskUpdateShow extends VBox {

    private final static String TAG="com.example.task_plan_manager.TaskUpdateShow.";

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
            ErrorUtils.NullPointerInputError(TAG+"UpdateTaskShow");
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
        importance.getItems().add(Event.IMPORT[0]);
        importance.getItems().add(new Separator());
        importance.getItems().add(Event.IMPORT[1]);
        importance.getItems().add(new Separator());
        importance.getItems().add(Event.IMPORT[2]);
        importance.setValue(Event.IMPORT[(int) tmp.get(1)]);
        new_importance=(int) tmp.get(1);
        start.setValue(new Date((long) tmp.get(2)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        end.setValue(new Date((long) tmp.get(3)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        String path="./data/"+ Globe.getUser().getId()+"/"+id+"/";
        if ((boolean) tmp.get(4))fileWrite(detail,path+"detail");
        boolean finish= (boolean) tmp.get(10);
        remark_label.setVisible(finish);
        remark_label.setManaged(finish);
        if (finish&&(boolean) tmp.get(5))fileWrite(remark,path+"remark");
        setDateBaseShow(file_label,file_local,file_local_add,file_cloud,file_cloud_add,
                true,FileUtils.IN,in);
        boolean have=(boolean) tmp.get(7);
        other_label.setManaged(finish);
        other_label.setVisible(finish);
        if (finish) setDateBaseShow(other_label,other_local,other_local_add,other_cloud,other_cloud_add,
                true,FileUtils.OUT,out);
        importance.getSelectionModel().selectedIndexProperty().addListener(
                (observableValue, number, t1) -> new_importance=t1.intValue()>>>1);
        file_add.setOnAction(e -> onAddFile(file_local_add,in));
        if (finish)other_add.setOnAction(e -> onAddFile(other_local_add,out));
        ok.setOnAction(e -> onOK(offset,s));
    }

    private void onOK(int offset, String s) {
        HashSet<String> set=new HashSet<>();
        for (FileShow i:in) {
            if (set.contains(i.getName())) {
                ErrorUtils.FileNameRepeat();
                return;
            }
            set.add(i.getName());
        }
        for (FileShow i:out) {
            if (set.contains(i.getName())) {
                ErrorUtils.FileNameRepeat();
                return;
            }
            set.add(i.getName());
        }
        String new_name=name.getText();
        LocalDate new_start=start.getValue();
        LocalDate new_end=end.getValue();
        String new_detail=detail.getText();
        String new_remark=remark.getText();
        if (new_name.isBlank()) {
            ErrorUtils.InputLengthError();
            return;
        }
        EventUtils.updateEvent(event,new_name,new_start,new_end,new_importance,
                new_detail,new_remark,in,out,-1,-1);
        Pass.getMainShow().getChildren().clear();
        Pass.getMainShow().getChildren().add(new EventsShow(s,offset));
    }

    public void onAddFile(VBox root, ArrayList<FileShow>tmp) {
        FileChooser fileChooser=new FileChooser();
        List<File>list=fileChooser.showOpenMultipleDialog(new Stage());
        if(list!=null) {
            for (File file:list) {
                FileShow fileShow=new FileShow(root,file,tmp);
                tmp.add(fileShow);
                root.getChildren().add(fileShow);
            }
        }
    }

    private boolean fileWrite(TextArea text, String path) {
        if (text==null||path==null) {
            ErrorUtils.NullPointerInputError(TAG+"fileWrite");
            return false;
        }
        ArrayList<String>tmp= FileUtils.readFile(path);
        if (tmp==null||tmp.isEmpty())return false;
        text.setText(tmp.get(0));
        for(int i=1;i<tmp.size();++i) text.appendText("\n"+tmp.get(i));
        return false;
    }

    private boolean setDateBaseShow(VBox label, VBox local, VBox local_add, VBox cloud, VBox cloud_add,
                                    boolean have, boolean io, ArrayList<FileShow>mid) {
        if (label==null||local==null||local_add==null||cloud==null||cloud_add==null) {
            ErrorUtils.NullPointerInputError(TAG+"setDateBaseShow");
            return false;
        }
        label.setVisible(have);
        label.setManaged(have);
        if (!have)return true;
        List<Pair<Integer, String>>tmp= DateBaseUtils.getFile(Globe.getUser().getId(),event,io,-1);
        if (tmp==null) {
            local.setVisible(false);
            local.setManaged(false);
        } else {
            for (Pair<Integer, String> i:tmp) {
                FileShow fileShow=new FileShow(local_add,new File(i.getValue()),mid);
                local_add.getChildren().add(fileShow);
                mid.add(fileShow);
            }
        }
        tmp=DateBaseUtils.getFile(Globe.getUser().getId(),event,io,0);
        if (tmp==null||tmp.isEmpty()) {
            cloud.setVisible(false);
            cloud.setManaged(false);
        } else {
            for (Pair<Integer, String> i:tmp) {
                FileShow fileShow=new FileShow(local_add,new File(i.getValue()),mid);
                cloud_add.getChildren().add(fileShow);
                mid.add(fileShow);
            }
        }
        return true;
    }
}
