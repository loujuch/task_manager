package com.example.task_plan_manager.Controllers;

import com.example.task_plan_manager.other.Globe;
import com.example.task_plan_manager.Utils.DateBaseUtils;
import com.example.task_plan_manager.Utils.FileUtils;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.util.List;

public class EventDetailShow extends VBox {

    protected boolean setFileShow(VBox vBox, VBox text, String path, boolean have, int event) {
        if (vBox==null||text==null||path==null) {
            return false;
        }
        vBox.setVisible(have);
        vBox.setManaged(have);
        if (have) {
            List<String> list= FileUtils.readFile(Globe.getPath()+"/.data/"+ Globe.getUser().getId()+"/"+event+"/"+path);
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

    protected boolean setDateBaseShow(VBox label, VBox local, VBox local_add, VBox cloud, VBox cloud_add,
                                    boolean have, boolean io, int event) {
        if (label==null||local==null||local_add==null||cloud==null||cloud_add==null) {
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
        tmp= DateBaseUtils.getFile(Globe.getUser().getId(),event,io,0);
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
